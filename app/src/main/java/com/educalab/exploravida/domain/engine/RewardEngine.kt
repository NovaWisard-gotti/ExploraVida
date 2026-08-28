package com.educalab.exploravida.domain.engine

import com.educalab.exploravida.domain.model.BadgeModel
import com.educalab.exploravida.domain.model.ProgressStats
import com.educalab.exploravida.domain.model.Systems

/**
 * Decide que insignias merece el nino. Cada insignia responde a una accion
 * real registrada en la base de datos, nunca al simple paso del tiempo.
 */
class RewardEngine(private val badges: List<BadgeModel>) {

    object Rules {
        const val PRIMER_DESCUBRIMIENTO = "PRIMER_DESCUBRIMIENTO"
        const val EXPERIENCIAS = "EXPERIENCIAS"
        const val ACTIVIDADES = "ACTIVIDADES"
        const val RECORRIDOS = "RECORRIDOS"
        const val CONEXIONES = "CONEXIONES"
        const val SECUENCIAS = "SECUENCIAS"
        const val ELEMENTOS = "ELEMENTOS"
        const val DESCUBRIMIENTOS = "DESCUBRIMIENTOS"
        const val XP = "XP"
        const val TODOS_LOS_SISTEMAS = "TODOS_LOS_SISTEMAS"
        const val PERFECTAS = "PERFECTAS"
        const val MAESTRO = "MAESTRO"
    }

    fun isEarned(badge: BadgeModel, stats: ProgressStats): Boolean = when (badge.ruleKey) {
        Rules.PRIMER_DESCUBRIMIENTO -> stats.discoveries >= 1
        Rules.EXPERIENCIAS -> stats.experiencesCompleted >= badge.threshold
        Rules.ACTIVIDADES -> stats.activitiesCompleted >= badge.threshold
        Rules.RECORRIDOS -> stats.journeysCompleted >= badge.threshold
        Rules.CONEXIONES -> stats.connectionsMade >= badge.threshold
        Rules.SECUENCIAS -> stats.sequencesSolved >= badge.threshold
        Rules.ELEMENTOS -> stats.elementsExplored >= badge.threshold
        Rules.DESCUBRIMIENTOS -> stats.discoveries >= badge.threshold
        Rules.XP -> stats.xp >= badge.threshold
        Rules.TODOS_LOS_SISTEMAS -> stats.systemsVisited.containsAll(Systems.ALL)
        Rules.PERFECTAS -> stats.perfectActivities >= badge.threshold
        Rules.MAESTRO -> badges.filter { it.ruleKey != Rules.MAESTRO }.all { isEarned(it, stats) }
        else -> false
    }

    /** Insignias nuevas: las que se cumplen y todavia no estaban entregadas. */
    fun newlyEarned(stats: ProgressStats, alreadyEarned: Set<String>): List<BadgeModel> =
        badges.filter { it.id !in alreadyEarned && isEarned(it, stats) }

    fun earnedCount(stats: ProgressStats): Int = badges.count { isEarned(it, stats) }

    fun total(): Int = badges.size

    /** Cuanto falta para la siguiente insignia, en tanto por uno. */
    fun progressTowards(badge: BadgeModel, stats: ProgressStats): Float {
        if (badge.threshold <= 0) return if (isEarned(badge, stats)) 1f else 0f
        val current = when (badge.ruleKey) {
            Rules.EXPERIENCIAS -> stats.experiencesCompleted
            Rules.ACTIVIDADES -> stats.activitiesCompleted
            Rules.RECORRIDOS -> stats.journeysCompleted
            Rules.CONEXIONES -> stats.connectionsMade
            Rules.SECUENCIAS -> stats.sequencesSolved
            Rules.ELEMENTOS -> stats.elementsExplored
            Rules.DESCUBRIMIENTOS, Rules.PRIMER_DESCUBRIMIENTO -> stats.discoveries
            Rules.XP -> stats.xp
            Rules.PERFECTAS -> stats.perfectActivities
            Rules.TODOS_LOS_SISTEMAS -> stats.systemsVisited.size
            else -> if (isEarned(badge, stats)) badge.threshold else 0
        }
        return (current.toFloat() / badge.threshold).coerceIn(0f, 1f)
    }
}
