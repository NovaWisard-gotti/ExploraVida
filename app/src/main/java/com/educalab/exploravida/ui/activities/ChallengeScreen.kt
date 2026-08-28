package com.educalab.exploravida.ui.activities

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.domain.usecase.Challenge
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.Illustration
import com.educalab.exploravida.ui.components.LabButton
import com.educalab.exploravida.ui.components.NoraBubble
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.components.SystemGlyph
import com.educalab.exploravida.ui.components.VitaMood
import com.educalab.exploravida.ui.components.VitaOrganism
import com.educalab.exploravida.ui.theme.LabColors
import com.educalab.exploravida.util.LocalFeedback
import kotlin.math.roundToInt

/** Retos de arrastrar, predecir, comparar y observar. */
@Composable
fun ChallengeScreen(
    activityId: String,
    viewModel: ChallengeViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val feedback = LocalFeedback.current

    LaunchedEffect(activityId) { viewModel.load(activityId) }

    Box(Modifier.fillMaxSize()) {
        SceneBackdrop(backgroundFor(state.challenge), Modifier.fillMaxSize())
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            ActivityHeader(state.title, state.situation, state.stars, onBack, Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Text(state.prompt, style = MaterialTheme.typography.titleMedium, color = LabColors.Paper)
            Spacer(Modifier.height(8.dp))

            Box(Modifier.fillMaxWidth().weight(1f)) {
                when (val challenge = state.challenge) {
                    is Challenge.Drag -> DragBoard(challenge, state, viewModel)
                    is Challenge.Predict -> PredictBoard(challenge, state, viewModel)
                    is Challenge.Compare -> CompareBoard(challenge, state, viewModel)
                    is Challenge.Observe -> ObserveBoard(challenge, state, viewModel)
                    null -> Text(
                        "Este reto todavia no esta preparado.",
                        color = LabColors.Sand,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            if (state.feedback.isNotBlank()) {
                GlassPanel(
                    Modifier.fillMaxWidth(),
                    tint = if (state.solved) LabColors.Lime else LabColors.Amber
                ) {
                    Text(
                        state.feedback,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (state.solved) LabColors.Lime else LabColors.Sand
                    )
                }
            } else {
                NoraBubble("Observa con calma antes de responder.", Modifier.fillMaxWidth(), compact = true)
            }
            Spacer(Modifier.height(8.dp))

            if (state.solved) {
                LabButton("Volver al laboratorio", onBack, Modifier.fillMaxWidth(), tint = LabColors.Lime)
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LabButton("Reiniciar", { viewModel.retry() }, tint = LabColors.Violet)
                    if (state.challenge is Challenge.Predict) {
                        LabButton(
                            text = "Comprobar",
                            onClick = {
                                feedback.tapSound()
                                viewModel.submitPredict()
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DragBoard(
    challenge: Challenge.Drag,
    state: ChallengeUiState,
    viewModel: ChallengeViewModel
) {
    val density = LocalDensity.current
    val haptics = LocalHapticFeedback.current
    val feedback = LocalFeedback.current
    var dragged by remember { mutableStateOf<IllustrationKey?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            VitaOrganism(
                modifier = Modifier.fillMaxSize(),
                mood = if (state.solved) VitaMood.COMIENDO else VitaMood.TRANQUILA,
                highlightedSystems = if (state.solved) setOf("digestivo", "circulatorio") else emptySet()
            )
            if (state.droppedItem != null && state.solved) {
                Illustration(state.droppedItem, Modifier.size(52.dp))
            }
        }
        Text(
            "Arrastra la pieza correcta hasta Vita",
            style = MaterialTheme.typography.labelLarge,
            color = LabColors.Amber
        )
        Spacer(Modifier.height(6.dp))
        Row(
            Modifier.fillMaxWidth().height(96.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            challenge.items.forEach { item ->
                val isDragging = dragged == item
                Box(
                    Modifier
                        .offset {
                            if (isDragging) {
                                IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt())
                            } else {
                                IntOffset.Zero
                            }
                        }
                        .size(72.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(LabColors.Glass.copy(alpha = 0.85f))
                        .border(2.dp, LabColors.Amber.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                        .pointerInput(item, state.solved) {
                            detectDragGestures(
                                onDragStart = {
                                    if (!state.solved) {
                                        dragged = item
                                        dragOffset = Offset.Zero
                                    }
                                },
                                onDrag = { change, amount ->
                                    dragOffset += amount
                                    change.consume()
                                },
                                onDragEnd = {
                                    val liftedUp = with(density) { dragOffset.y < -60.dp.toPx() }
                                    if (liftedUp) {
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        feedback.tapSound()
                                        viewModel.dropItem(item)
                                    }
                                    dragged = null
                                    dragOffset = Offset.Zero
                                },
                                onDragCancel = {
                                    dragged = null
                                    dragOffset = Offset.Zero
                                }
                            )
                        }
                        .padding(8.dp)
                ) {
                    Illustration(item, Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
private fun PredictBoard(
    challenge: Challenge.Predict,
    state: ChallengeUiState,
    viewModel: ChallengeViewModel
) {
    val feedback = LocalFeedback.current
    Column(Modifier.fillMaxSize()) {
        Text(challenge.question, style = MaterialTheme.typography.titleSmall, color = LabColors.Sand)
        Spacer(Modifier.height(10.dp))
        val rows = state.systems.chunked(3)
        rows.forEach { row ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { system ->
                    SystemGlyph(
                        systemId = system.id,
                        label = system.name.removePrefix("Sistema ").take(10),
                        active = system.id in state.chosenSystems,
                        onClick = {
                            feedback.tapSound()
                            viewModel.toggleSystem(system.id)
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "Marcados: " + state.chosenSystems.size,
            style = MaterialTheme.typography.labelSmall,
            color = LabColors.Teal
        )
    }
}

@Composable
private fun CompareBoard(
    challenge: Challenge.Compare,
    state: ChallengeUiState,
    viewModel: ChallengeViewModel
) {
    val feedback = LocalFeedback.current
    var running by remember { mutableStateOf(false) }
    val run by animateFloatAsState(
        targetValue = if (running) 1f else 0f,
        animationSpec = tween(1600),
        label = "simulacion"
    )

    Column(Modifier.fillMaxSize()) {
        Text(challenge.question, style = MaterialTheme.typography.titleSmall, color = LabColors.Sand)
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SimulationColumn(
                label = challenge.labelA,
                energy = challenge.energyA * run,
                selected = state.chosenSide == true,
                enabled = running && !state.solved,
                onPick = {
                    feedback.tapSound()
                    viewModel.chooseSide(true)
                },
                modifier = Modifier.weight(1f)
            )
            SimulationColumn(
                label = challenge.labelB,
                energy = challenge.energyB * run,
                selected = state.chosenSide == false,
                enabled = running && !state.solved,
                onPick = {
                    feedback.tapSound()
                    viewModel.chooseSide(false)
                },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(8.dp))
        LabButton(
            text = if (running) "Repetir simulacion" else "Ejecutar las dos pruebas",
            onClick = {
                running = false
                running = true
            },
            modifier = Modifier.fillMaxWidth(),
            tint = LabColors.Sky
        )
        if (running && !state.solved) {
            Spacer(Modifier.height(4.dp))
            Text(
                "Ahora toca la prueba que creas correcta.",
                style = MaterialTheme.typography.labelSmall,
                color = LabColors.Amber
            )
        }
    }
}

@Composable
private fun SimulationColumn(
    label: String,
    energy: Float,
    selected: Boolean,
    enabled: Boolean,
    onPick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(LabColors.Glass.copy(alpha = 0.8f))
            .border(
                2.dp,
                if (selected) LabColors.Lime else LabColors.Sky.copy(alpha = 0.4f),
                RoundedCornerShape(20.dp)
            )
            .clickable(enabled = enabled) { onPick() }
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = LabColors.Paper)
        Spacer(Modifier.height(8.dp))
        Canvas(Modifier.fillMaxWidth().weight(1f)) {
            val barWidth = size.width * 0.45f
            val left = (size.width - barWidth) / 2f
            drawRoundRect(
                color = LabColors.InkSoft.copy(alpha = 0.45f),
                topLeft = Offset(left, 0f),
                size = androidx.compose.ui.geometry.Size(barWidth, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(barWidth / 2f)
            )
            val filled = size.height * energy.coerceIn(0f, 1f)
            drawRoundRect(
                color = LabColors.Amber,
                topLeft = Offset(left, size.height - filled),
                size = androidx.compose.ui.geometry.Size(barWidth, filled),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(barWidth / 2f)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "energia " + (energy * 100).toInt() + "%",
            style = MaterialTheme.typography.labelSmall,
            color = LabColors.Teal
        )
    }
}

@Composable
private fun ObserveBoard(
    challenge: Challenge.Observe,
    state: ChallengeUiState,
    viewModel: ChallengeViewModel
) {
    val feedback = LocalFeedback.current
    val haptics = LocalHapticFeedback.current
    val transition = rememberInfiniteTransition(label = "observa")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "pulso"
    )

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(challenge.instruction, style = MaterialTheme.typography.titleSmall, color = LabColors.Sand)
        Spacer(Modifier.height(6.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable(enabled = !state.solved) {
                    if (pulse > 0.55f) {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        feedback.tapSound()
                        viewModel.countObservation()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            VitaOrganism(
                modifier = Modifier.fillMaxSize(),
                mood = VitaMood.RESPIRANDO,
                highlightedSystems = if (pulse > 0.55f) setOf("respiratorio") else emptySet()
            )
        }
        Text(
            "Contados: " + state.observedCount + " de " + challenge.target,
            style = MaterialTheme.typography.titleMedium,
            color = if (state.solved) LabColors.Lime else LabColors.Amber
        )
        Text(
            "Toca solo cuando la zona del aire se ilumine.",
            style = MaterialTheme.typography.labelSmall,
            color = LabColors.Sand.copy(alpha = 0.85f)
        )
    }
}

private fun backgroundFor(challenge: Challenge?): SceneBackground = when (challenge) {
    is Challenge.Drag -> SceneBackground.CUEVA_ESTOMAGO
    is Challenge.Predict -> SceneBackground.LABORATORIO
    is Challenge.Compare -> SceneBackground.CAMPO_MUSCULO
    is Challenge.Observe -> SceneBackground.CAMARA_AIRE
    null -> SceneBackground.LABORATORIO
}
