package com.educalab.exploravida.domain.model

/**
 * Claves estables del universo ExploraVida.
 * Se usan tanto en Room (como texto) como en la capa de dibujo (Compose Canvas).
 */
object Systems {
    const val DIGESTIVO = "digestivo"
    const val RESPIRATORIO = "respiratorio"
    const val CIRCULATORIO = "circulatorio"
    const val MOVIMIENTO = "movimiento"
    const val LIMPIEZA = "limpieza"
    const val RELACION = "relacion"

    val ALL = listOf(DIGESTIVO, RESPIRATORIO, CIRCULATORIO, MOVIMIENTO, LIMPIEZA, RELACION)
}

/** Acciones que Vita puede realizar y que activan varios sistemas a la vez. */
enum class BodyAction {
    COMER, BEBER, RESPIRAR, MOVERSE, CORRER, DESCANSAR, ELIMINAR, PERCIBIR, COMER_Y_CORRER
}

/** Tipo de experiencia educativa. Define que pantalla la ejecuta. */
enum class ExperienceKind {
    RECORRIDO,      // el nino sigue una particula por el cuerpo
    EXPLORACION,    // el nino toca zonas del organismo
    SECUENCIA,      // el nino ordena eventos
    CONEXION,       // el nino une sistemas
    COMPARACION,    // el nino compara dos escenarios
    HISTORIA        // varias acciones encadenadas
}

/** Tipo de actividad interactiva. Ninguna es de opcion multiple pura. */
enum class ActivityKind {
    ARRASTRAR, ORDENAR, CONECTAR, COMPARAR, PREDECIR, OBSERVAR
}

/** Estado visual de un modulo. Nunca se expresa solo con color. */
enum class ModuleState {
    BLOQUEADO, DISPONIBLE, INICIADO, COMPLETADO, DOMINADO
}

/** 20 animaciones educativas. Cada una explica algo, no decora. */
enum class AnimationKey {
    ALIMENTO_VIAJA, MASTICAR, MEZCLA_ESTOMAGO, LIBERAR_NUTRIENTES, FLUJO_SANGRE,
    AIRE_ENTRA, AIRE_SALE, BURBUJA_OXIGENO, LATIDO, BOMBEO_MUSCULO,
    CHISPA_ENERGIA, FILTRO_LIMPIEZA, SISTEMA_ILUMINA, FLECHA_CONEXION, CARRERA,
    SALTO, ONDA_SENTIDO, GOTA_AGUA, ZOOM_CELULA, CELEBRACION
}

/** 10 fondos ilustrados dibujados con Compose Canvas. */
enum class SceneBackground {
    LABORATORIO, LABORATORIO_NOCHE, PRADERA, CIELO, TORRENTE,
    CUEVA_ESTOMAGO, CAMARA_AIRE, CAMPO_MUSCULO, PAPEL_CUADERNO, SALA_INSIGNIAS
}

/** 30 ilustraciones reutilizables del mismo universo grafico. */
enum class IllustrationKey {
    VITA_TRANQUILA, VITA_COMIENDO, VITA_RESPIRANDO, VITA_CORRIENDO, VITA_CURIOSA,
    NORA, MANZANA, PAN, ZANAHORIA, VASO_AGUA,
    LUPA, MATRAZ, MICROSCOPIO, CUADERNO, PROBETA,
    PARTICULA_ENERGIA, BURBUJA_OXIGENO, NUTRIENTE, DESECHO, GOTA,
    BOCA, TUBO, ESTOMAGO, INTESTINO, NARIZ,
    PULMONES, CORAZON, CAMINOS, MUSCULO, FILTRO
}

data class LivingSystemModel(
    val id: String,
    val name: String,
    val shortDescription: String,
    val colorHex: String,
    val iconKey: String,
    val orderIndex: Int
)

data class SystemLink(
    val fromSystemId: String,
    val toSystemId: String,
    val explanation: String
)

data class InteractiveElementModel(
    val id: String,
    val name: String,
    val systemId: String,
    val description: String,
    val x: Float,
    val y: Float,
    val radius: Float,
    val illustrationKey: String
)

data class ExperienceModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val noraIntro: String,
    val kind: ExperienceKind,
    val orderIndex: Int,
    val requiredXp: Int,
    val background: SceneBackground,
    val iconKey: String
)

data class StepModel(
    val id: Long,
    val experienceId: String,
    val orderIndex: Int,
    val title: String,
    val text: String,
    val systemId: String?,
    val animation: AnimationKey,
    val illustrationKey: String
)

data class BadgeModel(
    val id: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val ruleKey: String,
    val threshold: Int
)

/** Fotografia del avance del nino. La calculan los motores, no la UI. */
data class ProgressStats(
    val xp: Int = 0,
    val experiencesCompleted: Int = 0,
    val activitiesCompleted: Int = 0,
    val perfectActivities: Int = 0,
    val journeysCompleted: Int = 0,
    val sequencesSolved: Int = 0,
    val connectionsMade: Int = 0,
    val elementsExplored: Int = 0,
    val discoveries: Int = 0,
    val systemsVisited: Set<String> = emptySet()
)
