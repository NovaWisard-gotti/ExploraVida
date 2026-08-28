package com.educalab.exploravida.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.educalab.exploravida.domain.model.Systems
import com.educalab.exploravida.ui.theme.LabColors
import kotlin.math.sin

/** Estados de animo de Vita. Cambian la postura, no el estilo grafico. */
enum class VitaMood { TRANQUILA, COMIENDO, RESPIRANDO, CORRIENDO, CURIOSA }

/**
 * Vita: el ser vivo del laboratorio.
 *
 * No es una anatomia humana realista. Es una criatura amable y translucida
 * en la que se ven, de forma simplificada, los sistemas que trabajan dentro.
 * Todo se dibuja con Canvas: no hay imagenes descargadas.
 */
@Composable
fun VitaOrganism(
    modifier: Modifier = Modifier,
    mood: VitaMood = VitaMood.TRANQUILA,
    highlightedSystems: Set<String> = emptySet(),
    exploredElements: Set<String> = emptySet(),
    showHotspots: Boolean = false
) {
    val transition = rememberInfiniteTransition(label = "vita")
    val breath by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2600), RepeatMode.Reverse), label = "aliento"
    )
    val beat by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(760), RepeatMode.Reverse), label = "latido"
    )
    val bounce by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(520), RepeatMode.Reverse), label = "salto"
    )

    Canvas(modifier = modifier) {
        drawVita(mood, highlightedSystems, exploredElements, showHotspots, breath, beat, bounce)
    }
}

private fun DrawScope.point(x: Float, y: Float) = Offset(size.width * x, size.height * y)

private fun DrawScope.unit(value: Float) = size.minDimension * value

private fun glow(base: Color, active: Boolean) =
    if (active) base.copy(alpha = 0.95f) else base.copy(alpha = 0.38f)

