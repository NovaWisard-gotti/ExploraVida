package com.educalab.exploravida.progress

import com.educalab.exploravida.data.local.seed.SeedContent
import com.educalab.exploravida.domain.engine.ProgressEngine
import com.educalab.exploravida.domain.model.ExperienceKind
import com.educalab.exploravida.domain.model.ExperienceModel
import com.educalab.exploravida.domain.model.ModuleState
import com.educalab.exploravida.domain.model.ProgressStats
import com.educalab.exploravida.domain.model.SceneBackground
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressEngineTest {

    private fun experience(id: String, order: Int, requiredXp: Int) = ExperienceModel(
        id = id,
        title = id,
        subtitle = "",
        noraIntro = "",
        kind = ExperienceKind.RECORRIDO,
        orderIndex = order,
        requiredXp = requiredXp,
        background = SceneBackground.LABORATORIO,
        iconKey = "ic_flask"
    )

    private val all = listOf(
        experience("a", 0, 0),
        experience("b", 1, 60),
        experience("c", 2, 150)
    )

    @Test
    fun `una base nueva empieza en nivel uno`() {
        assertEquals(1, ProgressEngine.levelFor(0))
        assertEquals("Explorador novato", ProgressEngine.levelTitle(1))
    }

    @Test
    fun `el xp negativo no rompe el nivel`() {
        assertEquals(1, ProgressEngine.levelFor(-50))
        assertEquals(0f, ProgressEngine.levelProgress(-10), 0.001f)
    }

    @Test
    fun `el nivel sube al alcanzar el umbral`() {
        assertEquals(2, ProgressEngine.levelFor(60))
        assertEquals(3, ProgressEngine.levelFor(150))
        assertEquals(5, ProgressEngine.levelFor(450))
    }

    @Test
    fun `el nivel maximo se mantiene con xp muy alto`() {
        assertEquals(5, ProgressEngine.levelFor(99_999))
        assertEquals(1f, ProgressEngine.levelProgress(99_999), 0.001f)
        assertNull(ProgressEngine.xpForNextLevel(99_999))
    }

    @Test
    fun `sumar xp nunca resta`() {
        assertEquals(10, ProgressEngine.addXp(10, -5))
        assertEquals(0, ProgressEngine.addXp(-10, 0))
        assertEquals(25, ProgressEngine.addXp(10, 15))
    }

    @Test
    fun `solo se desbloquea lo que el xp permite`() {
        assertEquals(1, ProgressEngine.unlocked(all, 0).size)
        assertEquals(2, ProgressEngine.unlocked(all, 60).size)
        assertEquals(3, ProgressEngine.unlocked(all, 300).size)
    }

    @Test
    fun `hay contenido disponible desde el primer momento`() {
        val open = ProgressEngine.unlocked(SeedContent.experiences.map { it.toModelForTest() }, 0)
        assertTrue(open.size >= 3)
    }

    @Test
    fun `la siguiente experiencia salta las completadas`() {
        val next = ProgressEngine.nextExperience(all, 60, setOf("a"))
        assertEquals("b", next?.id)
    }

    @Test
    fun `sin experiencias pendientes no hay siguiente`() {
        assertNull(ProgressEngine.nextExperience(all, 500, setOf("a", "b", "c")))
    }

    @Test
    fun `los estados visuales reflejan el avance real`() {
        assertEquals(ModuleState.BLOQUEADO, ProgressEngine.stateOf(all[2], 0, emptySet(), emptySet(), emptySet()))
        assertEquals(ModuleState.DISPONIBLE, ProgressEngine.stateOf(all[0], 0, emptySet(), emptySet(), emptySet()))
        assertEquals(ModuleState.INICIADO, ProgressEngine.stateOf(all[0], 0, emptySet(), setOf("a"), emptySet()))
        assertEquals(ModuleState.COMPLETADO, ProgressEngine.stateOf(all[0], 0, setOf("a"), setOf("a"), emptySet()))
        assertEquals(ModuleState.DOMINADO, ProgressEngine.stateOf(all[0], 0, setOf("a"), setOf("a"), setOf("a")))
    }

    @Test
    fun `el progreso global no supera el cien por cien`() {
        val stats = ProgressStats(experiencesCompleted = 20)
        assertEquals(1f, ProgressEngine.globalProgress(stats, 12), 0.001f)
        assertEquals(0f, ProgressEngine.globalProgress(stats, 0), 0.001f)
    }

    @Test
    fun `la lista vacia de experiencias no rompe nada`() {
        assertTrue(ProgressEngine.unlocked(emptyList(), 100).isEmpty())
        assertNull(ProgressEngine.nextExperience(emptyList(), 100, emptySet()))
    }

    private fun com.educalab.exploravida.data.local.entity.LearningExperienceEntity.toModelForTest() =
        ExperienceModel(
            id = id,
            title = title,
            subtitle = subtitle,
            noraIntro = noraIntro,
            kind = ExperienceKind.valueOf(kind),
            orderIndex = orderIndex,
            requiredXp = requiredXp,
            background = SceneBackground.valueOf(backgroundKey),
            iconKey = iconKey
        )

    @Test
    fun `las constantes de xp son positivas`() {
        assertTrue(ProgressEngine.XP_ACTIVITY_OK > 0)
        assertTrue(ProgressEngine.XP_EXPERIENCE_COMPLETED > ProgressEngine.XP_STEP_VIEWED)
        assertFalse(ProgressEngine.LEVEL_THRESHOLDS.isEmpty())
    }
}
