package dam_A51811.filmroulette.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dam_A51811.filmroulette.data.model.GroupSession
import dam_A51811.filmroulette.data.model.Movie
import dam_A51811.filmroulette.data.model.SessionMember
import dam_A51811.filmroulette.data.model.SharedFilters
import dam_A51811.filmroulette.data.model.SessionInvite
import dam_A51811.filmroulette.data.model.SessionStatus
import dam_A51811.filmroulette.data.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Firebase Firestore implementation of the [GroupSessionRepository].
 *
 * This repository handles all session-related operations, including real-time
 * synchronization of session state and members across clients using Firestore.
 *
 * @property db The [FirebaseFirestore] instance to use for database operations.
 */
class GroupSessionRepositoryFirebase(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : GroupSessionRepository {

    /**
     * Creates a new session document in Firestore.
     * Generates a randomized session code prefixed with "RLT-".
     */
    override suspend fun createSession(hostId: String): String {
        
        
        val code = "RLT-" + (1000..9999).random().toString()
        val session = GroupSession(
            id = code,
            hostId = hostId,
            status = SessionStatus.WAITING,
            createdAt = System.currentTimeMillis(),
            sharedFilters = SharedFilters()
        )
        db.collection("sessions").document(code).set(session).await()
        return code
    }

    override suspend fun joinSession(sessionId: String, user: User) {
        val member = SessionMember(
            userId = user.id,
            name = user.username,
            avatarUrl = user.avatarUrl,
            isReady = false
        )
        db.collection("sessions").document(sessionId)
            .collection("members").document(user.id).set(member).await()
    }

    override suspend fun leaveSession(sessionId: String, userId: String) {
        db.collection("sessions").document(sessionId)
            .collection("members").document(userId).delete().await()
    }

    /**
     * Destroys the session by deleting all sub-documents in the "members" collection
     * before deleting the main session document.
     */
    override suspend fun destroySession(sessionId: String) {
        val membersRef = db.collection("sessions").document(sessionId).collection("members")
        val membersSnapshot = membersRef.get().await()
        for (doc in membersSnapshot.documents) {
            doc.reference.delete().await()
        }
        db.collection("sessions").document(sessionId).delete().await()
    }

    override suspend fun updateSessionStatus(sessionId: String, status: SessionStatus) {
        db.collection("sessions").document(sessionId)
            .update("status", status).await()
    }

    override suspend fun updateSharedFilters(sessionId: String, filters: SharedFilters) {
        db.collection("sessions").document(sessionId)
            .update("sharedFilters", filters).await()
    }

    /**
     * Observes real-time changes using a Firestore snapshot listener.
     * Yields updates through a [callbackFlow].
     */
    override fun observeSession(sessionId: String): Flow<GroupSession?> = callbackFlow {
        val listener = db.collection("sessions").document(sessionId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    trySend(snapshot.toObject(GroupSession::class.java))
                } else {
                    trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }

    /**
     * Observes real-time member changes within the session's "members" subcollection.
     * Yields updates through a [callbackFlow].
     */
    override fun observeMembers(sessionId: String): Flow<List<SessionMember>> = callbackFlow {
        val listener = db.collection("sessions").document(sessionId)
            .collection("members")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    trySend(snapshot.toObjects(SessionMember::class.java))
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun startSpinning(sessionId: String, deck: List<Movie>) {
        db.collection("sessions").document(sessionId)
            .update(
                mapOf(
                    "status" to SessionStatus.SPINNING,
                    "moviesDeck" to deck
                )
            ).await()
    }

    override suspend fun swipeRight(sessionId: String, movie: Movie, userId: String) {
        db.collection("sessions").document(sessionId)
            .update("rightSwipes.${movie.id}", FieldValue.arrayUnion(userId)).await()
    }

    override suspend fun setMatchMovie(sessionId: String, movie: Movie) {
        db.collection("sessions").document(sessionId)
            .update(
                mapOf(
                    "status" to SessionStatus.MATCHED,
                    "matchedMovie" to movie
                )
            ).await()
    }

    override suspend fun sendInvite(
        sessionId: String,
        friendId: String,
        inviterName: String,
        inviterAvatarUrl: String?
    ) {
        val inviteData = hashMapOf(
            "sessionId" to sessionId,
            "inviterName" to inviterName,
            "inviterAvatarUrl" to inviterAvatarUrl
        )
        db.collection("users").document(friendId)
            .collection("sessionInvites").document(sessionId)
            .set(inviteData).await()
    }

    /**
     * Observes real-time invite documents under the user's sessionInvites subcollection.
     * Yields updates through a [callbackFlow].
     */
    override fun observeInvites(userId: String): Flow<List<SessionInvite>> = callbackFlow {
        val listener = db.collection("users").document(userId)
            .collection("sessionInvites")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val invites = snapshot.documents.mapNotNull { doc ->
                        val sessionId = doc.getString("sessionId") ?: return@mapNotNull null
                        val inviterName = doc.getString("inviterName") ?: return@mapNotNull null
                        val inviterAvatarUrl = doc.getString("inviterAvatarUrl")
                        SessionInvite(sessionId, inviterName, inviterAvatarUrl)
                    }
                    trySend(invites)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun clearInvite(userId: String, sessionId: String) {
        db.collection("users").document(userId)
            .collection("sessionInvites").document(sessionId)
            .delete().await()
    }

    /**
     * Uses a Firestore collection group query to find every session the user is a member of,
     * then destroys the session if the user is the host, or removes only their membership otherwise.
     */
    override suspend fun leaveAllSessionsForUser(userId: String) {
        val memberDocs = db.collectionGroup("members")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        for (memberDoc in memberDocs.documents) {
            val sessionRef = memberDoc.reference.parent.parent ?: continue
            val sessionDoc = sessionRef.get().await()
            if (!sessionDoc.exists()) continue

            val hostId = sessionDoc.getString("hostId")
            if (hostId == userId) {
                val membersSnapshot = sessionRef.collection("members").get().await()
                val batch = db.batch()
                for (m in membersSnapshot.documents) {
                    batch.delete(m.reference)
                }
                batch.delete(sessionRef)
                batch.commit().await()
            } else {
                memberDoc.reference.delete().await()
            }
        }
    }
}

