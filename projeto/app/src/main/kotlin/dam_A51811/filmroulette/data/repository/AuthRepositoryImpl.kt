package dam_A51811.filmroulette.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Implementation of [AuthRepository] that relies on Firebase Authentication and Firebase Storage.
 *
 * @property auth The [FirebaseAuth] instance used to manage user authentication.
 */
class AuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    /**
     * A flow that emits the current [FirebaseUser] whenever the authentication state changes.
     */
    override val currentUserFlow: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    /**
     * Retrieves the currently logged-in user synchronously.
     *
     * @return The current [FirebaseUser], or null if no user is authenticated.
     */
    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    /**
     * Authenticates a user using their email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] containing the authenticated [FirebaseUser] if successful, or an exception on failure.
     */
    override suspend fun loginWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("User not found after login")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Registers a new user with an email, password, and optional profile details.
     * Updates the user's profile with the provided username and photo URL after creation.
     *
     * @param email The new user's email address.
     * @param password The new user's password.
     * @param username The display name for the new user.
     * @param photoUrl An optional URL pointing to the user's profile image.
     * @return A [Result] containing the registered [FirebaseUser] if successful, or an exception on failure.
     */
    override suspend fun registerWithEmailAndPassword(email: String, password: String, username: String, photoUrl: String?): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("User not created")
            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .apply {
                    if (photoUrl != null) {
                        setPhotoUri(android.net.Uri.parse(photoUrl))
                    }
                }
                .build()
            user.updateProfile(profileUpdates).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Updates the currently authenticated user's profile information.
     *
     * @param username The new display name, or null to leave it unchanged.
     * @param photoUrl The new profile photo URL, or null to leave it unchanged. Passing a blank string removes the photo.
     * @return A [Result] indicating success or failure.
     */
    override suspend fun updateProfile(username: String?, photoUrl: String?): Result<Unit> {
        return try {
            val user = auth.currentUser ?: throw Exception("User not logged in")
            val builder = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            if (username != null) builder.setDisplayName(username)
            if (photoUrl != null) {
                if (photoUrl.isBlank()) {
                    builder.setPhotoUri(null)
                } else {
                    builder.setPhotoUri(android.net.Uri.parse(photoUrl))
                }
            }
            user.updateProfile(builder.build()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Uploads a profile image to Firebase Storage and retrieves its download URL.
     *
     * @param uri The local [android.net.Uri] of the image to upload.
     * @return A [Result] containing the remote download URL if successful, or an exception on failure.
     */
    override suspend fun uploadProfileImage(uri: android.net.Uri): Result<String> {
        return try {
            val user = auth.currentUser ?: throw Exception("User not logged in")
            val storageRef = FirebaseStorage.getInstance().reference
            val profileImageRef = storageRef.child("profile_images/${user.uid}.jpg")
            
            profileImageRef.putFile(uri).await()
            val downloadUrl = profileImageRef.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletes the currently authenticated user's account from Firebase Authentication.
     *
     * @return A [Result] indicating success or failure.
     */
    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            val user = auth.currentUser ?: throw Exception("User not logged in")
            val uid = user.uid

            // 1. Clean up Friendships
            val friendsSnapshot = db.collection("users").document(uid).collection("friends").get().await()
            for (doc in friendsSnapshot.documents) {
                val friendId = doc.id
                try {
                    // Delete reverse mapping
                    db.collection("users").document(friendId).collection("friends").document(uid).delete().await()
                } catch (e: Exception) {
                    // Ignore individual failures to ensure we proceed with deletion
                }
                try {
                    // Delete local mapping
                    doc.reference.delete().await()
                } catch (e: Exception) { }
            }

            // 2. Clean up Watchlists
            val watchlistsSnapshot = db.collection("users").document(uid).collection("movie_lists").get().await()
            for (listDoc in watchlistsSnapshot.documents) {
                try {
                    val moviesSnapshot = listDoc.reference.collection("movies").get().await()
                    for (movieDoc in moviesSnapshot.documents) {
                        movieDoc.reference.delete().await()
                    }
                    listDoc.reference.delete().await()
                } catch (e: Exception) { }
            }

            // 3. Clean up Profile
            try {
                db.collection("users").document(uid).delete().await()
            } catch (e: Exception) { }

            // 4. Delete Auth Record
            user.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Signs out the currently authenticated user.
     */
    override fun logout() {
        auth.signOut()
    }
}
