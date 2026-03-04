package dam.exer_3

fun main() {
    // 1. generateSequence: Cria uma sequência iniciada em 100.0.
    val heights = generateSequence(100.0) { height ->
        height * 0.6
    }
        // 2. filter: Mantém apenas as alturas que sejam iguais ou superiores a 1.0 metro.
        // FIX: Substituido o filter por takeWhile. Deste modo, a sequência torna-se finita.
        .takeWhile { it >= 1.0 }
        // 3. take: Limita a extração aos primeiros 15 ressaltos que passam no filtro.
        .take(15)
        // 4. toList: Força a avaliação da sequência até este ponto e converte o resultado numa List convencional.
        .toList()
    /*
     * 5. Formatação: Utiliza o joinToString para iterar a lista e unir os elementos numa única String.
     * FIX: O parâmetro postfix serve foi colocado para corrigir uma inconsistência na string de output.
     * A função "%.2f".format(it) garante que cada número é arredondado a duas casas decimais.
     */
    val output = heights.joinToString("m | ", postfix = "m") { "%.2f".format(it) }
    println(output)
}