package com.educalab.exploravida.ui.components

import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.ui.theme.LabColors
import kotlin.math.sin

/**
 * Diez fondos ilustrados dibujados con Compose Canvas.
 * Ningun recurso viene de Internet: todo se dibuja en el dispositivo.
 */
@Composable
fun SceneBackdrop(kind: SceneBackground, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "fondo")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Restart),
        label = "deriva"
    )
    Canvas(modifier = modifier) {
        when (kind) {
            SceneBackground.LABORATORIO -> lab(drift, LabColors.Deep, LabColors.GlassSoft)
            SceneBackground.LABORATORIO_NOCHE -> lab(drift, Color(0xFF071A2E), Color(0xFF14304C))
            SceneBackground.PRADERA -> meadow(drift)
            SceneBackground.CIELO -> sky(drift)
            SceneBackground.TORRENTE -> stream(drift)
            SceneBackground.CUEVA_ESTOMAGO -> cave(drift)
            SceneBackground.CAMARA_AIRE -> airChamber(drift)
            SceneBackground.CAMPO_MUSCULO -> muscleField(drift)
            SceneBackground.PAPEL_CUADERNO -> paper()
            SceneBackground.SALA_INSIGNIAS -> trophyHall(drift)
        }
    }
}

private fun DrawScope.gradient(top: Color, bottom: Color) {
    drawRect(Brush.verticalGradient(listOf(top, bottom)), size = size)
}

private fun DrawScope.lab(drift: Float, top: Color, bottom: Color) {
    gradient(top, bottom)
    // rejilla del laboratorio
    val step = size.minDimension / 9f
    var x = 0f
    while (x < size.width) {
        drawLine(Color.White.copy(alpha = 0.05f), Offset(x, 0f), Offset(x, size.height), 1f)
        x += step
    }
    var y = 0f
    while (y < size.height) {
        drawLine(Color.White.copy(alpha = 0.05f), Offset(0f, y), Offset(size.width, y), 1f)
        y += step
    }
    // burbujas del tanque
    for (index in 0 until 14) {
        val phase = (drift + index * 0.07f) % 1f
        val bx = size.width * (0.06f + 0.062f * index)
        val by = size.height * (1.05f - phase * 1.1f)
        val radius = (4f + (index % 4) * 3f)
        drawCircle(LabColors.Sky.copy(alpha = 0.10f + 0.05f * (index % 3)), radius, Offset(bx, by))
    }
    // halo central
    drawCircle(
        Brush.radialGradient(
            listOf(LabColors.Lime.copy(alpha = 0.16f), Color.Transparent),
            center = Offset(size.width / 2f, size.height * 0.45f),
            radius = size.minDimension * 0.7f
        ),
        radius = size.minDimension * 0.7f,
        center = Offset(size.width / 2f, size.height * 0.45f)
    )
}

private fun DrawScope.meadow(drift: Float) {
    gradient(Color(0xFF123A5E), Color(0xFF1E5C4E))
    val hill = Path().apply {
        moveTo(0f, size.height * 0.72f)
        cubicTo(size.width * 0.3f, size.height * 0.62f, size.width * 0.7f, size.height * 0.82f, size.width, size.height * 0.66f)
        lineTo(size.width, size.height)
        lineTo(0f, size.height)
        close()
    }
    drawPath(hill, LabColors.Lime.copy(alpha = 0.22f))
    for (index in 0 until 18) {
        val gx = size.width * (index / 18f) + sin((drift + index) * 6.28f) * 4f
        val gh = size.height * (0.05f + 0.03f * (index % 3))
        drawLine(LabColors.Lime.copy(alpha = 0.35f), Offset(gx, size.height), Offset(gx, size.height - gh), 4f)
    }
}

