package com.educalab.exploravida.content

import com.educalab.exploravida.data.local.converters.Converters
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** La persistencia guarda listas como texto: aqui se prueban los casos limite. */
class ConvertersTest {

    private val converters = Converters()

    @Test
    fun `una lista vuelve igual tras guardarla`() {
        val original = listOf("digestivo", "respiratorio", "circulatorio")
        val stored = converters.fromStringList(original)
        assertEquals(original, converters.toStringList(stored))
    }

    @Test
    fun `una lista vacia se guarda como texto vacio`() {
        assertEquals("", converters.fromStringList(emptyList()))
        assertTrue(converters.toStringList("").isEmpty())
    }

    @Test
    fun `un valor nulo no rompe la conversion`() {
        assertEquals("", converters.fromStringList(null))
        assertTrue(converters.toStringList(null).isEmpty())
    }

    @Test
    fun `los elementos en blanco se descartan`() {
        assertEquals(listOf("uno"), converters.toStringList("uno||"))
        assertEquals("uno", converters.fromStringList(listOf("uno", "", "  ".trim())))
    }

    @Test
    fun `un texto largo se conserva completo`() {
        val many = (1..200).map { "clave" + it }
        assertEquals(many, converters.toStringList(converters.fromStringList(many)))
    }
}
