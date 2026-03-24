package dam.exer_vl

enum class BookFormat {
    PDF, EPUB, MOBI
}

class DigitalBook(
    title: String,
    author: String,
    publicationYear: Int,
    initialCopies: Int,
    val fileSize: Double,
    val format: BookFormat
) : Book(title, author, publicationYear, initialCopies) {

    override fun getStorageInfo(): String {
        return "Stored digitally: $fileSize MB, (${format.name})"
    }
}