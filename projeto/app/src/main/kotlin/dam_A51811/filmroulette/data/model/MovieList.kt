package dam_A51811.filmroulette.data.model

data class MovieList(
    val id: Long,
    val userId: Long,
    val name: String,
    val createdAt: Long,
    val movies: List<Movie> = emptyList()
)
