package com.educalab.exploravida.ui.activities

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.data.local.entity.SequenceItemEntity
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.Illustration
import com.educalab.exploravida.ui.components.LabButton
import com.educalab.exploravida.ui.components.NoraBubble
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.theme.LabColors
import com.educalab.exploravida.util.LocalFeedback

/** Actividad ORDENAR: colocar en la via las tarjetas ilustradas en el orden correcto. */
@Composable
fun SequenceScreen(
    activityId: String,
    viewModel: SequenceViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val feedback = LocalFeedback.current
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(activityId) { viewModel.load(activityId) }

    Box(Modifier.fillMaxSize()) {
        SceneBackdrop(SceneBackground.PAPEL_CUADERNO, Modifier.fillMaxSize())
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            ActivityHeader(state.title, state.situation, state.stars, onBack, Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            Text(state.prompt, style = MaterialTheme.typography.titleMedium, color = LabColors.Paper)
            Spacer(Modifier.height(10.dp))

            Text("La via", style = MaterialTheme.typography.labelLarge, color = LabColors.Lime)
            Spacer(Modifier.height(6.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(LabColors.Glass.copy(alpha = 0.7f))
                    .border(2.dp, LabColors.Lime.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .padding(8.dp)
            ) {
                if (state.placed.isEmpty()) {
                    Text(
                        "Toca las tarjetas de abajo para ir colocandolas aqui.",
                        style = MaterialTheme.typography.bodySmall,
                        color = LabColors.Sand.copy(alpha = 0.8f),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.placed, key = { it.id }) { item ->
                            SequenceCard(
                                item = item,
                                position = state.placed.indexOf(item) + 1,
                                tint = LabColors.Lime,
                                onClick = {
                                    if (state.placed.lastOrNull()?.id == item.id) {
                                        feedback.tapSound()
                                        viewModel.removeLast()
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Text("Tarjetas sueltas", style = MaterialTheme.typography.labelLarge, color = LabColors.Amber)
            Spacer(Modifier.height(6.dp))
            Box(Modifier.fillMaxWidth().weight(1f)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.pool, key = { it.id }) { item ->
                        SequenceCard(
                            item = item,
                            position = null,
                            tint = LabColors.Amber,
                            onClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                feedback.tapSound()
                                viewModel.place(item)
                            }
                        )
                    }
                }
            }

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
                Spacer(Modifier.height(8.dp))
            } else {
                NoraBubble("Piensa que ocurre primero y que ocurre despues.", Modifier.fillMaxWidth(), compact = true)
                Spacer(Modifier.height(8.dp))
            }

            if (state.solved) {
                LabButton("Volver al laboratorio", onBack, Modifier.fillMaxWidth(), tint = LabColors.Lime)
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LabButton("Pista", { viewModel.hint() }, tint = LabColors.Sky)
                    LabButton("Reiniciar", { viewModel.retry() }, tint = LabColors.Violet)
                    LabButton(
                        text = "Comprobar",
                        onClick = {
                            feedback.successSound()
                            viewModel.check()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = state.pool.isEmpty() && state.placed.isNotEmpty()
                    )
                }
            }
        }
    }
}

@Composable
private fun SequenceCard(
    item: SequenceItemEntity,
    position: Int?,
    tint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    val appear by animateFloatAsState(targetValue = 1f, animationSpec = tween(260), label = "tarjeta")
    Column(
        modifier = Modifier
            .scale(0.9f + appear * 0.1f)
            .width(118.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(LabColors.Glass.copy(alpha = 0.9f))
            .border(2.dp, tint.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Illustration(item.illustrationKey, Modifier.size(46.dp))
        Spacer(Modifier.height(4.dp))
        Text(
            item.label,
            style = MaterialTheme.typography.bodySmall,
            color = LabColors.Paper
        )
        if (position != null) {
            Text(
                "paso " + position,
                style = MaterialTheme.typography.labelSmall,
                color = tint
            )
        }
    }
}
