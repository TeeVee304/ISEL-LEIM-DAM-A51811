package dam_A51811.filmroulette.data.utils

import dam_A51811.filmroulette.data.local.MovieList as LocalMovieList
import dam_A51811.filmroulette.data.model.MovieList as DomainMovieList

/**
 * Extension function to convert the local Room [LocalMovieList] entity
 * (which stores [visibility] as a String) to the domain [DomainMovieList]
 * model (which exposes the typed [dam_A51811.filmroulette.data.model.Visibility] enum).
 *
 * Note: movies inside the list are not loaded here — they are fetched
 * separately via a JOIN query when needed. The domain model defaults to an
 * empty list.
 *
 * ### Firebase note
 * When reading a Firestore document, map its fields to [DomainMovieList]
 * directly (bypassing the local entity) using the same field names.
 * [VisibilityMapper.fromString] handles the String → Visibility conversion.
 */
object WatchlistMapper {
    fun LocalMovieList.toDomainMovieList(): DomainMovieList = DomainMovieList(
        id         = this.id,
        userId     = this.userId,
        name       = this.name,
        createdAt  = this.createdAt,
        visibility = VisibilityMapper.fromString(this.visibility)
    )
}
