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


