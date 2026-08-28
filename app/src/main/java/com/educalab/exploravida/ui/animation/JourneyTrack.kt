package com.educalab.exploravida.ui.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.educalab.exploravida.ui.theme.LabColors
import kotlin.math.sqrt

/** Una parada del recorrido: color del sistema y estado de visita. */
data class TrackStop(
    val label: String,
    val systemId: String?,
    val visited: Boolean
)

/**
 * Recorrido visual: una particula viaja de parada en parada.
 * El nino puede tocar cualquier parada para volver a leerla.
 * Es la mecanica central de las experiencias de tipo RECORRIDO.
 */
@Composable
fun JourneyTrack(
    stops: List<TrackStop>,
    currentIndex: Int,
    modifier: Modifier = Modifier,
    particleTint: Color = LabColors.Amber,
    onStopTap: (Int) -> Unit = {}
) {
    if (stops.isEmpty()) return
    val safeIndex = currentIndex.coerceIn(0, stops.lastIndex)
    val travel by animateFloatAsState(
        targetValue = safeIndex.toFloat(),
        animationSpec = tween(durationMillis = 700),
        label = "viaje"
    )
    val transition = rememberInfiniteTransition(label = "brillo")
    val pulse by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulso"
    )

    Canvas(
        modifier = modifier.pointerInput(stops.size) {
            detectTapGestures { tap ->
                val positions = stopPositions(stops.size, size.width.toFloat(), size.height.toFloat())
                var best = -1
                var bestDistance = Float.MAX_VALUE
                positions.forEachIndexed { index, position ->
                    val dx = position.x - tap.x
                    val dy = position.y - tap.y
                    val distance = sqrt(dx * dx + dy * dy)
                    if (distance < bestDistance) {
                        bestDistance = distance
                        best = index
                    }
                }
                val touchRadius = minOf(size.width, size.height) * 0.11f
                if (best >= 0 && bestDistance <= touchRadius) onStopTap(best)
            }
        }
    ) {
        val positions = stopPositions(stops.size, size.width, size.height)
        val unitSize = size.minDimension

        // Camino punteado entre paradas
        val road = Path().apply {
            moveTo(positions.first().x, positions.first().y)
            for (index in 1 until positions.size) {
                val previous = positions[index - 1]
                val next = positions[index]
                val midY = (previous.y + next.y) / 2f
                cubicTo(previous.x, midY, next.x, midY, next.x, next.y)
            }
        }
        drawPath(
            road,
            LabColors.Sand.copy(alpha = 0.30f),
            style = Stroke(
                width = unitSize * 0.022f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(unitSize * 0.05f, unitSize * 0.04f))
            )
        )

        // Tramo ya recorrido, iluminado
        if (safeIndex > 0) {
            val done = Path().apply {
                moveTo(positions.first().x, positions.first().y)
                for (index in 1..safeIndex) {
                    val previous = positions[index - 1]
                    val next = positions[index]
                    val midY = (previous.y + next.y) / 2f
                    cubicTo(previous.x, midY, next.x, midY, next.x, next.y)
                }
            }
            drawPath(done, particleTint.copy(alpha = 0.55f), style = Stroke(width = unitSize * 0.020f))
        }

        // Paradas
        positions.forEachIndexed { index, position ->
            val stop = stops[index]
            val tint = stop.systemId?.let { LabColors.ofSystem(it) } ?: LabColors.Sand
            val isCurrent = index == safeIndex
            val radius = unitSize * (if (isCurrent) 0.062f else 0.050f)
            drawCircle(tint.copy(alpha = if (stop.visited || isCurrent) 0.35f else 0.14f), radius * 1.5f, position)
            drawCircle(tint, radius, position, style = Stroke(width = unitSize * 0.012f))
            if (stop.visited) {
                drawCircle(tint, radius * 0.45f, position)
            }
            if (isCurrent) {
                drawCircle(
                    tint.copy(alpha = 0.20f + 0.20f * pulse),
                    radius * (2.0f + 0.4f * pulse),
                    position,
                    style = Stroke(width = unitSize * 0.008f)
                )
            }
            drawFlagNumber(position, index + 1, radius, tint)
        }

        // Particula viajera
        val particle = interpolate(positions, travel)
        drawCircle(particleTint.copy(alpha = 0.30f), unitSize * 0.055f, particle)
        drawCircle(particleTint, unitSize * 0.028f, particle)
        drawCircle(Color.White.copy(alpha = 0.85f), unitSize * 0.010f, Offset(particle.x - unitSize * 0.008f, particle.y - unitSize * 0.008f))
    }
}

private fun DrawScope.drawFlagNumber(center: Offset, number: Int, radius: Float, tint: Color) {
    // Marca de orden dibujada como puntos: legible sin depender del color.
    val dotRadius = radius * 0.16f
    val spacing = radius * 0.45f
    val startX = center.x - spacing * (number - 1) / 2f
    val y = center.y + radius * 1.9f
    repeat(number.coerceAtMost(6)) { index ->
        drawCircle(tint.copy(alpha = 0.8f), dotRadius, Offset(startX + spacing * index, y))
    }
}

/** Posiciones en zigzag. Se usan igual para dibujar y para detectar toques. */
fun stopPositions(count: Int, width: Float, height: Float): List<Offset> {
    if (count <= 0) return emptyList()
    val marginY = height * 0.10f
    val usableHeight = height - marginY * 2f
    return (0 until count).map { index ->
        val ratio = if (count == 1) 0.5f else index.toFloat() / (count - 1)
        val x = when (index % 3) {
            0 -> width * 0.26f
            1 -> width * 0.62f
            else -> width * 0.40f
        }
        Offset(x, marginY + usableHeight * ratio)
    }
}

private fun interpolate(positions: List<Offset>, travel: Float): Offset {
    if (positions.isEmpty()) return Offset.Zero
    val clamped = travel.coerceIn(0f, positions.lastIndex.toFloat())
    val lower = clamped.toInt()
    val upper = minOf(lower + 1, positions.lastIndex)
    val fraction = clamped - lower
    val a = positions[lower]
    val b = positions[upper]
    return Offset(a.x + (b.x - a.x) * fraction, a.y + (b.y - a.y) * fraction)
}

/** Onda de celebracion breve, sin animaciones largas. */
@Composable
fun CelebrationBurst(modifier: Modifier = Modifier, tint: Color = LabColors.Amber) {
    val transition = rememberInfiniteTransition(label = "celebracion")
    val wave by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing), RepeatMode.Restart),
        label = "onda"
    )
    Canvas(modifier = modifier) {
        val middle = Offset(size.width / 2f, size.height / 2f)
        repeat(3) { ring ->
            val phase = (wave + ring * 0.33f) % 1f
            drawCircle(
                tint.copy(alpha = (1f - phase) * 0.45f),
                size.minDimension * 0.18f + size.minDimension * 0.35f * phase,
                middle,
                style = Stroke(width = size.minDimension * 0.02f)
            )
        }
        repeat(8) { index ->
            val angle = Math.PI / 4 * index
            val distance = size.minDimension * (0.12f + 0.30f * wave)
            val x = middle.x + (distance * Math.cos(angle)).toFloat()
            val y = middle.y + (distance * Math.sin(angle)).toFloat()
            drawCircle(tint.copy(alpha = 1f - wave), size.minDimension * 0.028f, Offset(x, y))
        }
    }
}
