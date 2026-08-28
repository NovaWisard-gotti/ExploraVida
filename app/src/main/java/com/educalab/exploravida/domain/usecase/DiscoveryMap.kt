package com.educalab.exploravida.domain.usecase

import com.educalab.exploravida.domain.model.Systems

/**
 * Relaciona cada experiencia con los conceptos que el nino entiende al terminarla.
 * Vive en domain para poder probarse sin interfaz.
 */
object DiscoveryMap {

    private val byExperience: Map<String, List<String>> = mapOf(
        "exp_explorar" to listOf("ser_vivo", "sistema"),
        "exp_comer" to listOf("alimento", "digestion", "nutriente"),
        "exp_respirar" to listOf("respiracion", "oxigeno"),
        "exp_moverse" to listOf("movimiento", "energia"),
        "exp_viaje_energia" to listOf("energia", "circulacion", "recorrido"),
        "exp_viaje_oxigeno" to listOf("oxigeno", "circulacion", "recorrido"),
        "exp_conexiones" to listOf("conexion", "sistema"),
        "exp_secuencia" to listOf("recorrido", "cooperacion"),
        "exp_juntos" to listOf("cooperacion", "ser_vivo"),
        "exp_comparar" to listOf("energia", "cooperacion"),
        "exp_limpieza" to listOf("limpieza"),
        "exp_sentidos" to listOf("relacion", "ser_vivo")
    )

    private val bySystem: Map<String, String> = mapOf(
        Systems.DIGESTIVO to "digestion",
        Systems.RESPIRATORIO to "respiracion",
        Systems.CIRCULATORIO to "circulacion",
        Systems.MOVIMIENTO to "movimiento",
        Systems.LIMPIEZA to "limpieza",
        Systems.RELACION to "relacion"
    )

    fun conceptsOf(experienceId: String): List<String> = byExperience[experienceId].orEmpty()

    fun conceptOfSystem(systemId: String): String? = bySystem[systemId]

    fun coveredConcepts(): Set<String> =
        byExperience.values.flatten().toSet() + bySystem.values.toSet()
}
