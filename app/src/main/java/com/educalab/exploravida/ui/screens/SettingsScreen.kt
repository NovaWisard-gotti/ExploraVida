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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.Illustration
import com.educalab.exploravida.ui.components.LabButton
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.lab.LabViewModel
import com.educalab.exploravida.ui.theme.LabColors
import com.educalab.exploravida.util.LocalFeedback

/** Ajustes: sonido, vibracion, privacidad y reinicio del progreso. */
@Composable
fun SettingsScreen(
    viewModel: LabViewModel,
    onBack: () -> Unit,
    onOpenProfile: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val feedback = LocalFeedback.current
    var sound by remember(state.profile?.soundEnabled) {
        mutableStateOf(state.profile?.soundEnabled ?: true)
    }
    var haptics by remember(state.profile?.hapticsEnabled) {
        mutableStateOf(state.profile?.hapticsEnabled ?: true)
    }
    var confirmReset by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        SceneBackdrop(SceneBackground.LABORATORIO_NOCHE, Modifier.fillMaxSize())
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                Text("Ajustes", style = MaterialTheme.typography.headlineSmall, color = LabColors.Paper)
            }

            Spacer(Modifier.height(14.dp))
            GlassPanel(Modifier.fillMaxWidth(), tint = LabColors.Sky) {
                Column {
                    ToggleRow(
                        title = "Sonidos del laboratorio",
                        subtitle = "Tonos cortos al tocar y al acertar.",
                        icon = IllustrationKey.PROBETA,
                        checked = sound,
                        onChange = {
                            sound = it
                            feedback.soundEnabled = it
                            viewModel.savePreferences(sound, haptics)
                        }
                    )
                    Spacer(Modifier.height(10.dp))
                    ToggleRow(
                        title = "Vibracion suave",
                        subtitle = "Un toque corto al arrastrar o desbloquear.",
                        icon = IllustrationKey.PARTICULA_ENERGIA,
                        checked = haptics,
                        onChange = {
                            haptics = it
                            feedback.hapticsEnabled = it
                            viewModel.savePreferences(sound, haptics)
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            GlassPanel(Modifier.fillMaxWidth(), tint = LabColors.Lime) {
                Column {
                    Text("Privacidad", style = MaterialTheme.typography.titleMedium, color = LabColors.Paper)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "ExploraVida no pide nombre real, correo, telefono ni ubicacion. " +
                            "No usa camara ni microfono y no declara ningun permiso. " +
                            "Todo el progreso se guarda solo en este dispositivo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabColors.Sand
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            GlassPanel(Modifier.fillMaxWidth(), tint = LabColors.Amber) {
                Column {
                    Text("Tu ficha", style = MaterialTheme.typography.titleMedium, color = LabColors.Paper)
                    Text(
                        "Alias actual: " + (state.profile?.alias ?: "Explorador"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabColors.Sand
                    )
                    Spacer(Modifier.height(8.dp))
                    LabButton("Cambiar alias y avatar", onOpenProfile, tint = LabColors.Amber)
                }
            }

            Spacer(Modifier.height(12.dp))
            GlassPanel(Modifier.fillMaxWidth(), tint = LabColors.Coral) {
                Column {
                    Text("Empezar de cero", style = MaterialTheme.typography.titleMedium, color = LabColors.Paper)
                    Text(
                        "Se borran puntos, pegatinas e insignias. El contenido del laboratorio se queda.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabColors.Sand
                    )
                    Spacer(Modifier.height(8.dp))
                    if (confirmReset) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            LabButton(
                                "Si, reiniciar",
                                onClick = {
                                    viewModel.resetProgress()
                                    confirmReset = false
                                },
                                tint = LabColors.Coral
                            )
                            LabButton("Cancelar", { confirmReset = false }, tint = LabColors.Sky)
                        }
                    } else {
                        LabButton("Reiniciar progreso", { confirmReset = true }, tint = LabColors.Coral)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Text(
                "ExploraVida 1.0.0 - funciona sin internet",
                style = MaterialTheme.typography.labelSmall,
                color = LabColors.Sand.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    icon: IllustrationKey,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(LabColors.GlassSoft)
                .border(1.dp, LabColors.Sky.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Illustration(icon, Modifier.size(26.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = LabColors.Paper)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = LabColors.Sand)
        }
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = LabColors.Deep,
                checkedTrackColor = LabColors.Lime,
                uncheckedTrackColor = LabColors.Locked
            )
        )
    }
}
