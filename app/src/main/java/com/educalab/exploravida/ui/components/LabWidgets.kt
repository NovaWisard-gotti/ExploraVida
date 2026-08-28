package com.educalab.exploravida.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.domain.model.ModuleState
import com.educalab.exploravida.ui.theme.LabColors

/** Nora habla siempre en frases cortas y no interrumpe todo el rato. */
@Composable
fun NoraBubble(
    message: String,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    compact: Boolean = false
) {
    AnimatedVisibility(
        visible = visible && message.isNotBlank(),
        enter = fadeIn(tween(220)) + expandVertically(tween(220)),
        exit = fadeOut(tween(160)) + shrinkVertically(tween(160)),
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(if (compact) 40.dp else 56.dp)
                    .clip(CircleShape)
                    .background(LabColors.GlassSoft)
            ) {
                Illustration(IllustrationKey.NORA, Modifier.fillMaxWidth().padding(4.dp).height(if (compact) 32.dp else 48.dp))
            }
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(18.dp, 18.dp, 18.dp, 4.dp))
                    .background(LabColors.Paper.copy(alpha = 0.94f))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message,
                    color = LabColors.Ink,
                    fontWeight = FontWeight.Medium,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/** Estado del modulo con icono + texto, nunca solo con color. */
@Composable
fun StateChip(state: ModuleState, modifier: Modifier = Modifier) {
    val (label, mark, tint) = when (state) {
        ModuleState.BLOQUEADO -> Triple("Bloqueado", "candado", LabColors.Locked)
        ModuleState.DISPONIBLE -> Triple("Listo", "flecha", LabColors.Sky)
        ModuleState.INICIADO -> Triple("Empezado", "puntos", LabColors.Amber)
        ModuleState.COMPLETADO -> Triple("Completado", "check", LabColors.Lime)
        ModuleState.DOMINADO -> Triple("Dominado", "estrella", LabColors.Violet)
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(tint.copy(alpha = 0.18f))
            .border(1.dp, tint.copy(alpha = 0.7f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(Modifier.size(13.dp)) {
            val s = size.minDimension
            when (mark) {
                "candado" -> {
                    drawRoundRect(tint, topLeft = Offset(s * 0.15f, s * 0.42f), size = Size(s * 0.7f, s * 0.5f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.14f, s * 0.14f))
                    drawArc(tint, 180f, 180f, false, topLeft = Offset(s * 0.26f, s * 0.14f),
                        size = Size(s * 0.48f, s * 0.48f), style = Stroke(width = s * 0.14f))
                }
                "check" -> {
                    val path = Path().apply {
                        moveTo(s * 0.16f, s * 0.52f); lineTo(s * 0.42f, s * 0.78f); lineTo(s * 0.86f, s * 0.20f)
                    }
                    drawPath(path, tint, style = Stroke(width = s * 0.16f))
                }
                "flecha" -> {
                    val path = Path().apply {
                        moveTo(s * 0.28f, s * 0.16f); lineTo(s * 0.80f, s * 0.50f); lineTo(s * 0.28f, s * 0.84f); close()
                    }
                    drawPath(path, tint)
                }
                "puntos" -> {
                    drawCircle(tint, s * 0.11f, Offset(s * 0.24f, s * 0.5f))
                    drawCircle(tint, s * 0.11f, Offset(s * 0.5f, s * 0.5f))
                    drawCircle(tint.copy(alpha = 0.35f), s * 0.11f, Offset(s * 0.76f, s * 0.5f))
                }
                else -> {
                    val star = Path()
                    for (i in 0 until 10) {
                        val angle = Math.PI / 5 * i - Math.PI / 2
                        val radius = if (i % 2 == 0) s * 0.46f else s * 0.20f
                        val x = s / 2 + (radius * Math.cos(angle)).toFloat()
                        val y = s / 2 + (radius * Math.sin(angle)).toFloat()
                        if (i == 0) star.moveTo(x, y) else star.lineTo(x, y)
                    }
                    star.close()
                    drawPath(star, tint)
                }
            }
        }
        Spacer(Modifier.width(6.dp))
        Text(label, color = tint, style = androidx.compose.material3.MaterialTheme.typography.labelSmall)
    }
}

/** Barra de progreso con forma de tubo de laboratorio. */
@Composable
fun ProgressTube(
    progress: Float,
    modifier: Modifier = Modifier,
    tint: Color = LabColors.Lime
) {
    val animated by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(600),
        label = "tubo"
    )
    Canvas(modifier = modifier.height(16.dp)) {
        val radius = size.height / 2f
        drawRoundRect(
            LabColors.Ink.copy(alpha = 0.45f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius)
        )
        if (animated > 0f) {
            drawRoundRect(
                Brush.horizontalGradient(listOf(tint.copy(alpha = 0.75f), tint)),
                size = Size(size.width * animated, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius, radius)
            )
        }
        var bubbleX = radius
        while (bubbleX < size.width * animated) {
            drawCircle(Color.White.copy(alpha = 0.28f), size.height * 0.16f, Offset(bubbleX, size.height * 0.38f))
            bubbleX += size.height * 1.4f
        }
    }
}

/** Ficha circular de un sistema, con su color y su inicial cientifica. */
@Composable
fun SystemGlyph(
    systemId: String,
    label: String,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val tint = LabColors.ofSystem(systemId)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(if (active) 62.dp else 54.dp)) {
                drawCircle(tint.copy(alpha = if (active) 0.35f else 0.16f))
                drawCircle(tint, style = Stroke(width = size.minDimension * 0.06f))
            }
            Illustration(
                key = when (systemId) {
                    "digestivo" -> IllustrationKey.ESTOMAGO
                    "respiratorio" -> IllustrationKey.PULMONES
                    "circulatorio" -> IllustrationKey.CORAZON
                    "movimiento" -> IllustrationKey.MUSCULO
                    "limpieza" -> IllustrationKey.FILTRO
                    else -> IllustrationKey.LUPA
                },
                modifier = Modifier.size(if (active) 32.dp else 27.dp)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            color = if (active) tint else LabColors.Sand.copy(alpha = 0.8f),
            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

/** Insignia ilustrada. Bloqueada se ve como silueta, no solo mas gris. */
@Composable
fun BadgeArt(
    iconKey: String,
    earned: Boolean,
    modifier: Modifier = Modifier
) {
    val tint = if (earned) LabColors.Amber else LabColors.Locked
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxWidth().height(74.dp)) {
            val r = size.minDimension * 0.42f
            val middle = Offset(size.width / 2f, size.height / 2f)
            val hex = Path()
            for (i in 0 until 6) {
                val angle = Math.PI / 3 * i - Math.PI / 2
                val x = middle.x + (r * Math.cos(angle)).toFloat()
                val y = middle.y + (r * Math.sin(angle)).toFloat()
                if (i == 0) hex.moveTo(x, y) else hex.lineTo(x, y)
            }
            hex.close()
            drawPath(hex, tint.copy(alpha = if (earned) 0.30f else 0.12f))
            drawPath(hex, tint, style = Stroke(width = r * 0.13f))
            if (!earned) {
                drawCircle(tint.copy(alpha = 0.5f), r * 0.20f, middle)
            }
        }
        if (earned) {
            Illustration(
                key = when (iconKey) {
                    "ic_lungs" -> IllustrationKey.PULMONES
                    "ic_link" -> IllustrationKey.CAMINOS
                    "ic_lens" -> IllustrationKey.LUPA
                    "ic_muscle" -> IllustrationKey.MUSCULO
                    "ic_flask" -> IllustrationKey.MATRAZ
                    "ic_leaf" -> IllustrationKey.MANZANA
                    "ic_path" -> IllustrationKey.CAMINOS
                    "ic_badge" -> IllustrationKey.VITA_TRANQUILA
                    else -> IllustrationKey.PARTICULA_ENERGIA
                },
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

/** Boton grande del laboratorio: forma de capsula con textura, no un boton plano. */
@Composable
fun LabButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = LabColors.Lime,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.horizontalGradient(
                    if (enabled) listOf(tint.copy(alpha = 0.95f), tint.copy(alpha = 0.72f))
                    else listOf(LabColors.Locked.copy(alpha = 0.5f), LabColors.Locked.copy(alpha = 0.35f))
                )
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 22.dp, vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (enabled) LabColors.Ink else LabColors.Sand.copy(alpha = 0.7f),
            style = androidx.compose.material3.MaterialTheme.typography.labelLarge
        )
    }
}

/** Panel de cristal del laboratorio. Sustituye a las Cards genericas. */
@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    tint: Color = LabColors.Sky,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(LabColors.Glass.copy(alpha = 0.78f))
            .border(1.dp, tint.copy(alpha = 0.35f), RoundedCornerShape(22.dp))
            .padding(14.dp)
    ) { content() }
}

/** Fila de estrellas del resultado de una actividad. */
@Composable
fun StarRow(stars: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(3) { index ->
            Canvas(Modifier.size(20.dp)) {
                val s = size.minDimension
                val path = Path()
                for (i in 0 until 10) {
                    val angle = Math.PI / 5 * i - Math.PI / 2
                    val radius = if (i % 2 == 0) s * 0.48f else s * 0.20f
                    val x = s / 2 + (radius * Math.cos(angle)).toFloat()
                    val y = s / 2 + (radius * Math.sin(angle)).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(path, if (index < stars) LabColors.Amber else LabColors.Locked.copy(alpha = 0.45f))
            }
        }
    }
}
