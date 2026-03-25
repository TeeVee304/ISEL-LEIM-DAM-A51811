import kotlin.math.roundToLong

/* NOTA: Classes 'sealed' são sempre abstratas */
sealed class Event {
    abstract val username: String // que possa ser utilizada nas extended functions
    // NOTA: construtor na sealed class não funciona nesta situação
    class Login(override val username: String, val timestamp: Long): Event()
    class Purchase(override val username: String, val amount: Double, val timestamp: Long): Event()
    class Logout(override val username: String, val timestamp: Long): Event()

    // Para ter o output desejado na última linha
    override fun toString(): String {
        return when (this) {
            is Login -> "\tLogin(username=$username, timestamp=$timestamp)"
            is Purchase -> "\tPurchase(username=$username, amount=$amount, timestamp=$timestamp)"
            is Logout -> "\tLogout(username=$username, timestamp=$timestamp)"
        }
    }
}

// EXTENSION FUNCIONS de List<Event>: Acrescentam funções novas à classe "lista de Events"
fun List<Event>.filterByUser(username: String): List<Event> {
    return this.filter { it.username == username }
}

fun List<Event>.totalSpent(username: String): Double {
    return this.filterIsInstance<Event.Purchase>() // Seleciona exclusivamente eventos de compra
        .filter { it.username == username }        // Filtra por username
        .sumOf { it.amount }                       // Faz a soma do valor de todas as compras do utilizador
}

/* HIGHER-ORDER FUNCTION: É uma função que trata outras funções como dados comuns.
Neste caso, para passar uma função (handler, que aceita um event como parâmetro único) como argumento.
NOTA: Passar 'Unit' como produto de handler simplesmente significa que o "tipo" do resultado é indiferente
(aka 'void' em Java). */
fun processEvents(events: List<Event>, handler: (Event) -> Unit) {
    events.forEach { handler(it) }
}

// ==============================< main >==============================
// Sequência de comandos indicada no Tutorial 2
fun main() {
    val events = listOf(
        Event.Login("Alice", 1_000),
        Event.Purchase("Alice", 49.99, 1_100),
        Event.Purchase("Bob", 19.99, 1_200),
        Event.Login("Bob", 1_050),
        Event.Purchase("Alice", 15.00, 1_300),
        Event.Logout("Alice", 1_400),
        Event.Logout("Bob", 1_500)
    )
    // para o output desejado...
    processEvents(events, handler = {
        when (it) {
            is Event.Login -> println("[LOGIN] ${it.username} logged in at t=${it.timestamp}")
            is Event.Purchase -> println("[PURCHASE] ${it.username} spent $${it.amount} at t=${it.timestamp}")
            is Event.Logout -> println("[LOGOUT] ${it.username} logged out at t=${it.timestamp}")
        }
    })

    println("\nTotal spent by Alice: $${events.totalSpent("Alice")}")
    println("Total spent by Bob: $${events.totalSpent("Bob")}")

    println("\nEvents for Alice:")
    events.filterByUser("Alice").forEach { println(it) }
}