fun DrawScope.drawVita(
    mood: VitaMood,
    highlighted: Set<String>,
    explored: Set<String>,
    showHotspots: Boolean,
    breath: Float,
    beat: Float,
    bounce: Float
) {
    val lift = when (mood) {
        VitaMood.CORRIENDO -> -unit(0.02f) * bounce
        else -> 0f
    }

    // ---------------------------------------------------------------- cuerpo
    val body = Path().apply {
        moveTo(size.width * 0.5f, size.height * 0.06f + lift)
        cubicTo(
            size.width * 0.90f, size.height * 0.10f + lift,
            size.width * 0.94f, size.height * 0.62f + lift,
            size.width * 0.72f, size.height * 0.88f + lift
        )
        cubicTo(
            size.width * 0.60f, size.height * 0.99f + lift,
            size.width * 0.40f, size.height * 0.99f + lift,
            size.width * 0.28f, size.height * 0.88f + lift
        )
        cubicTo(
            size.width * 0.06f, size.height * 0.62f + lift,
            size.width * 0.10f, size.height * 0.10f + lift,
            size.width * 0.5f, size.height * 0.06f + lift
        )
        close()
    }
    drawPath(body, Color(0xFF9BE8C8).copy(alpha = 0.20f))
    drawPath(body, LabColors.Lime.copy(alpha = 0.55f), style = Stroke(width = unit(0.012f)))

    // brillo del cristal
    drawCircle(
        Color.White.copy(alpha = 0.10f),
        unit(0.16f),
        point(0.33f, 0.24f + lift / size.height)
    )

    // ------------------------------------------------------------- digestivo
    val digestActive = Systems.DIGESTIVO in highlighted
    val digestColor = glow(LabColors.Lime, digestActive)
    drawLine(digestColor, point(0.50f, 0.24f), point(0.47f, 0.38f), unit(0.030f))
    drawOval(
        digestColor.copy(alpha = digestColor.alpha * 0.85f),
        topLeft = point(0.33f, 0.38f), size = Size(unit(0.20f), unit(0.15f))
    )
    val gut = Path().apply {
        moveTo(size.width * 0.44f, size.height * 0.52f)
        cubicTo(
            size.width * 0.66f, size.height * 0.54f,
            size.width * 0.34f, size.height * 0.62f,
            size.width * 0.60f, size.height * 0.66f
        )
    }
    drawPath(gut, digestColor, style = Stroke(width = unit(0.036f)))

    // ----------------------------------------------------------- respiratorio
    val airActive = Systems.RESPIRATORIO in highlighted
    val airColor = glow(LabColors.Sky, airActive)
    val lungGrow = unit(0.008f) * breath
    drawOval(
        airColor,
        topLeft = Offset(point(0.30f, 0.30f).x - lungGrow, point(0.30f, 0.30f).y - lungGrow),
        size = Size(unit(0.15f) + lungGrow * 2, unit(0.19f) + lungGrow * 2)
    )
    drawOval(
        airColor,
        topLeft = Offset(point(0.55f, 0.30f).x - lungGrow, point(0.55f, 0.30f).y - lungGrow),
        size = Size(unit(0.15f) + lungGrow * 2, unit(0.19f) + lungGrow * 2)
    )
    drawLine(airColor, point(0.50f, 0.22f), point(0.50f, 0.32f), unit(0.020f))

    // ----------------------------------------------------------- circulatorio
    val bloodActive = Systems.CIRCULATORIO in highlighted
    val bloodColor = glow(LabColors.Coral, bloodActive)
    val heartScale = 1f + 0.12f * beat
    val heart = Path().apply {
        val cx = size.width * 0.50f
        val cy = size.height * 0.415f
        val r = unit(0.055f) * heartScale
        moveTo(cx, cy + r)
        cubicTo(cx - r * 1.6f, cy - r * 0.4f, cx - r * 0.5f, cy - r * 1.5f, cx, cy - r * 0.5f)
        cubicTo(cx + r * 0.5f, cy - r * 1.5f, cx + r * 1.6f, cy - r * 0.4f, cx, cy + r)
        close()
    }
    drawPath(heart, bloodColor)
    for (side in listOf(-1f, 1f)) {
        val vessel = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.42f)
            cubicTo(
                size.width * (0.50f + side * 0.26f), size.height * 0.30f,
                size.width * (0.50f + side * 0.28f), size.height * 0.66f,
                size.width * (0.50f + side * 0.08f), size.height * 0.80f
            )
        }
        drawPath(vessel, bloodColor.copy(alpha = bloodColor.alpha * 0.75f), style = Stroke(width = unit(0.014f)))
    }

    // ------------------------------------------------------------- movimiento
    val moveActive = Systems.MOVIMIENTO in highlighted
    val moveColor = glow(LabColors.Amber, moveActive)
    val swing = if (mood == VitaMood.CORRIENDO) sin(bounce * 6.28f) * unit(0.03f) else 0f
    drawOval(moveColor, topLeft = point(0.17f, 0.39f), size = Size(unit(0.11f), unit(0.16f)))
    drawOval(moveColor, topLeft = point(0.72f, 0.39f), size = Size(unit(0.11f), unit(0.16f)))
    drawOval(
        moveColor,
        topLeft = Offset(point(0.36f, 0.74f).x, point(0.36f, 0.74f).y + swing),
        size = Size(unit(0.12f), unit(0.19f))
    )
    drawOval(
        moveColor,
        topLeft = Offset(point(0.52f, 0.74f).x, point(0.52f, 0.74f).y - swing),
        size = Size(unit(0.12f), unit(0.19f))
    )

    // --------------------------------------------------------------- limpieza
    val cleanActive = Systems.LIMPIEZA in highlighted
    val cleanColor = glow(LabColors.Teal, cleanActive)
    drawOval(cleanColor, topLeft = point(0.33f, 0.59f), size = Size(unit(0.09f), unit(0.11f)))
    drawOval(cleanColor, topLeft = point(0.58f, 0.59f), size = Size(unit(0.09f), unit(0.11f)))
    drawCircle(cleanColor.copy(alpha = cleanColor.alpha * 0.8f), unit(0.03f), point(0.50f, 0.885f))

    // --------------------------------------------------------------- relacion
    val senseActive = Systems.RELACION in highlighted
    val senseColor = glow(LabColors.Violet, senseActive)
    drawLine(senseColor, point(0.50f, 0.135f), point(0.50f, 0.055f), unit(0.012f))
    drawCircle(LabColors.Amber, unit(0.028f), point(0.50f, 0.048f))
    drawCircle(senseColor.copy(alpha = senseColor.alpha * 0.6f), unit(0.045f), point(0.345f, 0.145f))
    drawCircle(senseColor.copy(alpha = senseColor.alpha * 0.35f), unit(0.05f), point(0.78f, 0.60f))

    // ------------------------------------------------------------------ cara
    val eyeOpen = if (mood == VitaMood.RESPIRANDO) 0.75f + 0.25f * breath else 1f
    drawOval(
        LabColors.Ink,
        topLeft = point(0.415f, 0.145f),
        size = Size(unit(0.055f), unit(0.062f) * eyeOpen)
    )
    drawOval(
        LabColors.Ink,
        topLeft = point(0.535f, 0.145f),
        size = Size(unit(0.055f), unit(0.062f) * eyeOpen)
    )
    drawCircle(Color.White, unit(0.014f), point(0.437f, 0.163f))
    drawCircle(Color.White, unit(0.014f), point(0.557f, 0.163f))

    val mouth = Path()
    when (mood) {
        VitaMood.COMIENDO -> drawCircle(LabColors.Ink.copy(alpha = 0.85f), unit(0.036f), point(0.50f, 0.235f))
        VitaMood.CURIOSA -> {
            mouth.moveTo(size.width * 0.46f, size.height * 0.235f)
            mouth.lineTo(size.width * 0.54f, size.height * 0.235f)
            drawPath(mouth, LabColors.Ink, style = Stroke(width = unit(0.012f)))
        }
        else -> {
            mouth.moveTo(size.width * 0.455f, size.height * 0.228f)
            mouth.quadraticBezierTo(
                size.width * 0.50f, size.height * 0.258f,
                size.width * 0.545f, size.height * 0.228f
            )
            drawPath(mouth, LabColors.Ink, style = Stroke(width = unit(0.013f)))
        }
    }

    // ------------------------------------------------------- zonas explorables
    if (showHotspots) {
        val hotspots = listOf(
            HotSpot("el_boca", 0.500f, 0.235f),
            HotSpot("el_tubo", 0.500f, 0.325f),
            HotSpot("el_estomago", 0.410f, 0.440f),
            HotSpot("el_intestino", 0.500f, 0.600f),
            HotSpot("el_gusto", 0.560f, 0.265f),
            HotSpot("el_nariz", 0.575f, 0.195f),
            HotSpot("el_via_aire", 0.500f, 0.290f),
            HotSpot("el_pulmon_izq", 0.375f, 0.365f),
            HotSpot("el_pulmon_der", 0.625f, 0.365f),
            HotSpot("el_corazon", 0.500f, 0.415f),
            HotSpot("el_camino_alto", 0.295f, 0.300f),
            HotSpot("el_camino_bajo", 0.695f, 0.545f),
            HotSpot("el_red_fina", 0.500f, 0.715f),
            HotSpot("el_vaso_pequeno", 0.725f, 0.300f),
            HotSpot("el_brazo_izq", 0.235f, 0.440f),
            HotSpot("el_brazo_der", 0.765f, 0.440f),
            HotSpot("el_pierna_izq", 0.420f, 0.815f),
            HotSpot("el_pierna_der", 0.580f, 0.815f),
            HotSpot("el_soporte", 0.500f, 0.675f),
            HotSpot("el_articulacion", 0.300f, 0.715f),
            HotSpot("el_filtro_izq", 0.375f, 0.625f),
            HotSpot("el_filtro_der", 0.625f, 0.625f),
            HotSpot("el_salida", 0.500f, 0.885f),
            HotSpot("el_poros", 0.285f, 0.575f),
            HotSpot("el_ojos", 0.445f, 0.155f),
            HotSpot("el_oido", 0.345f, 0.145f),
            HotSpot("el_antena", 0.500f, 0.075f),
            HotSpot("el_piel", 0.775f, 0.600f),
            HotSpot("el_tacto", 0.800f, 0.720f),
            HotSpot("el_centro", 0.500f, 0.130f),
        )
        hotspots.forEachIndexed { index, spot ->
            val center = point(spot.x, spot.y)
            val seen = spot.id in explored
            val pulse = unit(0.004f) * sin((breath * 6.28f) + index)
            drawCircle(
                if (seen) LabColors.Lime.copy(alpha = 0.30f) else Color.White.copy(alpha = 0.16f),
                unit(0.030f) + pulse,
                center
            )
            drawCircle(
                if (seen) LabColors.Lime else Color.White.copy(alpha = 0.60f),
                unit(0.010f),
                center
            )
        }
    }
}

/** Zona tocable de Vita, en coordenadas 0..1. */
data class HotSpot(val id: String, val x: Float, val y: Float)

/** Version pequena de Vita para cabeceras y tarjetas. */
@Composable
fun VitaBadgeMark(modifier: Modifier = Modifier, tint: Color = LabColors.Lime) {
    Canvas(modifier = modifier) {
        val r = size.minDimension / 2f
        val middle = Offset(size.width / 2f, size.height / 2f)
        drawCircle(tint.copy(alpha = 0.20f), r, middle)
        drawCircle(tint, r * 0.62f, middle, style = Stroke(width = r * 0.14f))
        drawCircle(LabColors.Amber, r * 0.16f, Offset(middle.x, middle.y - r * 0.20f))
        drawArc(
            color = LabColors.Paper,
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(middle.x - r * 0.40f, middle.y - r * 0.25f),
            size = Size(r * 0.80f, r * 0.60f),
            style = Stroke(width = r * 0.10f)
        )
    }
}
