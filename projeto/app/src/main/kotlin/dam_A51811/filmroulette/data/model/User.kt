package dam_A51811.filmroulette.data.model

/**
 * Represents a user within the system.
 *
 * @property id The unique identifier for the user.
 * @property username The chosen display name of the user.
 * @property email The email address associated with the user account.
 * @property registryDate The timestamp indicating when the user registered, in milliseconds since the epoch.
 * @property avatarUrl The optional URL pointing to the user's avatar image.
 */
data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val registryDate: Long = 0L,
    val avatarUrl: String? = null
)