package dam.exer_vl

data class LibraryMember(
    val name: String,
    val membershipId: String,
    val borrowedBooks: MutableList<String> = mutableListOf()
)

class Library(val name: String) {
    private val books = mutableListOf<Book>()

    companion object { // companion object =~ static
        private var totalBooksAdded = 0

        fun getTotalBooksCreated(): Int {
            return totalBooksAdded
        }
    }

    fun addBook(book: Book) {
        books.add(book)
        totalBooksAdded++
    }

    fun borrowBook(title: String) {
        val book = books.find { it.title == title }

        if (book != null) {
            if (book.availableCopies > 0) {
                book.availableCopies--
                println("'${book.title}' has been borrowed (${book.availableCopies} copies remaining).")
            } else {
                println("Sorry, no copies of '${book.title}' left at the moment.")
            }
        } else {
            println("Book '$title' not found.")
        }
    }

    fun returnBook(title: String) {
        val book = books.find { it.title == title }

        if (book != null) {
            book.availableCopies++
            println("Book '${book.title}' returned successfully. Copies available: ${book.availableCopies}")
        } else {
            println("Book '$title' not found.")
        }
    }

    fun showBooks() {
        println("=== Book Database ==")
        books.forEach { print(it.toString()) }
        println("====================")
    }

    fun searchByAuthor(author: String) {
        val authorBooks = books.filter { it.author == author }

        println("Books by $author:")
        if (authorBooks.isEmpty()) {
            println("No books found for this author.")
        } else {
            authorBooks.forEach {
                val copyWord = if (it.availableCopies == 1) "copy" else "copies"
                println("${it.title} (${it.era}, ${it.availableCopies} $copyWord available)")
            }
        }
    }
}