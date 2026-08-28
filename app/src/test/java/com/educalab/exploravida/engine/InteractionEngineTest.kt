package com.educalab.exploravida.engine

import com.educalab.exploravida.domain.engine.InteractionEngine
import com.educalab.exploravida.domain.model.InteractiveElementModel
import com.educalab.exploravida.domain.model.Systems
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class InteractionEngineTest {

    private val elements = listOf(
        InteractiveElementModel("boca", "Boca", Systems.DIGESTIVO, "Entra el alimento", 0.5f, 0.2f, 0.08f, "BOCA"),
        InteractiveElementModel("estomago", "Estomago", Systems.DIGESTIVO, "Se mezcla", 0.5f, 0.5f, 0.08f, "ESTOMAGO"),
        InteractiveElementModel("pulmon", "Pulmones", Systems.RESPIRATORIO, "Entra el aire", 0.3f, 0.35f, 0.08f, "PULMONES")
    )

    private fun engine() = InteractionEngine(elements)

    @Test
    fun `un toque valido se acepta la primera vez`() {
        val activation = engine().activate("boca", 1_000L)
        assertTrue(activation is InteractionEngine.Activation.Accepted)
        assertTrue((activation as InteractionEngine.Activation.Accepted).firstTime)
    }

    @Test
    fun `el doble toque rapido se ignora`() {
        val engine = engine()
        engine.activate("boca", 1_000L)
        val second = engine.activate("boca", 1_100L)
        assertTrue(second is InteractionEngine.Activation.Ignored)
    }

    @Test
    fun `el mismo elemento se puede repasar pasado el tiempo`() {
        val engine = engine()
        engine.activate("boca", 1_000L)
        val second = engine.activate("boca", 3_000L)
        assertTrue(second is InteractionEngine.Activation.Accepted)
        assertFalse((second as InteractionEngine.Activation.Accepted).firstTime)
    }

    @Test
    fun `un elemento inexistente devuelve desconocido`() {
        assertEquals(InteractionEngine.Activation.Unknown, engine().activate("no_existe", 1L))
    }

    @Test
    fun `se localiza el elemento por coordenadas`() {
        assertNotNull(engine().elementAt(0.5f, 0.21f))
        assertNull(engine().elementAt(0.95f, 0.95f))
    }

    @Test
    fun `un sistema esta completo cuando se tocan todas sus zonas`() {
        val engine = engine()
        assertFalse(engine.isSystemFullyExplored(Systems.DIGESTIVO))
        engine.activate("boca", 0L)
        engine.activate("estomago", 1_000L)
        assertTrue(engine.isSystemFullyExplored(Systems.DIGESTIVO))
    }

    @Test
    fun `los sistemas visitados se acumulan`() {
        val engine = engine()
        engine.activate("boca", 0L)
        engine.activate("pulmon", 1_000L)
        assertEquals(setOf(Systems.DIGESTIVO, Systems.RESPIRATORIO), engine.visitedSystems())
    }

    @Test
    fun `el progreso llega a uno al explorar todo`() {
        val engine = engine()
        elements.forEachIndexed { index, element ->
            engine.activate(element.id, index * 1_000L)
        }
        assertEquals(1f, engine.progress(), 0.001f)
    }

    @Test
    fun `restaurar recupera el avance guardado`() {
        val engine = engine()
        engine.restore(listOf("boca", "pulmon", "inexistente"))
        assertEquals(2, engine.activatedCount())
    }

    @Test
    fun `reiniciar deja el progreso a cero`() {
        val engine = engine()
        engine.activate("boca", 0L)
        engine.reset()
        assertEquals(0, engine.activatedCount())
        assertEquals(0f, engine.progress(), 0.001f)
    }

    @Test
    fun `una lista vacia de elementos no rompe el progreso`() {
        val empty = InteractionEngine(emptyList())
        assertEquals(0f, empty.progress(), 0.001f)
        assertEquals(InteractionEngine.Activation.Unknown, empty.activate("boca", 0L))
    }
}
