package com.educalab.exploravida.ui.activities

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.domain.engine.ConnectionEngine
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.LabButton
import com.educalab.exploravida.ui.components.NoraBubble
import com.educalab.exploravida.ui.components.ProgressTube
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.components.drawIllustrationIn
import com.educalab.exploravida.ui.theme.LabColors
import com.educalab.exploravida.util.LocalFeedback
import kotlin.math.hypot

/**
 * Actividad CONECTAR: los seis sistemas se colocan en circulo y el nino
 * arrastra un cable de uno a otro. El motor decide si la ayuda existe.
 */
@Composable
fun ConnectionScreen(
    activityId: String,
    viewModel: ConnectionViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val feedback = LocalFeedback.current
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(activityId) { viewModel.load(activityId) }

    var dragFrom by remember { mutableStateOf<String?>(null) }
    var dragPoint by remember { mutableStateOf(Offset.Zero) }
    val nodes = remember { mutableMapOf<String, Offset>() }

    Box(Modifier.fillMaxSize()) {
        SceneBackdrop(SceneBackground.LABORATORIO_NOCHE, Modifier.fillMaxSize())
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            ActivityHeader(state.title, state.situation, if (state.solved) 3 else 0, onBack, Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Text(state.prompt, style = MaterialTheme.typography.titleMedium, color = LabColors.Paper)
            Spacer(Modifier.height(6.dp))
            ProgressTube(
                if (state.total == 0) 0f else state.made.size.toFloat() / state.total,
                Modifier.fillMaxWidth(),
                tint = LabColors.Teal
            )
            Spacer(Modifier.height(8.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(state.systems.size) {
                        detectDragGestures(
                            onDragStart = { start ->
                                dragFrom = nearestNode(nodes, start)
                                dragPoint = start
                            },
                            onDrag = { change, _ ->
                                dragPoint = change.position
                                change.consume()
                            },
                            onDragEnd = {
                                val target = nearestNode(nodes, dragPoint)
                                val origin = dragFrom
                                if (origin != null && target != null) {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    feedback.tapSound()
                                    viewModel.connect(origin, target)
                                }
                                dragFrom = null
                            },
                            onDragCancel = { dragFrom = null }
                        )
                    }
                    .pointerInput(state.systems.size) {
                        detectTapGestures { tap ->
                            nearestNode(nodes, tap)?.let {
                                feedback.tapSound()
                                viewModel.select(it)
                            }
                        }
                    }
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val radius = minOf(size.width, size.height) * 0.36f
                    val count = state.systems.size.coerceAtLeast(1)

                    state.systems.forEachIndexed { index, system ->
                        val angle = (Math.PI * 2 / count) * index - Math.PI / 2
                        nodes[system.id] = Offset(
                            cx + (radius * Math.cos(angle)).toFloat(),
                            cy + (radius * Math.sin(angle)).toFloat()
                        )
                    }

                    // cables ya descubiertos
                    state.made.forEach { (from, to) ->
                        val a = nodes[from]
                        val b = nodes[to]
                        if (a != null && b != null) {
                            drawLine(
                                color = LabColors.Lime.copy(alpha = 0.85f),
                                start = a,
                                end = b,
                                strokeWidth = 7f
                            )
                        }
                    }

                    // cable que se esta arrastrando
                    val origin = dragFrom?.let { nodes[it] }
                    if (origin != null) {
                        drawLine(LabColors.Amber, origin, dragPoint, strokeWidth = 6f)
                    }

                    // nodos
                    state.systems.forEach { system ->
                        val center = nodes[system.id] ?: return@forEach
                        val tint = LabColors.ofSystem(system.id)
                        val selected = state.selected == system.id
                        drawCircle(
                            color = tint.copy(alpha = if (selected) 0.45f else 0.22f),
                            radius = if (selected) 52f else 44f,
                            center = center
                        )
                        drawCircle(
                            color = tint,
                            radius = if (selected) 52f else 44f,
                            center = center,
                            style = Stroke(width = 4f)
                        )
                        val box = 46f
                        drawIllustrationIn(
                            iconOf(system.iconKey),
                            Offset(center.x - box / 2f, center.y - box / 2f),
                            box
                        )
                    }
                }

                Column(Modifier.fillMaxWidth().padding(top = 4.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        state.systems.take(3).forEach {
                            Text(
                                it.name.removePrefix("Sistema "),
                                style = MaterialTheme.typography.labelSmall,
                                color = LabColors.ofSystem(it.id)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            GlassPanel(Modifier.fillMaxWidth(), tint = tintOf(state.lastStatus)) {
                Column {
                    Text(
                        state.feedback,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabColors.Sand
                    )
                    Text(
                        "Conexiones descubiertas: " + state.made.size + " de " + state.total,
                        style = MaterialTheme.typography.labelSmall,
                        color = LabColors.Teal
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            if (state.solved) {
                LabButton("Volver al laboratorio", onBack, Modifier.fillMaxWidth(), tint = LabColors.Lime)
            } else {
                NoraBubble(
                    "Arrastra un cable de un sistema a otro. Tambien puedes tocarlos uno detras de otro.",
                    Modifier.fillMaxWidth(),
                    compact = true
                )
            }
        }
    }
}

private fun tintOf(status: ConnectionEngine.Status?): Color = when (status) {
    ConnectionEngine.Status.VALIDA -> LabColors.Lime
    ConnectionEngine.Status.DUPLICADA -> LabColors.Sky
    ConnectionEngine.Status.INVALIDA -> LabColors.Coral
    ConnectionEngine.Status.MISMO_SISTEMA -> LabColors.Amber
    ConnectionEngine.Status.DESCONOCIDA -> LabColors.Coral
    null -> LabColors.Sky
}

private fun nearestNode(nodes: Map<String, Offset>, point: Offset): String? {
    var best: String? = null
    var bestDistance = Float.MAX_VALUE
    nodes.forEach { (id, center) ->
        val distance = hypot(center.x - point.x, center.y - point.y)
        if (distance < bestDistance) {
            bestDistance = distance
            best = id
        }
    }
    return if (bestDistance <= 90f) best else null
}

private fun iconOf(iconKey: String) =
    runCatching { com.educalab.exploravida.domain.model.IllustrationKey.valueOf(iconKey) }
        .getOrDefault(com.educalab.exploravida.domain.model.IllustrationKey.LUPA)
