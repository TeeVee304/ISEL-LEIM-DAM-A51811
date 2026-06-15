package dam_A51811.filmroulette.data.utils

import dam_A51811.filmroulette.data.remote.dto.TmdbMovieDetailsDto

/**
 * Utility object for mapping movie data between different layers (remote, local, and domain).
 */
object MovieMapper {
    /**
     * Converts a [TmdbMovieDetailsDto] from the remote API into a local [dam_A51811.filmroulette.data.local.Movie] entity.
     *
     * @return The converted local movie entity.
     */
    fun TmdbMovieDetailsDto.toLocalMovie(): dam_A51811.filmroulette.data.local.Movie {
        val mappedGenres = this.genres?.map { GenreMapper.fromTmdbId(it.id) } ?: emptyList()
        val posterPathUrl = if (this.posterPath.isNullOrEmpty()) "" else "https://image.tmdb.org/t/p/w780${this.posterPath}"
        return dam_A51811.filmroulette.data.local.Movie(
            id = this.id,
            title = this.title,
            duration = this.runtime ?: 0,
            synopsys = this.overview ?: "",
            imgUrl = posterPathUrl,
            avgRating = this.voteAverage,
            releaseDate = this.releaseDate,
            originalLanguage = this.originalLanguage,
            genres = mappedGenres
        )
    }

    /**
     * Converts a local [dam_A51811.filmroulette.data.local.Movie] entity into a domain [dam_A51811.filmroulette.data.model.Movie] model.
     *
     * @return The converted domain movie model.
     */
    fun dam_A51811.filmroulette.data.local.Movie.toDomainMovie(): dam_A51811.filmroulette.data.model.Movie {
        return dam_A51811.filmroulette.data.model.Movie(
            id = this.id,
            title = this.title,
            duration = this.duration,
            synopsys = this.synopsys,
            imgUrl = this.imgUrl,
            avgRating = this.avgRating,
            releaseDate = this.releaseDate,
            originalLanguage = this.originalLanguage,
            genres = this.genres
        )
    }
}
