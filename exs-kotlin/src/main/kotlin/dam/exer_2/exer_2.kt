import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    println("Supported operations:")
    println("  * Arithmetic    : +, -, *, /")
    println("  * Boolean Logic : &&, ||, !")
    println("  * Bitwise Shift : shl, shr")
    println("Type 'exit' to quit.")
    print("--------------------------------------------------")

    /*
     * Do-While: Ciclo de execução principal.
     * Processa continuamente as entradas até o utilizador digitar "exit".
     */
    do {
        print("\n> ")
        val input = scanner.nextLine().lowercase().trim() // Normaliza para minúsculas e remove espaços
        if (input.isEmpty()) continue // Ignora entradas vazias

        try {
            /*
             * Try-Catch: Divide input em partes (operador e operandos) com base em espaços (" ").
             * ISSUE: A síntaxe precisa de ser estritamente seguida, pelo que não podem haver espaços duplicados.
             */
            val parts = input.split(" ")

            // Condição para direcionar o cálculo com base no número de argumentos
            val result: Long = when {
                // Operação lógica unária (negação "!")
                parts.size == 2 && parts[0] == "!" -> {
                    val operand = parts[1].toLong()
                    if (operand == 0L) 1L else 0L
                }

                // Operações binárias (restantes)
                parts.size == 3 -> {
                    val a = parts[0].toLong()
                    val operator = parts[1]
                    val b = parts[2].toLong()

                    when (operator) {
                        // Aritmética
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> {
                            // Tratamento da divisão por zero (0L = '0 em formato long')
                            if (b == 0L) throw Exception("Cannot divide by zero (0).")
                            a / b
                        }
                        // Lógica Booleana (0 = Falso, de outro modo Verdadeiro)
                        "&&" -> if (a != 0L && b != 0L) 1L else 0L
                        "||" -> if (a != 0L || b != 0L) 1L else 0L
                        // Bitwise Shift
                        "shl" -> a shl b.toInt()
                        "shr" -> a shr b.toInt()

                        else -> throw IllegalArgumentException("Unknown operator: '$operator'")
                    }
                } else -> throw IllegalArgumentException("Invalid format. Please use 'A operator B' or '! A'.")
            }

            // Binário e Hexadecimal
            val booleanResult = result != 0L
            val hexResult = result.toString(16).uppercase()

            println(">> Decimal : $result")
            println(">> Hex     : 0x$hexResult")
            println(">> Boolean : $booleanResult")
            print("--------------------------------------------------")
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
        }
    } while (input != "exit")
}