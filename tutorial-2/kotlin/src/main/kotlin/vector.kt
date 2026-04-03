import kotlin.math.sqrt

data class Vec2(val x: Double, val y: Double) : Comparable<Vec2> {
    // Adição {+}
    operator fun plus(other: Vec2): Vec2 = Vec2(this.x + other.x, this.y + other.y)
    // Subtração {-}
    operator fun minus(other: Vec2): Vec2 = Vec2(this.x - other.x, this.y - other.y)
    // Multiplicação escalar {*}
    operator fun times(scalar: Double): Vec2 = Vec2(this.x * scalar, this.y * scalar)
    // Negação {- unário}
    operator fun unaryMinus(): Vec2 = Vec2(-this.x, -this.y)

    override operator fun compareTo(other: Vec2): Int {
        return this.magnitude().compareTo(other.magnitude())
    }

    // Magnitude
    fun magnitude(): Double = sqrt(x * x + y * y)
    // Produto escalar
    fun dot(other: Vec2): Double = (this.x * other.x) + (this.y * other.y)
    // Vetor normalizado
    fun normalized(): Vec2 {
        val mag = magnitude()
        if (mag == 0.0) throw IllegalStateException("Cannot normalize a zero vector.")
        return Vec2(this.x / mag, this.y / mag)
    }

    // Overload do get()
    operator fun get(index: Int): Double {
        return when (index) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException("Valid indexes are 0 and 1.")
        }
    }
}

// ==============================< main >==============================
fun main() {
    val a = Vec2(3.0, 4.0)
    val b = Vec2(1.0, 2.0)

    println("a = $a") // a = Vec2(x=3.0, y=4.0)
    println("b = $b") // b = Vec2(x=1.0, y=2.0)
    println("a + b = ${a + b}") // a + b = Vec2(x=4.0, y=6.0)
    println("a - b = ${a - b}") // a - b = Vec2(x=2.0, y=2.0)
    println("a * 2.0 = ${a * 2.0}") // a * 2.0 = Vec2(x=6.0, y=8.0)
    println("-a = ${-a}") // -a = Vec2(x=-3.0, y=-4.0)
    println("|a| = ${a.magnitude()}") // |a| = 5.0
    println("a dot b = ${a.dot(b)}") // a dot b = 11.0
    println("norm(a) = ${a.normalized()}") // norm(a) = Vec2(x=0.6, y=0.8)
    println("a[0] = ${a[0]}") // a[0] = 3.0
    println("a[1] = ${a[1]}") // a[1] = 4.0
    println("a > b = ${a > b}") // a > b = true
    println("a < b = ${a < b}") // a < b = false

    val vectors = listOf(Vec2(1.0, 0.0), Vec2(3.0, 4.0), Vec2(0.0, 2.0))
    println("Longest = ${vectors.maxOrNull()}") // Longest = Vec2(x=3.0, y=4.0)
    println("Shortest = ${vectors.minOrNull()}") // Shortest = Vec2(x=1.0, y=0.0)
}

/* OVERLOAD: Significa usar o mesmo símbolo para realizar tarefas diferentes,
   dependendo dos parâmetros utilizados. */
/* OPERATOR: Serve para avisar o compilador que uma função específica vai
   "sobrecarregar" (overload) um operador default da linguagem. */
/* Interface COMPARABLE: Garante ao Kotlin que instâncias da classe podem ser ordenadas. */