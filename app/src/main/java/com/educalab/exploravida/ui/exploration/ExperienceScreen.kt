package com.educalab.exploravida.ui.exploration

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.ui.animation.CelebrationBurst
import com.educalab.exploravida.ui.animation.JourneyTrack
import com.educalab.exploravida.ui.animation.TrackStop
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.Illustration
import com.educalab.exploravida.ui.components.LabButton
import com.educalab.exploravida.ui.components.NoraBubble
import com.educalab.exploravida.ui.components.ProgressTube
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.components.VitaMood
import com.educalab.exploravida.ui.components.VitaOrganism
import com.educalab.exploravida.ui.theme.LabColors
import com.educalab.exploravida.util.LocalFeedback

/**
 * Ejecuta una experiencia: recorrido visual con paradas tocables,
 * Vita reaccionando y una explicacion corta en cada paso.
 */
@Composable
fun ExperienceScreen(
    experienceId: String,
    viewModel: ExperienceViewModel,
    onBack: () -> Unit,
    onOpenActivity: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val feedback = LocalFeedback.current
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(experienceId) { viewModel.load(experienceId) }

    Box(Modifier.fillMaxSize()) {
        SceneBackdrop(
            state.experience?.background ?: com.educalab.exploravida.domain.model.SceneBackground.LABORATORIO,
            Modifier.fillMaxSize()
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(LabColors.GlassSoft)
                        .clickable { onBack() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Volver", style = MaterialTheme.typography.labelLarge, color = LabColors.Sand)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        state.experience?.title ?: "Experiencia",
                        style = MaterialTheme.typography.headlineSmall,
                        color = LabColors.Paper
                    )
                    Text(
                        "Paso " + (state.index + 1) + " de " + state.steps.size.coerceAtLeast(1),
                        style = MaterialTheme.typography.labelSmall,
                        color = LabColors.Sand.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            ProgressTube(state.progress, Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))

            Row(Modifier.fillMaxWidth().weight(1f)) {
                JourneyTrack(
                    stops = state.steps.mapIndexed { index, step ->
                        TrackStop(step.title, step.systemId, index in state.visited)
                    },
                    currentIndex = state.index,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    onStopTap = { index ->
                        feedback.tapSound()
                        viewModel.goTo(index)
                    }
                )
                Spacer(Modifier.width(8.dp))
                VitaStage(
                    mood = moodFor(state.current?.systemId),
                    highlightedSystems = state.current?.systemId?.let { setOf(it) } ?: emptySet(),
                    finished = state.finished,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                )
            }

            Spacer(Modifier.height(8.dp))

            GlassPanel(Modifier.fillMaxWidth(), tint = stepTint(state.current?.systemId)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Illustration(
                        state.current?.illustrationKey ?: "VITA_TRANQUILA",
                        Modifier.size(56.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            state.current?.title ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            color = LabColors.Paper
                        )
                        Text(
                            state.current?.text ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LabColors.Sand
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            if (state.finished) {
                FinishPanel(
                    discoveries = state.newDiscoveries.size,
                    badges = state.newBadges.map { it.name },
                    onRepeat = { viewModel.restart() },
                    onActivity = { state.primaryActivityId?.let(onOpenActivity) },
                    hasActivity = state.primaryActivityId != null,
                    onBack = onBack
                )
            } else {
                NoraBubble(state.noraMessage, Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    LabButton(
                        text = "Atras",
                        onClick = { viewModel.previous() },
                        tint = LabColors.Sky,
                        enabled = state.index > 0
                    )
                    LabButton(
                        text = if (state.isLast) "Terminar" else "Siguiente",
                        onClick = {
                            haptics.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            feedback.tapSound()
                            viewModel.next()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun VitaStage(
    mood: VitaMood,
    highlightedSystems: Set<String>,
    finished: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        VitaOrganism(
            modifier = Modifier.fillMaxSize(),
            mood = mood,
            highlightedSystems = highlightedSystems
        )
        AnimatedVisibility(
            visible = finished,
            enter = fadeIn(tween(220)) + scaleIn(tween(220)),
            exit = fadeOut(tween(160))
        ) {
            CelebrationBurst(Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun FinishPanel(
    discoveries: Int,
    badges: List<String>,
    onRepeat: () -> Unit,
    onActivity: () -> Unit,
    hasActivity: Boolean,
    onBack: () -> Unit
) {
    GlassPanel(Modifier.fillMaxWidth(), tint = LabColors.Amber) {
        Column {
            Text(
                "Descubrimiento completado",
                style = MaterialTheme.typography.titleLarge,
                color = LabColors.Amber
            )
            Text(
                if (discoveries > 0) {
                    "Has guardado " + discoveries + " pegatina(s) nueva(s) en el cuaderno."
                } else {
                    "Ya tenias estas pegatinas, pero repasar siempre viene bien."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = LabColors.Sand,
                textAlign = TextAlign.Start
            )
            if (badges.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Insignia nueva: " + badges.joinToString(", "),
                    style = MaterialTheme.typography.labelLarge,
                    color = LabColors.Lime
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LabButton("Practicar otra vez", onRepeat, tint = LabColors.Sky)
                if (hasActivity) LabButton("Ir al reto", onActivity, tint = LabColors.Amber)
            }
            Spacer(Modifier.height(8.dp))
            LabButton("Volver al laboratorio", onBack, Modifier.fillMaxWidth(), tint = LabColors.Lime)
        }
    }
}

private fun moodFor(systemId: String?): VitaMood = when (systemId) {
    "digestivo" -> VitaMood.COMIENDO
    "respiratorio" -> VitaMood.RESPIRANDO
    "movimiento" -> VitaMood.CORRIENDO
    "relacion" -> VitaMood.CURIOSA
    else -> VitaMood.TRANQUILA
}

private fun stepTint(systemId: String?) =
    if (systemId == null) LabColors.Sky else LabColors.ofSystem(systemId)
