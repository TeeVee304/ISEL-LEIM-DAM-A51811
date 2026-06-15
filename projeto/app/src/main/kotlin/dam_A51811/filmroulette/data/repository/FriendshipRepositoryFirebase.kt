package dam_A51811.filmroulette.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import dam_A51811.filmroulette.data.model.Friendship
import dam_A51811.filmroulette.data.model.FriendshipStatus
import dam_A51811.filmroulette.data.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Dispatchers

/**
 * Firebase implementation of [FriendshipRepository] using Firestore.
 *
 * @property db The [FirebaseFirestore] instance used for database operations.
 */
class FriendshipRepositoryFirebase(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : FriendshipRepository {

    override suspend fun sendFriendRequest(requesterId: String, receiverId: String) {
        val batch = db.batch()
        
        val requesterRef = db.collection("users").document(requesterId).collection("friends").document(receiverId)
        val receiverRef = db.collection("users").document(receiverId).collection("friends").document(requesterId)
        
        val friendshipData = hashMapOf(
            "userId1" to requesterId,
            "userId2" to receiverId,
            "status" to FriendshipStatus.PENDING.name,
            "requesterId" to requesterId
        )
        
        batch.set(requesterRef, friendshipData)
        batch.set(receiverRef, friendshipData)
        batch.commit().await()
    }

    override suspend fun acceptFriendRequest(userId: String, otherUserId: String) {
        val batch = db.batch()
        
        val userRef = db.collection("users").document(userId).collection("friends").document(otherUserId)
        val otherUserRef = db.collection("users").document(otherUserId).collection("friends").document(userId)
        
        batch.update(userRef, "status", FriendshipStatus.ACCEPTED.name)
        batch.update(otherUserRef, "status", FriendshipStatus.ACCEPTED.name)
        batch.commit().await()
    }

    override suspend fun removeFriendOrRequest(userId: String, otherUserId: String) {
        val batch = db.batch()
        
        val userRef = db.collection("users").document(userId).collection("friends").document(otherUserId)
        val otherUserRef = db.collection("users").document(otherUserId).collection("friends").document(userId)
        
        batch.delete(userRef)
        batch.delete(otherUserRef)
        batch.commit().await()
    }

    override fun getFriends(userId: String): Flow<List<User>> = callbackFlow {
        val listener = db.collection("users").document(userId).collection("friends")
            .whereEqualTo("status", FriendshipStatus.ACCEPTED.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val friendIds = snapshot.documents.map { it.id }
                    if (friendIds.isEmpty()) {
                        trySend(emptyList())
                    } else {
                        // Firestore 'in' queries support a maximum of 10 elements.
                        // We must chunk the friend IDs into sublists of 10 and fetch them separately.
                        val chunks = friendIds.chunked(10)
                        val users = mutableListOf<User>()
                        
                        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                            try {
                                for (chunk in chunks) {
                                    val usersSnapshot = db.collection("users").whereIn("id", chunk).get().await()
                                    users.addAll(usersSnapshot.toObjects(User::class.java))
                                }
                                trySend(users)
                            } catch (e: Exception) {
                                
                            }
                        }
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override fun getPendingRequests(userId: String): Flow<List<Friendship>> = callbackFlow {
        val listener = db.collection("users").document(userId).collection("friends")
            .whereEqualTo("status", FriendshipStatus.PENDING.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val friendships = snapshot.documents.mapNotNull {
                        val u1 = it.getString("userId1") ?: return@mapNotNull null
                        val u2 = it.getString("userId2") ?: return@mapNotNull null
                        val reqId = it.getString("requesterId") ?: return@mapNotNull null
                        val statusStr = it.getString("status") ?: "PENDING"
                        val status = try { FriendshipStatus.valueOf(statusStr) } catch(e: Exception) { FriendshipStatus.PENDING }
                        Friendship(u1, u2, status, reqId)
                    }
                    trySend(friendships)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getFriendship(userId: String, otherUserId: String): Friendship? {
        val doc = db.collection("users").document(userId).collection("friends").document(otherUserId).get().await()
        if (doc.exists()) {
            val u1 = doc.getString("userId1") ?: return null
            val u2 = doc.getString("userId2") ?: return null
            val reqId = doc.getString("requesterId") ?: return null
            val statusStr = doc.getString("status") ?: "PENDING"
            val status = try { FriendshipStatus.valueOf(statusStr) } catch(e: Exception) { FriendshipStatus.PENDING }
            return Friendship(u1, u2, status, reqId)
        }
        return null
    }

    override suspend fun getFriendIds(userId: String): List<String> {
        val snapshot = db.collection("users").document(userId).collection("friends")
            .whereEqualTo("status", FriendshipStatus.ACCEPTED.name)
            .get()
            .await()
        return snapshot.documents.map { it.id }
    }

    override suspend fun getOrCreateUser(email: String, username: String?, avatarUrl: String?): User {
        val existingSnapshot = db.collection("users").whereEqualTo("email", email).get().await()
        if (!existingSnapshot.isEmpty) {
            val doc = existingSnapshot.documents.first()
            val existingUser = doc.toObject(User::class.java)!!

            // Verify if the requested username or avatarUrl differ from the stored ones, and update if necessary
            var needsUpdate = false
            var newUsername = existingUser.username
            var newAvatarUrl = existingUser.avatarUrl
            
            if (username != null && existingUser.username != username) {
                needsUpdate = true
                newUsername = username
            }
            if (avatarUrl != null && existingUser.avatarUrl != avatarUrl) {
                needsUpdate = true
                newAvatarUrl = avatarUrl
            }
            
            if (needsUpdate) {
                db.collection("users").document(existingUser.id).update(
                    mapOf(
                        "username" to newUsername,
                        "avatarUrl" to newAvatarUrl
                    )
                ).await()
                return existingUser.copy(username = newUsername, avatarUrl = newAvatarUrl)
            }
            
            return existingUser
        }

        // The user doesn't exist yet, we need to create a new profile.
        // We will try to map the new profile to an existing Firebase Auth user by email.
        val authUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        val uid = if (authUser != null && authUser.email == email) {
            authUser.uid
        } else {
            db.collection("users").document().id 
        }
        
        val finalUsername = username ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
        val newUser = User(
            id = uid,
            username = finalUsername,
            email = email,
            registryDate = System.currentTimeMillis(),
            avatarUrl = avatarUrl
        )
        
        db.collection("users").document(uid).set(newUser).await()
        return newUser
    }

    override suspend fun getUserByEmail(email: String): User? {
        val snapshot = db.collection("users").whereEqualTo("email", email).get().await()
        if (!snapshot.isEmpty) {
            return snapshot.documents.first().toObject(User::class.java)
        }
        return null
    }

    override suspend fun getUserById(id: String): User? {
        val doc = db.collection("users").document(id).get().await()
        if (doc.exists()) {
            return doc.toObject(User::class.java)
        }
        return null
    }
}
