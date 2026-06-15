package dam_A51811.filmroulette.data.model

/**
 * Represents a streaming or media platform.
 *
 * @param id The unique identifier of the platform.
 * @param name The display name of the platform.
 * @param logoUrl The URL of the platform's logo image.
 */
data class Platform(
    val id: Long,
    val name: String,
    val logoUrl: String
)