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
            14 -> Genre.FANTASY
            27 -> Genre.HORROR
            9648 -> Genre.MYSTERY
            10749 -> Genre.ROMANCE
            878 -> Genre.SCIENCE_FICTION
            53 -> Genre.THRILLER
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
            Genre.FANTASY -> 14
            Genre.HORROR -> 27
            Genre.MYSTERY -> 9648
            Genre.ROMANCE -> 10749
            Genre.SCIENCE_FICTION -> 878
            Genre.THRILLER -> 53
            Genre.WESTERN -> 37
            Genre.OTHER -> null
        }
    }
}
