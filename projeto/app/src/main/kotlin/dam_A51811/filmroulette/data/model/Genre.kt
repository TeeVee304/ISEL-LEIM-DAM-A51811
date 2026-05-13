package dam_A51811.filmroulette.data.model

/**
 * Represents the genre of a movie.
 *
 * These are pre-defined categories shared across all [Movie] entities.
 * Stored in the database as the enum name (String) via [dam_A51811.filmroulette.data.local.GenreConverter].
 */
enum class Genre {
    ACTION,
    ADVENTURE,
    ANIMATION,
    COMEDY,
    CRIME,
    DOCUMENTARY,
    DRAMA,
    FANTASY,
    HORROR,
    MYSTERY,
    ROMANCE,
    SCIENCE_FICTION,
    THRILLER,
    WESTERN,
    OTHER
}