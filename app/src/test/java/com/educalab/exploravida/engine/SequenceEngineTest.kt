package com.educalab.exploravida.engine

import com.educalab.exploravida.domain.engine.SequenceEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SequenceEngineTest {

    private val engine = SequenceEngine()
    private val expected = listOf("morder", "tragar", "mezclar", "repartir")

    @Test
    fun `el orden correcto se resuelve`() {
        val result = engine.validate(expected, expected)
        assertTrue(result.solved)
        assertEquals(4, result.correctCount)
        assertEquals(-1, result.firstErrorIndex)
    }

    @Test
    fun `una secuencia vacia no rompe el motor`() {
        val result = engine.validate(emptyList(), emptyList())
        assertFalse(result.solved)
        assertEquals(0, result.total)
    }

    @Test
    fun `faltan tarjetas por colocar`() {
        val result = engine.validate(expected, listOf("morder", "tragar"))
        assertFalse(result.solved)
        assertTrue(result.feedback.contains("Faltan"))
    }

    @Test
    fun `el feedback dice cuantas tarjetas estan bien`() {
        val result = engine.validate(expected, listOf("morder", "mezclar", "tragar", "repartir"))
        assertFalse(result.solved)
        assertEquals(2, result.correctCount)
        assertEquals(1, result.firstErrorIndex)
    }

    @Test
    fun `el feedback nunca es solo incorrecto`() {
        val result = engine.validate(expected, expected.reversed())
        assertTrue(result.feedback.length > 20)
        assertFalse(result.feedback.equals("Incorrecto", ignoreCase = true))
    }

    @Test
    fun `la pista senala el primer paso pendiente`() {
        val hint = engine.hint(expected, listOf("morder"))
        assertTrue(hint.contains("2"))
    }

    @Test
    fun `la mezcla determinista conserva todas las tarjetas`() {
        val shuffled = engine.shuffleDeterministic(expected, 42L)
        assertEquals(expected.size, shuffled.size)
        assertEquals(expected.toSet(), shuffled.toSet())
    }

    @Test
    fun `la misma semilla produce la misma mezcla`() {
        assertEquals(
            engine.shuffleDeterministic(expected, 7L),
            engine.shuffleDeterministic(expected, 7L)
        )
    }

    @Test
    fun `las estrellas premian resolver a la primera`() {
        assertEquals(3, engine.stars(1, true))
        assertTrue(engine.stars(3, true) < 3)
        assertEquals(0, engine.stars(5, false))
    }
}
