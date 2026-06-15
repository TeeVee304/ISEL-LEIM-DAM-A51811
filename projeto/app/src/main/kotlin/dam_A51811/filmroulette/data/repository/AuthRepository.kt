package dam_A51811.filmroulette.data.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

/**
 * Defines the contract for user authentication and profile management operations.
 */
interface AuthRepository {
    /**
     * A flow emitting the current authenticated user or null if not authenticated.
     */
    val currentUserFlow: Flow<FirebaseUser?>

    /**
     * Retrieves the currently authenticated user.
     *
     * @return The current user, or null if no user is authenticated.
     */
    fun getCurrentUser(): FirebaseUser?

    /**
     * Authenticates a user using an email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A result containing the authenticated user if successful, or an error otherwise.
     */
    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<FirebaseUser>

    /**
     * Registers a new user with an email, password, and profile information.
     *
     * @param email The new user's email address.
     * @param password The new user's password.
     * @param username The new user's display name.
     * @param photoUrl An optional URL to the new user's profile picture.
     * @return A result containing the newly registered user if successful, or an error otherwise.
     */
    suspend fun registerWithEmailAndPassword(email: String, password: String, username: String, photoUrl: String? = null): Result<FirebaseUser>

    /**
     * Updates the current user's profile information.
     *
     * @param username The new display name for the user, or null to keep unchanged.
     * @param photoUrl The new profile picture URL for the user, or null to keep unchanged.
     * @return A result indicating success or failure.
     */
    suspend fun updateProfile(username: String?, photoUrl: String?): Result<Unit>

    /**
     * Uploads a profile image for the current user.
     *
     * @param uri The local URI of the image to upload.
     * @return A result containing the download URL of the uploaded image if successful, or an error otherwise.
     */
    suspend fun uploadProfileImage(uri: android.net.Uri): Result<String>

    /**
     * Deletes the current authenticated user's account.
     *
     * @return A result indicating success or failure.
     */
    suspend fun deleteAccount(): Result<Unit>

    /**
     * Logs out the currently authenticated user.
     */
    fun logout()
}
