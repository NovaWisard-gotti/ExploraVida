package com.educalab.exploravida.seed

import com.educalab.exploravida.data.local.seed.SeedActivities
import com.educalab.exploravida.domain.model.ActivityKind
import com.educalab.exploravida.domain.model.Systems
import com.educalab.exploravida.domain.usecase.Challenge
import com.educalab.exploravida.domain.usecase.ChallengeCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ChallengeCatalogTest {

    @Test
    fun `cada actividad de arrastrar predecir comparar u observar tiene reto`() {
        val kinds = setOf(
            ActivityKind.ARRASTRAR.name,
            ActivityKind.PREDECIR.name,
            ActivityKind.COMPARAR.name,
            ActivityKind.OBSERVAR.name
        )
        SeedActivities.activities.filter { it.kind in kinds }.forEach {
            assertNotNull("Falta reto para " + it.id, ChallengeCatalog.forActivity(it.id))
        }
    }

    @Test
    fun `una actividad inexistente no tiene reto`() {
        assertNull(ChallengeCatalog.forActivity("act_inventada"))
    }

    @Test
    fun `los retos de prediccion usan sistemas reales`() {
        ChallengeCatalog.forActivity("act_comer_predecir").let { challenge ->
            val predict = challenge as Challenge.Predict
            predict.correct.forEach { assertTrue(it in Systems.ALL) }
            assertTrue(predict.correct.isNotEmpty())
        }
    }

    @Test
    fun `la prediccion exacta se acepta`() {
        val predict = ChallengeCatalog.forActivity("act_mover_predecir") as Challenge.Predict
        assertTrue(ChallengeCatalog.evaluatePredict(predict, predict.correct))
    }

    @Test
    fun `sobrar un sistema invalida la prediccion`() {
        val predict = ChallengeCatalog.forActivity("act_mover_predecir") as Challenge.Predict
        val extra = predict.correct + Systems.ALL.first { it !in predict.correct }
        assertFalse(ChallengeCatalog.evaluatePredict(predict, extra))
        assertTrue(ChallengeCatalog.predictFeedback(predict, extra).contains("mas"))
    }

    @Test
    fun `sin marcar nada se pide marcar algo`() {
        val predict = ChallengeCatalog.forActivity("act_limpieza_predecir") as Challenge.Predict
        assertTrue(ChallengeCatalog.predictFeedback(predict, emptySet()).contains("Marca"))
    }

    @Test
    fun `el feedback correcto explica el porque`() {
        val predict = ChallengeCatalog.forActivity("act_comer_predecir") as Challenge.Predict
        assertEquals(predict.explanation, ChallengeCatalog.predictFeedback(predict, predict.correct))
        assertTrue(predict.explanation.length > 25)
    }

    @Test
    fun `las comparaciones tienen energias distintas`() {
        listOf("act_mover_comparar", "act_juntos_comparar", "act_comparar_aire", "act_sentidos_comparar")
            .forEach { id ->
                val compare = ChallengeCatalog.forActivity(id) as Challenge.Compare
                assertTrue(compare.energyA != compare.energyB)
                assertEquals(id, compare.correctIsA, compare.energyA > compare.energyB)
                assertTrue(compare.explanation.length > 25)
            }
    }

    @Test
    fun `las observaciones piden contar mas de una vez`() {
        listOf("act_respirar_observar", "act_oxigeno_observar").forEach { id ->
            val observe = ChallengeCatalog.forActivity(id) as Challenge.Observe
            assertTrue(observe.target >= 3)
        }
    }

    @Test
    fun `los retos de arrastrar tienen piezas correctas e incorrectas o todas validas`() {
        val drag = ChallengeCatalog.forActivity("act_energia_arrastrar") as Challenge.Drag
        assertTrue(drag.items.containsAll(drag.correctItems))
        assertTrue(drag.correctItems.size < drag.items.size)
        assertTrue(drag.retryText.isNotBlank())
    }

    @Test
    fun `el catalogo cubre once retos`() {
        assertEquals(11, ChallengeCatalog.size())
    }
}
