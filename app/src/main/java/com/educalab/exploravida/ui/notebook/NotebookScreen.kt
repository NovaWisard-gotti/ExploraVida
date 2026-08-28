package com.educalab.exploravida.ui.notebook

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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.data.local.entity.ExplorerNotebookEntity
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.Illustration
import com.educalab.exploravida.ui.components.illustrationForIcon
import com.educalab.exploravida.ui.components.LabButton
import com.educalab.exploravida.ui.components.NoraBubble
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.lab.LabViewModel
import com.educalab.exploravida.ui.theme.LabColors
import com.educalab.exploravida.util.LocalFeedback

/**
 * CUADERNO DEL EXPLORADOR.
 * Coleccion real: cada pegatina se desbloquea al descubrir el concepto.
 */
@Composable
fun NotebookScreen(viewModel: LabViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val feedback = LocalFeedback.current
    var selected by remember { mutableStateOf<ExplorerNotebookEntity?>(null) }

    LaunchedEffect(Unit) { viewModel.refresh() }

    val discoveriesByKey = state.discoveries.associateBy { it.conceptKey }
    val unlockedKeys = discoveriesByKey.keys

    Box(Modifier.fillMaxSize()) {
        SceneBackdrop(SceneBackground.PAPEL_CUADERNO, Modifier.fillMaxSize())
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
                    Text(
                        "Cuaderno del explorador",
                        style = MaterialTheme.typography.headlineSmall,
                        color = LabColors.Paper
                    )
                    Text(
                        "Pegatinas: " + unlockedKeys.size + " de " + state.pages.size,
                        style = MaterialTheme.typography.labelSmall,
                        color = LabColors.Lime
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            NoraBubble(
                if (unlockedKeys.isEmpty()) {
                    "Aun no hay pegatinas. Completa una experiencia y aparecera la primera."
                } else {
                    "Toca una pegatina para leer lo que descubriste."
                },
                Modifier.fillMaxWidth(),
                compact = true
            )
            Spacer(Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.pages, key = { it.id }) { page ->
                    val unlocked = conceptKeyOf(page) in unlockedKeys
                    Sticker(
                        page = page,
                        unlocked = unlocked,
                        onClick = {
                            feedback.tapSound()
                            selected = if (unlocked) page else null
                        }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            GlassPanel(Modifier.fillMaxWidth(), tint = LabColors.Amber) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Illustration(
                        illustrationForIcon(selected?.stickerKey ?: "ic_notebook"),
                        Modifier.size(52.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            selected?.title ?: "Elige una pegatina",
                            style = MaterialTheme.typography.titleMedium,
                            color = LabColors.Paper
                        )
                        Text(
                            selected?.let { discoveriesByKey[conceptKeyOf(it)]?.text }
                                ?: "Aqui se guarda todo lo que vas descubriendo sobre los seres vivos.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LabColors.Sand
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            LabButton("Seguir explorando", onBack, Modifier.fillMaxWidth(), tint = LabColors.Lime)
        }
    }
}

@Composable
private fun Sticker(
    page: ExplorerNotebookEntity,
    unlocked: Boolean,
    onClick: () -> Unit
) {
    val appear by animateFloatAsState(
        targetValue = if (unlocked) 1f else 0f,
        animationSpec = tween(400),
        label = "pegatina"
    )
    Column(
        modifier = Modifier
            .height(122.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (unlocked) LabColors.Glass.copy(alpha = 0.9f)
                else LabColors.Ink.copy(alpha = 0.55f)
            )
            .border(
                2.dp,
                if (unlocked) LabColors.Amber.copy(alpha = 0.7f) else LabColors.Locked,
                RoundedCornerShape(18.dp)
            )
            .clickable(enabled = unlocked) { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
            if (unlocked) {
                Illustration(
                    illustrationForIcon(page.stickerKey),
                    Modifier.size(46.dp).rotate((1f - appear) * -12f)
                )
            } else {
                Illustration(com.educalab.exploravida.domain.model.IllustrationKey.LUPA, Modifier.size(30.dp))
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            if (unlocked) page.title else "Por descubrir",
            style = MaterialTheme.typography.labelSmall,
            color = if (unlocked) LabColors.Paper else LabColors.Sand.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

private fun conceptKeyOf(page: ExplorerNotebookEntity): String = page.id.removePrefix("page_")
