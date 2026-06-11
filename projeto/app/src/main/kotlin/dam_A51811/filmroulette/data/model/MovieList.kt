package dam_A51811.filmroulette.data.model

import dam_A51811.filmroulette.data.model.Visibility

data class MovieList(
    val id: Long,
    val userId: Long,
    val name: String,
    val createdAt: Long,
    /** Controls who can see this list. Defaults to [Visibility.PRIVATE]. */
    val visibility: Visibility = Visibility.PRIVATE,
    val movies: List<Movie> = emptyList()
)
