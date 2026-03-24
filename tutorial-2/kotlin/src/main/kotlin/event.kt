sealed class Event {
    class Login(val username: String, val timestamp: Long): Event()
    class Purchase(val username: String, val amount: Double, val timestamp: Long): Event()
    class Logout(val username: String, val timestamp: Long): Event()
}

fun List<Event>.filterByUser(username: String): List<Event> {
    return this.filter { it.username == username }
}
