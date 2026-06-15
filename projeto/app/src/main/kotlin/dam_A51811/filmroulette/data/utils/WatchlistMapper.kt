package dam_A51811.filmroulette.data.utils

import dam_A51811.filmroulette.data.local.MovieList as LocalMovieList
import dam_A51811.filmroulette.data.model.MovieList as DomainMovieList


/**
 * Mapper object for movie list entity conversions.
 */
object WatchlistMapper {
    /**
     * Converts a local database movie list into a domain movie list.
     *
     * @return A mapped domain movie list object.
     */
    fun LocalMovieList.toDomainMovieList(): DomainMovieList = DomainMovieList(
        id         = this.id.toString(),
        userId     = this.userId.toString(),
        name       = this.name,
        createdAt  = this.createdAt,
        visibility = VisibilityMapper.fromString(this.visibility)
    )
}
