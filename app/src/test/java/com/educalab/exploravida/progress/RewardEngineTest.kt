package com.educalab.exploravida.progress

import com.educalab.exploravida.data.local.seed.SeedActivities
import com.educalab.exploravida.domain.engine.RewardEngine
import com.educalab.exploravida.domain.model.BadgeModel
import com.educalab.exploravida.domain.model.ProgressStats
import com.educalab.exploravida.domain.model.Systems
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RewardEngineTest {

    private val badges = SeedActivities.badges.map {
        BadgeModel(it.id, it.name, it.description, it.iconKey, it.ruleKey, it.threshold)
    }
    private val engine = RewardEngine(badges)

    @Test
    fun `hay doce insignias`() {
        assertEquals(12, badges.size)
        assertEquals(12, engine.total())
    }

    @Test
    fun `sin actividad no se gana ninguna insignia`() {
        assertEquals(0, engine.earnedCount(ProgressStats()))
    }

    @Test
    fun `el primer descubrimiento entrega insignia`() {
        val earned = engine.newlyEarned(ProgressStats(discoveries = 1), emptySet())
        assertTrue(earned.any { it.ruleKey == RewardEngine.Rules.PRIMER_DESCUBRIMIENTO })
    }

    @Test
    fun `una insignia ya entregada no se repite`() {
        val stats = ProgressStats(discoveries = 1)
        val first = engine.newlyEarned(stats, emptySet())
        val second = engine.newlyEarned(stats, first.map { it.id }.toSet())
        assertTrue(second.none { badge -> badge.id in first.map { it.id } })
    }

    @Test
    fun `visitar todos los sistemas entrega la insignia de aventurero`() {
        val stats = ProgressStats(systemsVisited = Systems.ALL.toSet())
        val earned = engine.newlyEarned(stats, emptySet())
        assertTrue(earned.any { it.ruleKey == RewardEngine.Rules.TODOS_LOS_SISTEMAS })
    }

    @Test
    fun `visitar casi todos los sistemas no basta`() {
        val stats = ProgressStats(systemsVisited = Systems.ALL.dropLast(1).toSet())
        assertFalse(
            engine.newlyEarned(stats, emptySet())
                .any { it.ruleKey == RewardEngine.Rules.TODOS_LOS_SISTEMAS }
        )
    }

    @Test
    fun `la insignia de maestro exige todas las demas`() {
        val maestro = badges.first { it.ruleKey == RewardEngine.Rules.MAESTRO }
        assertFalse(engine.isEarned(maestro, ProgressStats(discoveries = 1)))
        val perfectStats = ProgressStats(
            xp = 5_000,
            experiencesCompleted = 50,
            activitiesCompleted = 50,
            perfectActivities = 50,
            journeysCompleted = 50,
            sequencesSolved = 50,
            connectionsMade = 50,
            elementsExplored = 50,
            discoveries = 50,
            systemsVisited = Systems.ALL.toSet()
        )
        assertTrue(engine.isEarned(maestro, perfectStats))
    }

    @Test
    fun `el progreso hacia una insignia crece con el avance`() {
        val badge = badges.first { it.ruleKey == RewardEngine.Rules.EXPERIENCIAS }
        val little = engine.progressTowards(badge, ProgressStats(experiencesCompleted = 1))
        val more = engine.progressTowards(badge, ProgressStats(experiencesCompleted = badge.threshold))
        assertTrue(more > little)
        assertEquals(1f, more, 0.001f)
    }

    @Test
    fun `el progreso nunca es negativo ni mayor que uno`() {
        badges.forEach { badge ->
            val value = engine.progressTowards(badge, ProgressStats(experiencesCompleted = 999))
            assertTrue(value in 0f..1f)
        }
    }

    @Test
    fun `una regla desconocida no entrega insignia`() {
        val fake = BadgeModel("x", "X", "", "ic_badge", "REGLA_INVENTADA", 1)
        assertFalse(RewardEngine(listOf(fake)).isEarned(fake, ProgressStats(xp = 9_999)))
    }

    @Test
    fun `sin insignias configuradas el motor no falla`() {
        val empty = RewardEngine(emptyList())
        assertEquals(0, empty.total())
        assertTrue(empty.newlyEarned(ProgressStats(xp = 100), emptySet()).isEmpty())
    }
}
