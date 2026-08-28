package com.educalab.exploravida.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.domain.engine.RewardEngine
import com.educalab.exploravida.domain.model.BadgeModel
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.ui.components.BadgeArt
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.LabButton
import com.educalab.exploravida.ui.components.ProgressTube
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.lab.LabViewModel
import com.educalab.exploravida.ui.theme.LabColors

/** Sala de insignias: 12 recompensas reales, con lo que falta para cada una. */
@Composable
fun BadgesScreen(viewModel: LabViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var selected by remember { mutableStateOf<BadgeModel?>(null) }

    LaunchedEffect(Unit) { viewModel.refresh() }

    val engine = remember(state.badges) { RewardEngine(state.badges) }

    Box(Modifier.fillMaxSize()) {
        SceneBackdrop(SceneBackground.SALA_INSIGNIAS, Modifier.fillMaxSize())
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
                Column {
                    Text("Sala de insignias", style = MaterialTheme.typography.headlineSmall, color = LabColors.Paper)
                    Text(
                        "Ganadas: " + state.earnedBadgeIds.size + " de " + state.badges.size,
                        style = MaterialTheme.typography.labelSmall,
                        color = LabColors.Amber
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            ProgressTube(
                if (state.badges.isEmpty()) 0f
                else state.earnedBadgeIds.size.toFloat() / state.badges.size,
                Modifier.fillMaxWidth(),
                tint = LabColors.Amber
            )
            Spacer(Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.badges, key = { it.id }) { badge ->
                    val earned = badge.id in state.earnedBadgeIds
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                if (earned) LabColors.Glass.copy(alpha = 0.9f)
                                else LabColors.Ink.copy(alpha = 0.5f)
                            )
                            .border(
                                2.dp,
                                if (earned) LabColors.Amber.copy(alpha = 0.7f) else LabColors.Locked,
                                RoundedCornerShape(18.dp)
                            )
                            .clickable { selected = badge }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BadgeArt(badge.iconKey, earned)
                        Text(
                            badge.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (earned) LabColors.Paper else LabColors.Sand.copy(alpha = 0.55f),
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                        Text(
                            if (earned) "Conseguida" else "Bloqueada",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (earned) LabColors.Lime else LabColors.Locked
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            GlassPanel(Modifier.fillMaxWidth(), tint = LabColors.Amber) {
                val badge = selected
                Column {
                    Text(
                        badge?.name ?: "Toca una insignia",
                        style = MaterialTheme.typography.titleMedium,
                        color = LabColors.Paper
                    )
                    Text(
                        badge?.description ?: "Cada insignia se gana haciendo algo de verdad en el laboratorio.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabColors.Sand
                    )
                    if (badge != null && badge.id !in state.earnedBadgeIds) {
                        Spacer(Modifier.height(6.dp))
                        val progress = engine.progressTowards(badge, state.stats)
                        ProgressTube(progress, Modifier.fillMaxWidth(), tint = LabColors.Violet)
                        Text(
                            "Progreso: " + (progress * 100).toInt() + "%",
                            style = MaterialTheme.typography.labelSmall,
                            color = LabColors.Violet
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            LabButton("Volver al laboratorio", onBack, Modifier.fillMaxWidth(), tint = LabColors.Lime)
        }
    }
}
