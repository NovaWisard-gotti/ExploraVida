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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.Illustration
import com.educalab.exploravida.ui.components.LabButton
import com.educalab.exploravida.ui.components.NoraBubble
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.lab.LabViewModel
import com.educalab.exploravida.ui.lab.avatarIllustration
import com.educalab.exploravida.ui.theme.LabColors

/** 8 alias sugeridos. Nunca se pide el nombre real. */
private val aliasOptions = listOf(
    "Explorador", "Exploradora", "Bioaventura", "Curiosa",
    "Curioso", "Doctor Vita", "Doctora Vita", "Cientifico",
    "Cientifica", "Rastreador", "Rastreadora", "Capitan celula"
)

@Composable
fun ProfileScreen(viewModel: LabViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var alias by remember(state.profile?.alias) { mutableStateOf(state.profile?.alias ?: "Explorador") }
    var avatar by remember(state.profile?.avatarId) { mutableIntStateOf(state.profile?.avatarId ?: 0) }

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
                        .clickable { onBack() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Volver", style = MaterialTheme.typography.labelLarge, color = LabColors.Sand)
                }
                Spacer(Modifier.width(12.dp))
                Text("Tu ficha de explorador", style = MaterialTheme.typography.headlineSmall, color = LabColors.Paper)
            }

            Spacer(Modifier.height(12.dp))
            GlassPanel(Modifier.fillMaxWidth(), tint = LabColors.Sky) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(LabColors.Sky.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Illustration(avatarIllustration(avatar.coerceIn(0, 7)), Modifier.size(44.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(alias, style = MaterialTheme.typography.titleLarge, color = LabColors.Paper)
                        Text(
                            "Nivel " + state.level + " - " + state.levelTitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = LabColors.Lime
                        )
                        Text(
                            "Pegatinas " + state.stats.discoveries + " - Insignias " + state.earnedBadgeIds.size,
                            style = MaterialTheme.typography.labelSmall,
                            color = LabColors.Sand
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            Text("Elige tu avatar", style = MaterialTheme.typography.labelLarge, color = LabColors.Amber)
            Spacer(Modifier.height(8.dp))
            AvatarPicker(avatar) { avatar = it }

            Spacer(Modifier.height(14.dp))
            Text("Elige tu alias", style = MaterialTheme.typography.labelLarge, color = LabColors.Amber)
            Spacer(Modifier.height(8.dp))
            AliasPicker(alias) { alias = it }

            Spacer(Modifier.height(14.dp))
            NoraBubble(
                "No hace falta tu nombre real: elige el alias que mas te guste.",
                Modifier.fillMaxWidth(),
                compact = true
            )

            Spacer(Modifier.weight(1f))
            LabButton(
                "Guardar ficha",
                onClick = {
                    viewModel.saveIdentity(alias, avatar)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AvatarPicker(selected: Int, onSelect: (Int) -> Unit) {
    Column {
        listOf(0..3, 4..7).forEach { range ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                range.forEach { index ->
                    Box(
                        Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == selected) LabColors.Lime.copy(alpha = 0.28f)
                                else LabColors.Glass.copy(alpha = 0.8f)
                            )
                            .border(
                                2.dp,
                                if (index == selected) LabColors.Lime else LabColors.Locked,
                                CircleShape
                            )
                            .clickable { onSelect(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Illustration(avatarIllustration(index), Modifier.size(36.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AliasPicker(selected: String, onSelect: (String) -> Unit) {
    Column {
        aliasOptions.chunked(3).forEach { row ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { option ->
                    Box(
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (option == selected) LabColors.Sky.copy(alpha = 0.30f)
                                else LabColors.Glass.copy(alpha = 0.75f)
                            )
                            .clickable { onSelect(option) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            option,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (option == selected) LabColors.Paper else LabColors.Sand
                        )
                    }
                }
            }
        }
    }
}
