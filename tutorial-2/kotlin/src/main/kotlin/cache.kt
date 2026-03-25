class Cache<K : Any, V : Any> {
    /* NOTA: Não podemos instanciar MutableMap<> diretamente porque se trata de uma interface
    * (que estende de Map<>). */
    private val storage = mutableMapOf<K, V>()

    fun put(key: K, value: V) { storage[key] = value }
    fun get(key: K): V? = storage[key]
    fun evict(key: K) { storage.remove(key) } // equivalente ao remove() em Python
    fun size(): Int = storage.size // DÚVIDA: size() já não é uma propriedade de MutableMap() por default?
}