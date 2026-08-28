package com.educalab.exploravida.domain.engine

/**
 * Valida el orden de los eventos que el nino coloca.
 * Nunca responde solo "correcto" o "incorrecto": devuelve siempre
 * cuantos pasos estan bien y una pista educativa.
 */
class SequenceEngine {

    data class Result(
        val solved: Boolean,
        val correctCount: Int,
        val total: Int,
        val firstErrorIndex: Int,
        val feedback: String
    )

    fun validate(expected: List<String>, given: List<String>): Result {
        if (expected.isEmpty()) {
            return Result(false, 0, 0, -1, "Esta secuencia todavia no tiene pasos.")
        }
        if (given.size != expected.size) {
            return Result(
                solved = false,
                correctCount = countCorrect(expected, given),
                total = expected.size,
                firstErrorIndex = given.size,
                feedback = "Faltan tarjetas por colocar. Coloca las " + expected.size + " tarjetas."
            )
        }
        val correct = countCorrect(expected, given)
        val firstError = (0 until expected.size).firstOrNull { expected[it] != given[it] } ?: -1
        return if (firstError == -1) {
            Result(true, correct, expected.size, -1, "Ese es el camino: cada paso prepara al siguiente.")
        } else {
            Result(
                solved = false,
                correctCount = correct,
                total = expected.size,
                firstErrorIndex = firstError,
                feedback = "Vas bien: " + correct + " de " + expected.size +
                    " tarjetas estan en su sitio. Revisa la tarjeta numero " + (firstError + 1) + "."
            )
        }
    }

    private fun countCorrect(expected: List<String>, given: List<String>): Int {
        var count = 0
        for (index in expected.indices) {
            if (index < given.size && expected[index] == given[index]) count++
        }
        return count
    }

    /** Pista suave: dice que tarjeta va primero entre las que aun no estan bien. */
    fun hint(expected: List<String>, given: List<String>): String {
        if (expected.isEmpty()) return "No hay nada que ordenar todavia."
        for (index in expected.indices) {
            if (index >= given.size || expected[index] != given[index]) {
                return "Piensa que ocurre en el paso " + (index + 1) + "."
            }
        }
        return "La secuencia ya esta completa."
    }

    /** Baraja estable: la misma semilla siempre da el mismo desorden. */
    fun shuffleDeterministic(items: List<String>, seed: Long): List<String> {
        if (items.size < 2) return items
        val result = items.toMutableList()
        var state = if (seed == 0L) 88172645463325252L else seed
        for (index in result.indices.reversed()) {
            state = state xor (state shl 13)
            state = state xor (state ushr 7)
            state = state xor (state shl 17)
            val target = ((state ushr 1) % (index + 1).toLong()).toInt()
            val temporary = result[index]
            result[index] = result[target]
            result[target] = temporary
        }
        return result
    }

    /** Puntuacion en estrellas segun intentos usados. */
    fun stars(attempts: Int, solved: Boolean): Int = when {
        !solved -> 0
        attempts <= 1 -> 3
        attempts == 2 -> 2
        else -> 1
    }
}
