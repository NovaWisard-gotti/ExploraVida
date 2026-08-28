package com.educalab.exploravida.domain.engine

/**
 * Controla el modo Conexiones: el nino une dos sistemas y el motor
 * decide si la relacion existe de verdad y por que.
 */
class ConnectionEngine(private val body: BodySystemEngine) {

    enum class Status { VALIDA, DUPLICADA, INVALIDA, MISMO_SISTEMA, DESCONOCIDA }

    data class Result(
        val status: Status,
        val explanation: String,
        val madeCount: Int,
        val totalCount: Int
    )

    private val made = LinkedHashSet<Pair<String, String>>()

    fun connect(from: String, to: String): Result {
        if (!body.exists(from) || !body.exists(to)) {
            return result(Status.DESCONOCIDA, "Ese sistema no esta en el laboratorio.")
        }
        if (from == to) {
            return result(Status.MISMO_SISTEMA, "Un sistema no se conecta consigo mismo. Busca un companero.")
        }
        val pair = from to to
        if (pair in made) {
            return result(Status.DUPLICADA, "Esa conexion ya la habias descubierto. Prueba con otra pareja.")
        }
        val explanation = body.explanationFor(from, to)
        return if (explanation == null) {
            val alternative = body.neighbors(from).firstOrNull()
            val hint = if (alternative == null) {
                "Observa de nuevo el recorrido."
            } else {
                "Fijate en lo que hace " + (body.system(from)?.name ?: from) + " justo despues."
            }
            result(Status.INVALIDA, "Todavia no. " + hint)
        } else {
            made.add(pair)
            result(Status.VALIDA, explanation)
        }
    }

    fun madeConnections(): Set<Pair<String, String>> = made.toSet()

    fun coverage(): Float = body.coverage(made)

    fun isComplete(): Boolean = made.size >= body.totalLinks()

    fun restore(pairs: Collection<Pair<String, String>>) {
        made.clear()
        pairs.filter { body.isValidLink(it.first, it.second) }.forEach { made.add(it) }
    }

    fun reset() = made.clear()

    private fun result(status: Status, explanation: String) =
        Result(status, explanation, made.size, body.totalLinks())
}
