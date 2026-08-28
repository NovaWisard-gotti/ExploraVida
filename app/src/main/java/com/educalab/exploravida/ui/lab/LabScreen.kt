package com.educalab.exploravida.ui.lab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.domain.model.ModuleState
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.Illustration
import com.educalab.exploravida.ui.components.NoraBubble
import com.educalab.exploravida.ui.components.ProgressTube
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.components.StateChip
import com.educalab.exploravida.ui.components.SystemGlyph
import com.educalab.exploravida.ui.components.VitaMood
import com.educalab.exploravida.ui.components.VitaOrganism
import com.educalab.exploravida.ui.theme.LabColors
import kotlin.math.cos
import kotlin.math.sin

/**
 * EL LABORATORIO. Pantalla principal.
 *
 * No es un dashboard ni una lista de botones: es una sala con Vita flotando
 * en el centro, los seis sistemas girando a su alrededor y un carril de
 * estaciones de experiencia en la parte baja.
 */
@Composable
fun LabScreen(
    viewModel: LabViewModel,
    onOpenExperience: (String) -> Unit,
    onExploreOrganism: () -> Unit,
    onOpenNotebook: () -> Unit,
    onOpenBadges: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenReview: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var focusedSystem by remember { mutableStateOf<String?>(null) }

    Box(Modifier.fillMaxSize()) {
        SceneBackdrop(SceneBackground.LABORATORIO, Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            LabHeader(
                alias = state.profile?.alias ?: "Explorador",
                avatarId = state.profile?.avatarId ?: 0,
                levelTitle = state.levelTitle,
                level = state.level,
                levelProgress = state.levelProgress,
                badges = state.earnedBadgeIds.size,
                discoveries = state.stats.discoveries,
                onOpenNotebook = onOpenNotebook,
                onOpenBadges = onOpenBadges,
                onOpenSettings = onOpenSettings,
                onOpenProfile = onOpenProfile
            )

            Spacer(Modifier.height(6.dp))

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                val orbit = minOf(maxWidth, maxHeight) * 0.40f
                VitaOrganism(
                    modifier = Modifier
                        .size(minOf(maxWidth, maxHeight) * 0.52f)
                        .clickable { onExploreOrganism() },
                    mood = VitaMood.TRANQUILA,
                    highlightedSystems = focusedSystem?.let { setOf(it) } ?: emptySet()
                )
                state.systems.forEachIndexed { index, system ->
                    val angle = (-90.0 + index * (360.0 / state.systems.size.coerceAtLeast(1))) * Math.PI / 180.0
                    SystemGlyph(
                        systemId = system.id,
                        label = system.name.removePrefix("Sistema ").replaceFirstChar { it.uppercase() },
                        active = focusedSystem == system.id,
                        onClick = {
                            focusedSystem = if (focusedSystem == system.id) null else system.id
                        },
                        modifier = Modifier.offset(
                            x = orbit * cos(angle).toFloat(),
                            y = orbit * sin(angle).toFloat()
                        )
                    )
                }
            }

            val focused = state.systems.firstOrNull { it.id == focusedSystem }
            NoraBubble(
                message = focused?.shortDescription
                    ?: state.next?.noraIntro
                    ?: "Toca a Vita para explorarla por dentro.",
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            )

            Text(
                text = "ESTACIONES DEL LABORATORIO",
                style = MaterialTheme.typography.labelSmall,
                color = LabColors.Sand.copy(alpha = 0.75f),
                modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.cards.forEach { card ->
                    StationTile(
                        title = card.experience.title,
                        subtitle = card.experience.subtitle,
                        iconKey = card.experience.iconKey,
                        state = card.state,
                        isNext = card.experience.id == state.next?.id,
                        requiredXp = card.experience.requiredXp,
                        onClick = {
                            if (card.state != ModuleState.BLOQUEADO) {
                                if (card.experience.id == "exp_explorar") onExploreOrganism()
                                else onOpenExperience(card.experience.id)
                            }
                        }
                    )
                }
                if (state.reviewActivities.isNotEmpty()) {
                    ReviewTile(
                        pending = state.reviewActivities.size,
                        onClick = { onOpenReview(state.reviewActivities.first().id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewTile(pending: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(LabColors.Violet.copy(alpha = 0.18f))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Illustration(IllustrationKey.PROBETA, Modifier.size(52.dp))
        Spacer(Modifier.height(6.dp))
        Text(
            "Practicar otra vez",
            style = MaterialTheme.typography.titleMedium,
            color = LabColors.Paper,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        Text(
            "Tienes " + pending + " reto(s) por repasar",
            style = MaterialTheme.typography.labelSmall,
            color = LabColors.Violet,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun LabHeader(
    alias: String,
    avatarId: Int,
    levelTitle: String,
    level: Int,
    levelProgress: Float,
    badges: Int,
    discoveries: Int,
    onOpenNotebook: () -> Unit,
    onOpenBadges: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenProfile: () -> Unit
) {
    GlassPanel(modifier = Modifier.fillMaxWidth(), tint = LabColors.Lime) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(LabColors.ofSystem(avatarKeyOf(avatarId)).copy(alpha = 0.30f))
                        .clickable { onOpenProfile() },
                    contentAlignment = Alignment.Center
                ) {
                    Illustration(avatarIllustration(avatarId), Modifier.size(32.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(alias, style = MaterialTheme.typography.titleMedium, color = LabColors.Paper)
                    Text(
                        "Nivel " + level + " - " + levelTitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = LabColors.Lime
                    )
                }
                ShelfTool("Cuaderno", IllustrationKey.CUADERNO, onOpenNotebook)
                Spacer(Modifier.width(6.dp))
                ShelfTool("Insignias", IllustrationKey.MATRAZ, onOpenBadges)
                Spacer(Modifier.width(6.dp))
                ShelfTool("Ajustes", IllustrationKey.MICROSCOPIO, onOpenSettings)
            }
            Spacer(Modifier.height(8.dp))
            ProgressTube(levelProgress, Modifier.fillMaxWidth())
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    "Pegatinas: " + discoveries,
                    style = MaterialTheme.typography.labelSmall,
                    color = LabColors.Sand
                )
                Text(
                    "Insignias: " + badges,
                    style = MaterialTheme.typography.labelSmall,
                    color = LabColors.Sand
                )
            }
        }
    }
}

@Composable
private fun ShelfTool(label: String, key: IllustrationKey, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(
            Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(LabColors.GlassSoft),
            contentAlignment = Alignment.Center
        ) {
            Illustration(key, Modifier.size(24.dp))
        }
        Text(label, style = MaterialTheme.typography.labelSmall, color = LabColors.Sand.copy(alpha = 0.8f))
    }
}

@Composable
private fun StationTile(
    title: String,
    subtitle: String,
    iconKey: String,
    state: ModuleState,
    isNext: Boolean,
    requiredXp: Int,
    onClick: () -> Unit
) {
    val locked = state == ModuleState.BLOQUEADO
    Column(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isNext) LabColors.Lime.copy(alpha = 0.18f) else LabColors.Glass.copy(alpha = 0.80f)
            )
            .clickable(enabled = !locked) { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Illustration(
            key = stationIllustration(iconKey),
            modifier = Modifier.size(52.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = if (locked) LabColors.Sand.copy(alpha = 0.55f) else LabColors.Paper,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        Text(
            if (locked) "Necesitas " + requiredXp + " puntos" else subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = LabColors.Sand.copy(alpha = 0.75f),
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        Spacer(Modifier.height(8.dp))
        StateChip(state)
    }
}

private fun stationIllustration(iconKey: String): IllustrationKey = when (iconKey) {
    "ic_leaf" -> IllustrationKey.MANZANA
    "ic_lungs" -> IllustrationKey.PULMONES
    "ic_muscle" -> IllustrationKey.MUSCULO
    "ic_spark" -> IllustrationKey.PARTICULA_ENERGIA
    "ic_drop" -> IllustrationKey.GOTA
    "ic_link" -> IllustrationKey.CAMINOS
    "ic_path" -> IllustrationKey.INTESTINO
    "ic_heartbeat" -> IllustrationKey.CORAZON
    "ic_flask" -> IllustrationKey.MATRAZ
    else -> IllustrationKey.LUPA
}

fun avatarIllustration(avatarId: Int): IllustrationKey = when (avatarId) {
    0 -> IllustrationKey.VITA_TRANQUILA
    1 -> IllustrationKey.VITA_CURIOSA
    2 -> IllustrationKey.VITA_CORRIENDO
    3 -> IllustrationKey.VITA_RESPIRANDO
    4 -> IllustrationKey.LUPA
    5 -> IllustrationKey.MATRAZ
    6 -> IllustrationKey.MICROSCOPIO
    else -> IllustrationKey.PROBETA
}

fun avatarKeyOf(avatarId: Int): String = when (avatarId % 6) {
    0 -> "digestivo"
    1 -> "respiratorio"
    2 -> "circulatorio"
    3 -> "movimiento"
    4 -> "limpieza"
    else -> "relacion"
}
