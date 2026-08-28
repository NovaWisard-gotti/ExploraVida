package com.educalab.exploravida.domain.usecase

import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.domain.model.Systems

/**
 * Definicion de los retos que no son ni secuencia ni conexion.
 * Es contenido puro de Kotlin, sin Android, para poder probarlo.
 */
sealed class Challenge {

    /** Arrastrar un elemento ilustrado hasta Vita. */
    data class Drag(
        val activityId: String,
        val items: List<IllustrationKey>,
        val correctItems: Set<IllustrationKey>,
        val successText: String,
        val retryText: String
    ) : Challenge()

    /** Marcar sobre las ilustraciones los sistemas que participan. */
    data class Predict(
        val activityId: String,
        val question: String,
        val options: List<String>,
        val correct: Set<String>,
        val explanation: String
    ) : Challenge()

    /** Comparar dos simulaciones y decidir cual aguanta mas. */
    data class Compare(
        val activityId: String,
        val question: String,
        val labelA: String,
        val labelB: String,
        val energyA: Float,
        val energyB: Float,
        val correctIsA: Boolean,
        val explanation: String
    ) : Challenge()

    /** Observar y contar cuantas veces ocurre algo. */
    data class Observe(
        val activityId: String,
        val instruction: String,
        val target: Int,
        val explanation: String
    ) : Challenge()
}

object ChallengeCatalog {

    private val all: List<Challenge> = listOf(
        Challenge.Drag(
            activityId = "act_comer_arrastrar",
            items = listOf(
                IllustrationKey.MANZANA, IllustrationKey.PAN,
                IllustrationKey.ZANAHORIA, IllustrationKey.VASO_AGUA
            ),
            correctItems = setOf(
                IllustrationKey.MANZANA, IllustrationKey.PAN,
                IllustrationKey.ZANAHORIA, IllustrationKey.VASO_AGUA
            ),
            successText = "El alimento entra y el sistema digestivo se pone en marcha.",
            retryText = "Sueltalo justo encima de Vita para que pueda comerlo."
        ),
        Challenge.Drag(
            activityId = "act_energia_arrastrar",
            items = listOf(
                IllustrationKey.PARTICULA_ENERGIA, IllustrationKey.DESECHO,
                IllustrationKey.NUTRIENTE, IllustrationKey.BURBUJA_OXIGENO
            ),
            correctItems = setOf(IllustrationKey.PARTICULA_ENERGIA, IllustrationKey.NUTRIENTE),
            successText = "La energia llega al musculo y Vita puede moverse.",
            retryText = "Los desechos no dan energia. Prueba con otra pieza."
        ),
        Challenge.Predict(
            activityId = "act_comer_predecir",
            question = "Marca los sistemas que reciben lo bueno del alimento.",
            options = Systems.ALL,
            correct = setOf(Systems.CIRCULATORIO, Systems.DIGESTIVO),
            explanation = "El digestivo lo prepara y el circulatorio lo reparte por el cuerpo."
        ),
        Challenge.Predict(
            activityId = "act_mover_predecir",
            question = "Que necesita un musculo para trabajar? Marcalo.",
            options = Systems.ALL,
            correct = setOf(Systems.CIRCULATORIO, Systems.RESPIRATORIO, Systems.MOVIMIENTO),
            explanation = "Necesita energia y oxigeno, y los recibe gracias a la sangre y a la respiracion."
        ),
        Challenge.Predict(
            activityId = "act_limpieza_predecir",
            question = "Quien se encarga de lo que el cuerpo no aprovecha?",
            options = Systems.ALL,
            correct = setOf(Systems.LIMPIEZA, Systems.CIRCULATORIO),
            explanation = "La sangre lo recoge y el sistema de limpieza lo saca fuera."
        ),
        Challenge.Compare(
            activityId = "act_mover_comparar",
            question = "En cual de las dos pruebas gasta Vita mas energia?",
            labelA = "Vita camina",
            labelB = "Vita corre",
            energyA = 0.35f,
            energyB = 0.85f,
            correctIsA = false,
            explanation = "Correr gasta mas energia, por eso el corazon late mas rapido y respiras mas."
        ),
        Challenge.Compare(
            activityId = "act_juntos_comparar",
            question = "En que manana aguanta Vita mas rato corriendo?",
            labelA = "Ha desayunado",
            labelB = "No ha desayunado",
            energyA = 0.90f,
            energyB = 0.30f,
            correctIsA = true,
            explanation = "El alimento aporta energia. Sin ella, los musculos se cansan antes."
        ),
        Challenge.Compare(
            activityId = "act_comparar_aire",
            question = "En que camara aguanta mas la simulacion?",
            labelA = "Camara con mas aire",
            labelB = "Camara con menos aire",
            energyA = 0.88f,
            energyB = 0.32f,
            correctIsA = true,
            explanation = "Con mas oxigeno disponible, el transporte llega mejor a todo el cuerpo."
        ),
        Challenge.Compare(
            activityId = "act_sentidos_comparar",
            question = "Cuando reacciona antes Vita a la hoja que cae?",
            labelA = "Vita atenta",
            labelB = "Vita distraida",
            energyA = 0.80f,
            energyB = 0.40f,
            correctIsA = true,
            explanation = "Si los sentidos avisan antes, el movimiento tambien llega antes."
        ),
        Challenge.Observe(
            activityId = "act_respirar_observar",
            instruction = "Toca a Vita cada vez que toma aire. Hazlo 5 veces.",
            target = 5,
            explanation = "Respirar es algo que ocurre una y otra vez, sin parar, todo el dia."
        ),
        Challenge.Observe(
            activityId = "act_oxigeno_observar",
            instruction = "Toca la burbuja en sus 4 paradas.",
            target = 4,
            explanation = "El oxigeno hace un recorrido con paradas: aire, pulmones, sangre y cuerpo."
        )
    )

    fun forActivity(activityId: String): Challenge? = all.firstOrNull {
        when (it) {
            is Challenge.Drag -> it.activityId == activityId
            is Challenge.Predict -> it.activityId == activityId
            is Challenge.Compare -> it.activityId == activityId
            is Challenge.Observe -> it.activityId == activityId
        }
    }

    fun size(): Int = all.size

    /** Evalua una prediccion: exige el conjunto exacto de sistemas. */
    fun evaluatePredict(challenge: Challenge.Predict, chosen: Set<String>): Boolean =
        chosen == challenge.correct

    /** Feedback util: nunca solo "incorrecto". */
    fun predictFeedback(challenge: Challenge.Predict, chosen: Set<String>): String {
        if (chosen.isEmpty()) return "Marca al menos un sistema para responder."
        if (chosen == challenge.correct) return challenge.explanation
        val missing = challenge.correct.count { it !in chosen }
        val extra = chosen.count { it !in challenge.correct }
        return when {
            missing > 0 && extra > 0 ->
                "Casi. Falta " + missing + " sistema(s) y sobra " + extra + "."
            missing > 0 -> "Vas bien, pero falta " + missing + " sistema(s) por marcar."
            else -> "Has marcado " + extra + " sistema(s) de mas. Quita el que menos encaje."
        }
    }
}
