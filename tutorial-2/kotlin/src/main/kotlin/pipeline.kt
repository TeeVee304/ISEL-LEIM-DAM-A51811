class Pipeline {
    // Armazena os passos como uma lista de Pares (Pair): {Passo, Função de transformação}
    private val stages = mutableListOf<Pair<String, (List<String>) -> List<String>>>()

    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        stages.add(Pair(name, transform))
    }

    // .fold(): pega no input inicial e passa o resultado de cada etapa para a seguinte
    fun execute(input: List<String>): List<String> {
        return stages.fold(input) { current, stage ->
            stage.second(current) // stage.second = segundo elemento do par (Pair)
        }
    }

    fun describe() {
        println("Pipeline stages :")
        stages.forEachIndexed { index, stage ->
            println("${index + 1}. ${stage.first}")
        }
    }

    // CHALLENGE: fork() [2%]
    fun fork(input: List<String>, otherPipeline: Pipeline): Pair<List<String>, List<String>> {
        val firstFork = this.execute(input)
        val secondFork = otherPipeline.execute(input)

        return Pair(firstFork, secondFork)
    }
}

/* Higher-level function que utiliza "lambda with receiver" de notação A.(B) -> C,
   representando uma função chamada sobre o objeto (receiver) A com o parâmetro B
   retornando C. */
fun buildPipeline(lambda: Pipeline.() -> Unit): Pipeline {
    val pipeline = Pipeline()
    pipeline.lambda()
    return pipeline
}

// ==============================< main >==============================
fun main() {
    // Lista indicada no enunciado do Tutorial 2
    val logs = listOf(
        " INFO : server started ",
        " ERROR : disk full ",
        " DEBUG : checking config ",
        " ERROR : out of memory ",
        " INFO : request received ",
        " ERROR : connection timeout "
    )

    val pipeline = buildPipeline {
        // Trim
        addStage("Trim") { list ->
            list.map { it.trim() } // trim(): Remove os espaços em branco no início e no fim
        }
        // Filter errors
        addStage("Filter errors") { list ->
            list.filter { it.contains("ERROR") }
        }
        // Uppercase
        addStage("Uppercase") { list ->
            list.map { it.uppercase() }
        }
        // Add index
        addStage("Add index") { list ->
            list.mapIndexed { index, line -> "${index + 1}. $line" }
            // index recebe a posição do elemento na lista (map)
            // line recebe a string de texto correspondente
        }
    }

    pipeline.describe()

    val exe = pipeline.execute(logs)

    println("Result :")
    exe.forEach { println(it) }
}

