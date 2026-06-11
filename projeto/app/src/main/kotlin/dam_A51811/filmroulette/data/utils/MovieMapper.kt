package dam_A51811.filmroulette.data.utils

import dam_A51811.filmroulette.data.remote.dto.TmdbMovieDetailsDto

object MovieMapper {
    fun TmdbMovieDetailsDto.toLocalMovie(): dam_A51811.filmroulette.data.local.Movie {
        val mappedGenres = this.genres?.map { GenreMapper.fromTmdbId(it.id) } ?: emptyList()
        val posterPathUrl = if (this.posterPath.isNullOrEmpty()) "" else "https://image.tmdb.org/t/p/w780${this.posterPath}"
        return dam_A51811.filmroulette.data.local.Movie(
            id = this.id,
            title = this.title,
            duration = this.runtime ?: 120,
            synopsys = this.overview ?: "",
            imgUrl = posterPathUrl,
            avgRating = this.voteAverage,
            genres = mappedGenres
        )
    }

    fun dam_A51811.filmroulette.data.local.Movie.toDomainMovie(): dam_A51811.filmroulette.data.model.Movie {
        return dam_A51811.filmroulette.data.model.Movie(
            id = this.id,
            title = this.title,
            duration = this.duration,
            synopsys = this.synopsys,
            imgUrl = this.imgUrl,
            avgRating = this.avgRating,
            genres = this.genres
        )
    }
}
