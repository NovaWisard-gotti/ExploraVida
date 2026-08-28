package com.educalab.exploravida.ui.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.Illustration
import com.educalab.exploravida.ui.components.LabButton
import com.educalab.exploravida.ui.components.NoraBubble
import com.educalab.exploravida.ui.components.ProgressTube
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.components.SystemGlyph
import com.educalab.exploravida.ui.components.VitaMood
import com.educalab.exploravida.ui.components.VitaOrganism
import com.educalab.exploravida.ui.theme.LabColors
import com.educalab.exploravida.util.LocalFeedback

/**
 * MODO EXPLORACION.
 * Vita ocupa casi toda la pantalla y el nino toca sus zonas para descubrirlas.
 * No hay lista de organos: la exploracion es visual.
 */
@Composable
fun OrganismExplorerScreen(
    viewModel: ExplorerViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val feedback = LocalFeedback.current
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(Unit) { viewModel.load() }

    Box(Modifier.fillMaxSize()) {
        SceneBackdrop(SceneBackground.LABORATORIO, Modifier.fillMaxSize())

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
                        .clickable {
                            viewModel.finish()
                            onBack()
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Volver", style = MaterialTheme.typography.labelLarge, color = LabColors.Sand)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Explora a Vita", style = MaterialTheme.typography.headlineSmall, color = LabColors.Paper)
                    Text(
                        "Zonas descubiertas: " + state.explored.size + " de " + state.elements.size,
                        style = MaterialTheme.typography.labelSmall,
                        color = LabColors.Sand.copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            ProgressTube(state.progress, Modifier.fillMaxWidth(), tint = LabColors.Violet)
            Spacer(Modifier.height(6.dp))

            Row(
                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                state.systems.forEach { system ->
                    SystemGlyph(
                        systemId = system.id,
                        label = system.name.removePrefix("Sistema ").take(9),
                        active = state.selected?.systemId == system.id
                    )
                }
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(state.elements.size) {
                        detectTapGestures { tap ->
                            val nx = tap.x / size.width.toFloat()
                            val ny = tap.y / size.height.toFloat()
                            haptics.performHapticFeedback(
                                androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove
                            )
                            feedback.tapSound()
                            viewModel.tapAt(nx, ny, System.currentTimeMillis())
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                VitaOrganism(
                    modifier = Modifier.fillMaxSize(),
                    mood = VitaMood.CURIOSA,
                    highlightedSystems = state.selected?.systemId?.let { setOf(it) } ?: emptySet(),
                    exploredElements = state.explored,
                    showHotspots = true
                )
            }

            Spacer(Modifier.height(8.dp))

            GlassPanel(
                Modifier.fillMaxWidth(),
                tint = state.selected?.let { LabColors.ofSystem(it.systemId) } ?: LabColors.Sky
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Illustration(
                        state.selected?.illustrationKey ?: "LUPA",
                        Modifier.size(52.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            state.selected?.name ?: "Elige una zona",
                            style = MaterialTheme.typography.titleMedium,
                            color = LabColors.Paper
                        )
                        if (state.selectedSystemName.isNotBlank()) {
                            Text(
                                state.selectedSystemName,
                                style = MaterialTheme.typography.labelSmall,
                                color = state.selected?.let { LabColors.ofSystem(it.systemId) } ?: LabColors.Sand
                            )
                        }
                        Text(state.message, style = MaterialTheme.typography.bodyMedium, color = LabColors.Sand)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            NoraBubble(
                message = if (state.progress >= 0.99f) {
                    "Has explorado a Vita entera. Ahora ya sabes donde trabaja cada sistema."
                } else {
                    "Cada punto brillante pertenece a un sistema distinto."
                },
                modifier = Modifier.fillMaxWidth(),
                compact = true
            )
            Spacer(Modifier.height(8.dp))
            LabButton(
                "Terminar exploracion",
                onClick = {
                    viewModel.finish()
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                tint = LabColors.Violet
            )
        }
    }
}
