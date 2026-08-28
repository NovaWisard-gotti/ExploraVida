package com.educalab.exploravida.engine

import com.educalab.exploravida.data.local.seed.SeedContent
import com.educalab.exploravida.domain.engine.BodySystemEngine
import com.educalab.exploravida.domain.model.BodyAction
import com.educalab.exploravida.domain.model.LivingSystemModel
import com.educalab.exploravida.domain.model.SystemLink
import com.educalab.exploravida.domain.model.Systems
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BodySystemEngineTest {

    private lateinit var engine: BodySystemEngine

    @Before
    fun setUp() {
        val systems = SeedContent.systems.map {
            LivingSystemModel(it.id, it.name, it.shortDescription, it.colorHex, it.iconKey, it.orderIndex)
        }
        val links = SeedContent.connections.map {
            SystemLink(it.fromSystemId, it.toSystemId, it.explanation)
        }
        engine = BodySystemEngine(systems, links)
    }

    @Test
    fun `el laboratorio tiene los seis sistemas`() {
        assertEquals(6, engine.allSystems().size)
        Systems.ALL.forEach { assertTrue(engine.exists(it)) }
    }

    @Test
    fun `un sistema inexistente no rompe el motor`() {
        assertFalse(engine.exists("sistema_fantasma"))
        assertNull(engine.system("sistema_fantasma"))
        assertTrue(engine.neighbors("sistema_fantasma").isEmpty())
    }

    @Test
    fun `comer necesita mas de un sistema`() {
        val involved = engine.systemsInvolved(BodyAction.COMER)
        assertTrue(involved.size >= 2)
        assertTrue(engine.isIntegrated(BodyAction.COMER))
    }

    @Test
    fun `correr integra respiracion y circulacion`() {
        val involved = engine.systemsInvolved(BodyAction.CORRER)
        assertTrue(involved.contains(Systems.RESPIRATORIO))
        assertTrue(involved.contains(Systems.CIRCULATORIO))
        assertTrue(involved.contains(Systems.MOVIMIENTO))
    }

    @Test
    fun `existe camino entre digestivo y movimiento`() {
        val path = engine.path(Systems.DIGESTIVO, Systems.MOVIMIENTO)
        assertTrue(path.size >= 2)
        assertEquals(Systems.DIGESTIVO, path.first())
        assertEquals(Systems.MOVIMIENTO, path.last())
    }

    @Test
    fun `el camino hacia un sistema inexistente esta vacio`() {
        assertTrue(engine.path(Systems.DIGESTIVO, "no_existe").isEmpty())
    }

    @Test
    fun `el camino de un sistema a si mismo tiene un solo paso`() {
        assertEquals(listOf(Systems.DIGESTIVO), engine.path(Systems.DIGESTIVO, Systems.DIGESTIVO))
    }

    @Test
    fun `una conexion valida tiene explicacion`() {
        val link = SeedContent.connections.first()
        assertTrue(engine.isValidLink(link.fromSystemId, link.toSystemId))
        assertNotNull(engine.explanationFor(link.fromSystemId, link.toSystemId))
    }

    @Test
    fun `la cobertura crece al descubrir conexiones`() {
        val none = engine.coverage(emptySet())
        val one = engine.coverage(setOf(SeedContent.connections.first().let { it.fromSystemId to it.toSystemId }))
        assertEquals(0f, none, 0.001f)
        assertTrue(one > none)
    }

    @Test
    fun `cada sistema participa en al menos una conexion`() {
        Systems.ALL.forEach { system ->
            assertTrue("Sin conexiones: " + system, engine.linksOf(system).isNotEmpty())
        }
    }
}
