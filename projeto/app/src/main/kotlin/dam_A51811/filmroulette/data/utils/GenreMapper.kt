package dam_A51811.filmroulette.data.utils

import dam_A51811.filmroulette.data.model.Genre

object GenreMapper {
    fun fromTmdbId(id: Int): Genre {
        return when (id) {
            28 -> Genre.ACTION
            12 -> Genre.ADVENTURE
            16 -> Genre.ANIMATION
            35 -> Genre.COMEDY
            80 -> Genre.CRIME
            99 -> Genre.DOCUMENTARY
            18 -> Genre.DRAMA
            10751 -> Genre.FAMILY
            14 -> Genre.FANTASY
            36 -> Genre.HISTORY
            27 -> Genre.HORROR
            10402 -> Genre.MUSIC
            9648 -> Genre.MYSTERY
            10749 -> Genre.ROMANCE
            878 -> Genre.SCIENCE_FICTION
            53 -> Genre.THRILLER
            10770 -> Genre.TV_MOVIE
            10752 -> Genre.WAR
            37 -> Genre.WESTERN
            else -> Genre.OTHER
        }
    }

    fun toTmdbId(genre: Genre): Int? {
        return when (genre) {
            Genre.ACTION -> 28
            Genre.ADVENTURE -> 12
            Genre.ANIMATION -> 16
            Genre.COMEDY -> 35
            Genre.CRIME -> 80
            Genre.DOCUMENTARY -> 99
            Genre.DRAMA -> 18
            Genre.FAMILY -> 10751
            Genre.FANTASY -> 14
            Genre.HISTORY -> 36
            Genre.HORROR -> 27
            Genre.MUSIC -> 10402
            Genre.MYSTERY -> 9648
            Genre.ROMANCE -> 10749
            Genre.SCIENCE_FICTION -> 878
            Genre.THRILLER -> 53
            Genre.TV_MOVIE -> 10770
            Genre.WAR -> 10752
            Genre.WESTERN -> 37
            Genre.OTHER -> null
        }
    }
}
