class Pipeline {
    // Armazena os passos como uma lista de Pares (Pair): {Passo, Função de transformação}
    private val stages = mutableListOf<Pair<String, (List<String>) -> List<String>>>()

    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        stages.add(Pair(name, transform))
    }
}