package com.educalab.exploravida.seed

import com.educalab.exploravida.data.local.seed.SeedActivities
import com.educalab.exploravida.data.local.seed.SeedContent
import com.educalab.exploravida.domain.model.ActivityKind
import com.educalab.exploravida.domain.model.AnimationKey
import com.educalab.exploravida.domain.model.ExperienceKind
import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.domain.model.Systems
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** La app instalada debe sentirse completa: aqui se comprueba el contenido real. */
class SeedIntegrityTest {

    @Test
    fun `hay doce experiencias`() {
        assertEquals(12, SeedContent.experiences.size)
    }

    @Test
    fun `hay al menos quince actividades`() {
        assertTrue(SeedActivities.activities.size >= 15)
    }

    @Test
    fun `hay quince conceptos para el cuaderno`() {
        assertEquals(15, SeedActivities.concepts.size)
    }

    @Test
    fun `hay veinte situaciones distintas`() {
        val situations = SeedActivities.activities.map { it.situation }.filter { it.isNotBlank() }
        assertTrue(situations.size >= 20)
        assertEquals(situations.size, situations.toSet().size)
    }

    @Test
    fun `hay al menos veinticinco elementos ilustrados`() {
        assertTrue(SeedContent.elements.size >= 25)
    }

    @Test
    fun `no hay identificadores repetidos`() {
        assertEquals(SeedContent.experiences.size, SeedContent.experiences.map { it.id }.toSet().size)
        assertEquals(SeedContent.elements.size, SeedContent.elements.map { it.id }.toSet().size)
        assertEquals(SeedActivities.activities.size, SeedActivities.activities.map { it.id }.toSet().size)
        assertEquals(SeedActivities.badges.size, SeedActivities.badges.map { it.id }.toSet().size)
        assertEquals(SeedActivities.concepts.size, SeedActivities.concepts.map { it.key }.toSet().size)
    }

    @Test
    fun `el orden de las experiencias es unico`() {
        val orders = SeedContent.experiences.map { it.orderIndex }
        assertEquals(orders.size, orders.toSet().size)
    }

    @Test
    fun `las claves de enumeracion existen`() {
        SeedContent.experiences.forEach {
            ExperienceKind.valueOf(it.kind)
            SceneBackground.valueOf(it.backgroundKey)
        }
        SeedContent.steps.forEach {
            AnimationKey.valueOf(it.animationKey)
            IllustrationKey.valueOf(it.illustrationKey)
        }
        SeedContent.elements.forEach { IllustrationKey.valueOf(it.illustrationKey) }
        SeedActivities.activities.forEach { ActivityKind.valueOf(it.kind) }
    }

    @Test
    fun `las claves foraneas apuntan a datos reales`() {
        val experienceIds = SeedContent.experiences.map { it.id }.toSet()
        SeedContent.steps.forEach { assertTrue(it.experienceId in experienceIds) }
        SeedActivities.activities.forEach { assertTrue(it.experienceId in experienceIds) }

        val activityIds = SeedActivities.activities.map { it.id }.toSet()
        SeedActivities.sequences.forEach { assertTrue(it.activityId in activityIds) }
        SeedActivities.connectionChallenges.forEach { assertTrue(it.activityId in activityIds) }

        val sequenceIds = SeedActivities.sequences.map { it.id }.toSet()
        SeedActivities.sequenceItems.forEach { assertTrue(it.sequenceId in sequenceIds) }
    }

    @Test
    fun `los sistemas referenciados existen`() {
        SeedContent.elements.forEach { assertTrue(it.systemId in Systems.ALL) }
        SeedContent.connections.forEach {
            assertTrue(it.fromSystemId in Systems.ALL)
            assertTrue(it.toSystemId in Systems.ALL)
        }
        SeedContent.steps.mapNotNull { it.systemId }.forEach { assertTrue(it in Systems.ALL) }
    }

    @Test
    fun `las coordenadas de las zonas estan dentro del organismo`() {
        SeedContent.elements.forEach {
            assertTrue(it.x in 0f..1f)
            assertTrue(it.y in 0f..1f)
            assertTrue(it.radius > 0f && it.radius < 0.5f)
        }
    }

    @Test
    fun `cada secuencia tiene posiciones correlativas`() {
        SeedActivities.sequences.forEach { sequence ->
            val items = SeedActivities.sequenceItems.filter { it.sequenceId == sequence.id }
            assertTrue(items.size >= 3)
            val positions = items.map { it.correctPosition }.sorted()
            assertEquals((0 until items.size).toList(), positions)
        }
    }

    @Test
    fun `hay contenido abierto desde el principio`() {
        assertTrue(SeedContent.experiences.count { it.requiredXp == 0 } >= 3)
    }

    @Test
    fun `los textos son cortos y en espanol natural`() {
        SeedContent.steps.forEach {
            assertTrue("Paso demasiado largo: " + it.title, it.text.length <= 190)
            assertTrue(it.title.isNotBlank())
        }
    }

    @Test
    fun `menos de la mitad de las actividades son de eleccion`() {
        val choice = SeedActivities.activities.count {
            it.kind == ActivityKind.PREDECIR.name || it.kind == ActivityKind.COMPARAR.name
        }
        assertTrue(choice * 2 <= SeedActivities.activities.size)
    }

    @Test
    fun `cada experiencia tiene pasos`() {
        SeedContent.experiences.forEach { experience ->
            val steps = SeedContent.steps.filter { it.experienceId == experience.id }
            assertTrue("Sin pasos: " + experience.id, steps.size >= 3)
        }
    }

    @Test
    fun `hay veinte animaciones y diez fondos declarados`() {
        assertEquals(20, AnimationKey.values().size)
        assertEquals(10, SceneBackground.values().size)
        assertEquals(30, IllustrationKey.values().size)
    }
}
