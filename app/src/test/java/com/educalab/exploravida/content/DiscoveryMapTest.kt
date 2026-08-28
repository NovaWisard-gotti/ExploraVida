package com.educalab.exploravida.content

import com.educalab.exploravida.data.local.seed.SeedActivities
import com.educalab.exploravida.data.local.seed.SeedContent
import com.educalab.exploravida.domain.model.Systems
import com.educalab.exploravida.domain.usecase.DiscoveryMap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DiscoveryMapTest {

    @Test
    fun `cada experiencia entrega al menos un concepto`() {
        SeedContent.experiences.forEach {
            assertTrue("Sin conceptos: " + it.id, DiscoveryMap.conceptsOf(it.id).isNotEmpty())
        }
    }

    @Test
    fun `una experiencia inexistente no entrega conceptos`() {
        assertTrue(DiscoveryMap.conceptsOf("exp_inventada").isEmpty())
    }

    @Test
    fun `cada sistema tiene su concepto`() {
        Systems.ALL.forEach { assertNotNull(DiscoveryMap.conceptOfSystem(it)) }
        assertNull(DiscoveryMap.conceptOfSystem("fantasma"))
    }

    @Test
    fun `todos los conceptos referenciados existen en el cuaderno`() {
        val known = SeedActivities.concepts.map { it.key }.toSet()
        DiscoveryMap.coveredConcepts().forEach {
            assertTrue("Concepto sin pagina: " + it, it in known)
        }
    }

    @Test
    fun `las quince paginas del cuaderno se pueden desbloquear`() {
        val reachable = DiscoveryMap.coveredConcepts()
        val missing = SeedActivities.concepts.map { it.key }.filter { it !in reachable }
        assertEquals("Paginas inalcanzables: " + missing, 0, missing.size)
    }

    @Test
    fun `los textos del cuaderno son breves`() {
        SeedActivities.concepts.forEach {
            assertTrue(it.text.length in 20..170)
            assertTrue(it.title.isNotBlank())
        }
    }
}
