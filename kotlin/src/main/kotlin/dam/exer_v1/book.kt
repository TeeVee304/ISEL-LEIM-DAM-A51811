package dam.exer_v1

abstract class Book(
    val title: String,
    val author: String,
    val publicationYear: Int,
    initialCopies: Int
) {
    init {
        println("Book '$title' by $author has been added to the library.")
    }

    val era: String
        get() = when {
            publicationYear < 1980 -> "Classic"
            publicationYear in 1980..2010 -> "Musica"
            else -> "Contemporary"
        }

    var availableCopies: Int = initialCopies
        set(value) {
            field = if (value < 0) 0 else value
            if (field == 0) println("Book is currently out of stock.")
        }
}