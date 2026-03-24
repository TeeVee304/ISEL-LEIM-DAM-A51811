package dam.exer_vl

class PhysicalBook(
    title: String,
    author: String,
    publicationYear: Int,
    initialCopies: Int,
    val weight: Int,
    var hasHardcover: Boolean = true
) : Book(title, author, publicationYear, initialCopies) {

    override fun getStorageInfo(): String {
        val hardcover = if (hasHardcover) "Yes" else "No"
        return "Book: ${weight}g, Hardcover: $hardcover"
    }
}