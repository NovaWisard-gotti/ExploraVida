package com.educalab.exploravida.domain.engine

import com.educalab.exploravida.domain.model.ExperienceModel
import com.educalab.exploravida.domain.model.ModuleState
import com.educalab.exploravida.domain.model.ProgressStats

/**
 * Convierte acciones reales del nino en XP, nivel y contenido desbloqueado.
 * No hay niveles complicados: cinco escalones de explorador.
 */
object ProgressEngine {

    val LEVEL_THRESHOLDS = listOf(0, 60, 150, 280, 450)

    const val XP_STEP_VIEWED = 3
    const val XP_ELEMENT_EXPLORED = 4
    const val XP_ACTIVITY_OK = 15
    const val XP_ACTIVITY_PERFECT_BONUS = 5
    const val XP_EXPERIENCE_COMPLETED = 25
    const val XP_DISCOVERY = 8

    fun levelFor(xp: Int): Int {
        val safeXp = if (xp < 0) 0 else xp
        var level = 1
        for (index in LEVEL_THRESHOLDS.indices) {
            if (safeXp >= LEVEL_THRESHOLDS[index]) level = index + 1
        }
        return level
    }

    fun levelTitle(level: Int): String = when (level) {
        1 -> "Explorador novato"
        2 -> "Explorador curioso"
        3 -> "Explorador de sistemas"
        4 -> "Explorador experto"
        else -> "Maestro ExploraVida"
    }

    fun xpForNextLevel(xp: Int): Int? {
        val safeXp = if (xp < 0) 0 else xp
        return LEVEL_THRESHOLDS.firstOrNull { it > safeXp }
    }

    /** Avance dentro del nivel actual, entre 0 y 1. */
    fun levelProgress(xp: Int): Float {
        val safeXp = if (xp < 0) 0 else xp
        val level = levelFor(safeXp)
        val floor = LEVEL_THRESHOLDS[level - 1]
        val ceiling = LEVEL_THRESHOLDS.getOrNull(level) ?: return 1f
        val span = (ceiling - floor).toFloat()
        if (span <= 0f) return 1f
        return ((safeXp - floor) / span).coerceIn(0f, 1f)
    }

    fun isUnlocked(experience: ExperienceModel, xp: Int): Boolean = xp >= experience.requiredXp

    fun unlocked(all: List<ExperienceModel>, xp: Int): List<ExperienceModel> =
        all.filter { isUnlocked(it, xp) }.sortedBy { it.orderIndex }

    /** Siguiente experiencia sugerida: la primera desbloqueada sin completar. */
    fun nextExperience(all: List<ExperienceModel>, xp: Int, completed: Set<String>): ExperienceModel? =
        unlocked(all, xp).firstOrNull { it.id !in completed }

    fun stateOf(
        experience: ExperienceModel,
        xp: Int,
        completed: Set<String>,
        started: Set<String>,
        mastered: Set<String>
    ): ModuleState = when {
        experience.id in mastered -> ModuleState.DOMINADO
        experience.id in completed -> ModuleState.COMPLETADO
        !isUnlocked(experience, xp) -> ModuleState.BLOQUEADO
        experience.id in started -> ModuleState.INICIADO
        else -> ModuleState.DISPONIBLE
    }

    fun addXp(current: Int, amount: Int): Int {
        val safeCurrent = if (current < 0) 0 else current
        val safeAmount = if (amount < 0) 0 else amount
        return safeCurrent + safeAmount
    }

    fun globalProgress(stats: ProgressStats, totalExperiences: Int): Float {
        if (totalExperiences <= 0) return 0f
        return (stats.experiencesCompleted.toFloat() / totalExperiences).coerceIn(0f, 1f)
    }
}
