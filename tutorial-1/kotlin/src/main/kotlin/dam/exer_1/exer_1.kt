package dam.exer_1

fun main() {
    val a = IntArray(50) { it * it }
    val b = (0..49).map { it * it }
    val c = Array(50) { it * it }
    println(a.joinToString())
    println(b.joinToString())
    println(c.joinToString())
}

