class Cache<K : Any, V : Any> {
    /* NOTA: Não podemos instanciar MutableMap<> diretamente porque se trata de uma interface
    * (que estende de Map<>). */
    private val storage = mutableMapOf<K, V>()

    fun put(key: K, value: V) { storage[key] = value }
    fun get(key: K): V? = storage[key]
    fun evict(key: K) { storage.remove(key) } // equivalente ao remove() em Python
    fun size(): Int = storage.size // DÚVIDA: size() já não é uma propriedade de MutableMap() por default?

    // '() -> V': Calcula o valor apenas se a chave não existir
    fun getOrPut(key: K, default: () -> V): V {
        // Get
        val getValue = storage[key]
        if (getValue != null) {
            return getValue
        }
        // Put
        val putValue = default()
        storage[key] = putValue
        return putValue
    }
    // Passa o valor atual V como argumento de action() para transformar (retornar) o novo valor V
    fun transform(key: K, action: (V) -> V): Boolean {
        val value = storage[key]
        if (value != null) {
            storage[key] = action(value)
            return true
        }
        return false
    }
    // Converte o MutableMap num (read-only) Map. Modificar a cache original não afetará a snapshot
    fun snapshot(): Map<K, V> {
        return storage.toMap()
    }
}

// ==============================< main >==============================
fun main() {
    val wordCache = Cache<String, Int>()
    wordCache.put("kotlin", 1)
    wordCache.put("scala", 1)
    wordCache.put("haskell", 1)

    println("--- Word frequency cache ---")
    println("Size : ${wordCache.size()}")
    println("Frequency of \"kotlin\": ${wordCache.get("kotlin")}")

    // getOrPut()
    println("getOrPut \"kotlin\": ${wordCache.getOrPut("kotlin") { 0 }}")
    println("getOrPut \"java\": ${wordCache.getOrPut("java") { 0 }}")
    println("Size after getOrPut : ${wordCache.size()}")

    // transform()
    println("Transform \"kotlin\" (+1) : ${wordCache.transform("kotlin") { it + 1 }}")
    println("Transform \"cobol\" (+1) : ${wordCache.transform("cobol") { it + 1 }}")

    // snapshot ()
    // NOTA: A string formatou-se de forma específica para encaixar no enunciado
    val snapshotStr = wordCache.snapshot().entries.joinToString(", ", "{ ", "}") { "${it.key} =${it.value} " }
    println("Snapshot : $snapshotStr")

    println("\n--- Id registry cache ---")
    val idCache = Cache<Int, String>()
    idCache.put(1, "Alice")
    idCache.put(2, "Bob")

    println("Id 1 -> ${idCache.get(1)}")
    println("Id 2 -> ${idCache.get(2)}")

    idCache.evict(1)

    println("After evict id 1 , size : ${idCache.size()}")
    println("Id 1 after evict -> ${idCache.get(1)}")
}

/*
public inline fun <K, V> MutableMap<K, V>.getOrPut(key: K, defaultValue: () -> V): V {
    val value = get(key)
    return if (value == null) {
        val answer = defaultValue()
        put(key, answer)
        answer
    } else {
        value
    }
}
*/