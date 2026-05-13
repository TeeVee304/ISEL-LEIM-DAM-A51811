package dam_A51811.filmroulette.data.model

data class Rating(
    val userId: Long,
    val movieId: Long,
    val rating: Int,
    val ratedAt: Long
)