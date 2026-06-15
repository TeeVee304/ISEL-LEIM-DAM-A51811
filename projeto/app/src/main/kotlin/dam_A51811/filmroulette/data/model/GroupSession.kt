package dam_A51811.filmroulette.data.model

import dam_A51811.filmroulette.data.model.Movie

/**
 * Represents the current status of a group session.
 */
enum class SessionStatus {
    WAITING,
    SPINNING,
    MATCHED
}

/**
 * Represents a group session where users can vote on a deck of movies to find a match.
 *
 * @param id The unique identifier for the session.
 * @param hostId The unique identifier of the user hosting the session.
 * @param status The current operational status of the session.
 * @param createdAt The timestamp of when the session was created.
 * @param sharedFilters The filters applied to generate the movie deck.
 * @param moviesDeck The list of movies to be voted on by session participants.
 * @param rightSwipes A map tracking the movies each user has approved, keyed by user ID.
 * @param matchedMovie The final movie selected by the group, if any.
 */
data class GroupSession(
    val id: String = "",
    val hostId: String = "",
    val status: SessionStatus = SessionStatus.WAITING,
    val createdAt: Long = 0L,
    val sharedFilters: SharedFilters = SharedFilters(),
    val moviesDeck: List<Movie> = emptyList(),
    val rightSwipes: Map<String, List<String>> = emptyMap(),
    val matchedMovie: Movie? = null
)

/**
 * Represents the combined filters agreed upon by the group to generate the movie deck.
 *
 * @param selectedGenres The list of genre identifiers to include.
 * @param maxMinutes The maximum allowed duration for a movie in minutes.
 * @param selectedPlatforms The list of platform identifiers where the movie must be available.
 * @param selectedLanguages The list of language codes the movie must support.
 * @param selectedDecades The list of decades the movie must belong to.
 */
data class SharedFilters(
    val selectedGenres: List<String> = emptyList(),
    val maxMinutes: Int = 120,
    val selectedPlatforms: List<Int> = emptyList(),
    val selectedLanguages: List<String> = emptyList(),
    val selectedDecades: List<String> = emptyList()
)

/**
 * Represents a participant in a group session.
 *
 * @param userId The unique identifier of the user.
 * @param name The display name of the user.
 * @param avatarUrl The URL of the user's avatar image.
 * @param isReady Indicates whether the user is ready to begin the session.
 * @param individualFilters The specific filters preferred by this individual user.
 */
data class SessionMember(
    val userId: String = "",
    val name: String = "",
    val avatarUrl: String? = null,
    val isReady: Boolean = false,
    val individualFilters: IndividualFilters? = null
)

/**
 * Represents the personal filters selected by an individual user before joining the group filters.
 *
 * @param selectedGenres The list of genre identifiers preferred by the user.
 * @param maxMinutes The maximum preferred duration for a movie in minutes.
 */
data class IndividualFilters(
    val selectedGenres: List<String> = emptyList(),
    val maxMinutes: Int = 120
)
