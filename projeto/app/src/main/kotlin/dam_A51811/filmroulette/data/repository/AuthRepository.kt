package dam_A51811.filmroulette.data.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserFlow: Flow<FirebaseUser?>
    fun getCurrentUser(): FirebaseUser?
    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<FirebaseUser>
    suspend fun registerWithEmailAndPassword(email: String, password: String, username: String, photoUrl: String? = null): Result<FirebaseUser>
    fun logout()
}
