package com.educalab.exploravida.engine

import com.educalab.exploravida.data.local.seed.SeedContent
import com.educalab.exploravida.domain.engine.BodySystemEngine
import com.educalab.exploravida.domain.engine.ConnectionEngine
import com.educalab.exploravida.domain.model.LivingSystemModel
import com.educalab.exploravida.domain.model.SystemLink
import com.educalab.exploravida.domain.model.Systems
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ConnectionEngineTest {

    private lateinit var body: BodySystemEngine
    private lateinit var engine: ConnectionEngine

    @Before
    fun setUp() {
        body = BodySystemEngine(
            SeedContent.systems.map {
                LivingSystemModel(it.id, it.name, it.shortDescription, it.colorHex, it.iconKey, it.orderIndex)
            },
            SeedContent.connections.map { SystemLink(it.fromSystemId, it.toSystemId, it.explanation) }
        )
        engine = ConnectionEngine(body)
    }

    private fun firstValid(): Pair<String, String> =
        SeedContent.connections.first().let { it.fromSystemId to it.toSystemId }

    @Test
    fun `una conexion real se acepta`() {
        val (from, to) = firstValid()
        val result = engine.connect(from, to)
        assertEquals(ConnectionEngine.Status.VALIDA, result.status)
        assertEquals(1, result.madeCount)
    }

    @Test
    fun `la conexion duplicada se detecta`() {
        val (from, to) = firstValid()
        engine.connect(from, to)
        val second = engine.connect(from, to)
        assertEquals(ConnectionEngine.Status.DUPLICADA, second.status)
        assertEquals(1, engine.madeConnections().size)
    }

    @Test
    fun `un sistema no se conecta consigo mismo`() {
        val result = engine.connect(Systems.DIGESTIVO, Systems.DIGESTIVO)
        assertEquals(ConnectionEngine.Status.MISMO_SISTEMA, result.status)
    }

    @Test
    fun `un sistema desconocido devuelve estado desconocido`() {
        val result = engine.connect("fantasma", Systems.DIGESTIVO)
        assertEquals(ConnectionEngine.Status.DESCONOCIDA, result.status)
    }

    @Test
    fun `toda respuesta trae explicacion educativa`() {
        val result = engine.connect(Systems.DIGESTIVO, Systems.DIGESTIVO)
        assertTrue(result.explanation.length > 15)
    }

    @Test
    fun `la cobertura llega al maximo al unir todas las parejas`() {
        SeedContent.connections.forEach { engine.connect(it.fromSystemId, it.toSystemId) }
        assertEquals(1f, engine.coverage(), 0.001f)
        assertTrue(engine.isComplete())
    }

    @Test
    fun `reiniciar borra las conexiones`() {
        val (from, to) = firstValid()
        engine.connect(from, to)
        engine.reset()
        assertTrue(engine.madeConnections().isEmpty())
        assertFalse(engine.isComplete())
    }

    @Test
    fun `restaurar recupera conexiones guardadas`() {
        engine.restore(listOf(firstValid()))
        assertEquals(1, engine.madeConnections().size)
    }
}
