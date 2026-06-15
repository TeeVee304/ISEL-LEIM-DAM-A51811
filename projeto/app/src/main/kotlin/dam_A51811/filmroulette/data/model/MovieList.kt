package dam_A51811.filmroulette.data.model

import dam_A51811.filmroulette.data.model.Visibility

/**
 * Represents a custom list of movies created by a user.
 *
 * @param id The unique identifier of the movie list.
 * @param userId The unique identifier of the user who owns the list.
 * @param name The display name of the list.
 * @param createdAt The timestamp of when the list was created.
 * @param visibility The visibility setting of the list.
 * @param movies The collection of movies contained in the list.
 */
data class MovieList(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val createdAt: Long = 0L,
    
    val visibility: Visibility = Visibility.PRIVATE,
    
    val movies: List<Movie> = emptyList()
)