private fun DrawScope.sky(drift: Float) {
    gradient(Color(0xFF17456F), Color(0xFF6EC6FF))
    for (index in 0 until 5) {
        val cx = (size.width * ((index * 0.24f + drift * 0.35f) % 1.2f)) - size.width * 0.1f
        val cy = size.height * (0.12f + 0.11f * index)
        drawCircle(Color.White.copy(alpha = 0.13f), size.minDimension * 0.10f, Offset(cx, cy))
        drawCircle(Color.White.copy(alpha = 0.13f), size.minDimension * 0.075f, Offset(cx + 40f, cy + 8f))
        drawCircle(Color.White.copy(alpha = 0.13f), size.minDimension * 0.065f, Offset(cx - 38f, cy + 10f))
    }
}

private fun DrawScope.stream(drift: Float) {
    gradient(Color(0xFF3B1220), Color(0xFF7E2436))
    for (band in 0 until 6) {
        val path = Path()
        val baseY = size.height * (0.12f + band * 0.16f)
        path.moveTo(0f, baseY)
        var x = 0f
        while (x <= size.width) {
            val y = baseY + sin((x / size.width * 6.28f) + drift * 6.28f + band) * size.height * 0.03f
            path.lineTo(x, y)
            x += 12f
        }
        drawPath(path, LabColors.Coral.copy(alpha = 0.22f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 10f))
    }
}

private fun DrawScope.cave(drift: Float) {
    gradient(Color(0xFF2C1A3E), Color(0xFF5B3A2E))
    for (index in 0 until 12) {
        val phase = (drift * 0.6f + index * 0.09f) % 1f
        val cx = size.width * (0.1f + 0.08f * index)
        val cy = size.height * (0.85f - phase * 0.5f)
        drawCircle(LabColors.Amber.copy(alpha = 0.18f), 10f + (index % 4) * 5f, Offset(cx, cy))
    }
}

private fun DrawScope.airChamber(drift: Float) {
    gradient(Color(0xFF0F3355), Color(0xFF2E7FA8))
    for (index in 0 until 22) {
        val phase = (drift + index * 0.045f) % 1f
        val bx = size.width * ((index * 0.13f) % 1f)
        val by = size.height * (1f - phase)
        drawCircle(Color.White.copy(alpha = 0.14f), 5f + (index % 5) * 2.5f, Offset(bx, by))
    }
}

private fun DrawScope.muscleField(drift: Float) {
    gradient(Color(0xFF4A2708), Color(0xFF8A4B12))
    for (index in 0 until 9) {
        val y = size.height * (0.1f + index * 0.1f)
        val wobble = sin(drift * 6.28f + index) * 8f
        drawLine(
            LabColors.Amber.copy(alpha = 0.25f),
            Offset(0f, y + wobble), Offset(size.width, y - wobble), 9f
        )
    }
}

private fun DrawScope.paper() {
    gradient(LabColors.Paper, LabColors.Sand)
    var y = size.height * 0.08f
    while (y < size.height) {
        drawLine(LabColors.Sky.copy(alpha = 0.28f), Offset(size.width * 0.06f, y), Offset(size.width * 0.94f, y), 2f)
        y += size.height * 0.062f
    }
    drawLine(LabColors.Coral.copy(alpha = 0.45f), Offset(size.width * 0.14f, 0f), Offset(size.width * 0.14f, size.height), 3f)
}

private fun DrawScope.trophyHall(drift: Float) {
    gradient(Color(0xFF14213D), Color(0xFF3E2A5E))
    for (index in 0 until 20) {
        val phase = (drift + index * 0.05f) % 1f
        val cx = size.width * ((index * 0.11f) % 1f)
        val cy = size.height * phase
        val star = 4f + (index % 3) * 3f
        drawCircle(LabColors.Amber.copy(alpha = 0.35f - phase * 0.25f), star, Offset(cx, cy))
    }
    drawRect(
        Brush.radialGradient(
            listOf(LabColors.Amber.copy(alpha = 0.18f), Color.Transparent),
            center = Offset(size.width / 2f, size.height * 0.4f),
            radius = size.minDimension
        ),
        size = Size(size.width, size.height)
    )
}
