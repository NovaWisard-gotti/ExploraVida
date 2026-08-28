package com.educalab.exploravida.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.ui.components.GlassPanel
import com.educalab.exploravida.ui.components.Illustration
import com.educalab.exploravida.ui.components.LabButton
import com.educalab.exploravida.ui.components.NoraBubble
import com.educalab.exploravida.ui.components.SceneBackdrop
import com.educalab.exploravida.ui.components.VitaMood
import com.educalab.exploravida.ui.components.VitaOrganism
import com.educalab.exploravida.ui.theme.LabColors

private data class OnboardingPage(
    val title: String,
    val text: String,
    val illustration: IllustrationKey,
    val background: SceneBackground
)

/**
 * Onboarding de 4 pantallas: mundo, personajes, como se avanza y privacidad.
 * Solo aparece la primera vez.
 */
@Composable
fun OnboardingScreen(onFinish: (String, Int) -> Unit) {
    var page by remember { mutableIntStateOf(0) }
    var alias by remember { mutableStateOf("Explorador") }
    var avatar by remember { mutableIntStateOf(0) }

    val pages = remember {
        listOf(
            OnboardingPage(
                "Bienvenido al laboratorio de la vida",
                "Aqui vas a descubrir como funciona un ser vivo por dentro, sin memorizar listas.",
                IllustrationKey.MATRAZ,
                SceneBackground.LABORATORIO
            ),
            OnboardingPage(
                "Ella es Vita",
                "Vita es el organismo del laboratorio. Come, respira, se mueve y reacciona igual que tu.",
                IllustrationKey.VITA_TRANQUILA,
                SceneBackground.PRADERA
            ),
            OnboardingPage(
                "Nora te acompana",
                "Nora te propone retos: recorridos, conexiones, simulaciones y observaciones.",
                IllustrationKey.NORA,
                SceneBackground.LABORATORIO_NOCHE
            ),
            OnboardingPage(
                "Todo se queda en tu tablet",
                "No pedimos nombre real, ni correo, ni permisos. La app funciona sin internet.",
                IllustrationKey.CUADERNO,
                SceneBackground.PAPEL_CUADERNO
            )
        )
    }

    val current = pages[page]

    Box(Modifier.fillMaxSize()) {
        SceneBackdrop(current.background, Modifier.fillMaxSize())
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "EXPLORAVIDA",
                style = MaterialTheme.typography.headlineMedium,
                color = LabColors.Lime
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "El gran laboratorio de la vida",
                style = MaterialTheme.typography.labelLarge,
                color = LabColors.Sand
            )

            Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                AnimatedContent(
                    targetState = page,
                    transitionSpec = { fadeIn(tween(260)) togetherWith fadeOut(tween(200)) },
                    label = "onboarding"
                ) { index ->
                    if (index == 1) {
                        VitaOrganism(Modifier.fillMaxSize(), mood = VitaMood.CURIOSA)
                    } else {
                        Illustration(pages[index].illustration, Modifier.size(190.dp))
                    }
                }
            }

            GlassPanel(Modifier.fillMaxWidth(), tint = LabColors.Lime) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        current.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = LabColors.Paper,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        current.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabColors.Sand,
                        textAlign = TextAlign.Center
                    )
                    if (page == pages.lastIndex) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Elige tu insignia de explorador",
                            style = MaterialTheme.typography.labelLarge,
                            color = LabColors.Amber
                        )
                        Spacer(Modifier.height(8.dp))
                        AvatarPicker(avatar) { avatar = it }
                        Spacer(Modifier.height(8.dp))
                        AliasPicker(alias) { alias = it }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            NoraBubble(
                if (page == pages.lastIndex) "Cuando quieras, entramos."
                else "Sigue, que queda poco para empezar.",
                Modifier.fillMaxWidth(),
                compact = true
            )
            Spacer(Modifier.height(10.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                pages.indices.forEach { index ->
                    Box(
                        Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (index == page) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (index == page) LabColors.Lime else LabColors.Locked)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            LabButton(
                text = if (page == pages.lastIndex) "Entrar al laboratorio" else "Siguiente",
                onClick = {
                    if (page == pages.lastIndex) onFinish(alias, avatar) else page += 1
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
