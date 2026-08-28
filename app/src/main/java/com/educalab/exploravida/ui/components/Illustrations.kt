package com.educalab.exploravida.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.ui.theme.LabColors

/**
 * Atlas de 30 ilustraciones del mismo universo grafico, dibujadas con Canvas.
 * Se reutilizan en tarjetas, recorridos, cuaderno y actividades.
 */
@Composable
fun Illustration(key: String, modifier: Modifier = Modifier) {
    val resolved = runCatching { IllustrationKey.valueOf(key) }
        .getOrDefault(IllustrationKey.VITA_TRANQUILA)
    Canvas(modifier = modifier) { drawIllustration(resolved) }
}

@Composable
fun Illustration(key: IllustrationKey, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) { drawIllustration(key) }
}

private fun DrawScope.p(x: Float, y: Float) = Offset(size.width * x, size.height * y)
private fun DrawScope.u(v: Float) = size.minDimension * v

fun DrawScope.drawIllustration(key: IllustrationKey) {
    when (key) {
        IllustrationKey.VITA_TRANQUILA -> creature(LabColors.Lime, smile = true)
        IllustrationKey.VITA_COMIENDO -> creature(LabColors.Lime, openMouth = true)
        IllustrationKey.VITA_RESPIRANDO -> creature(LabColors.Sky, smile = true)
        IllustrationKey.VITA_CORRIENDO -> creature(LabColors.Amber, running = true)
        IllustrationKey.VITA_CURIOSA -> creature(LabColors.Violet, curious = true)
        IllustrationKey.NORA -> nora()
        IllustrationKey.MANZANA -> apple()
        IllustrationKey.PAN -> bread()
        IllustrationKey.ZANAHORIA -> carrot()
        IllustrationKey.VASO_AGUA -> glassOfWater()
        IllustrationKey.LUPA -> magnifier()
        IllustrationKey.MATRAZ -> flask()
        IllustrationKey.MICROSCOPIO -> microscope()
        IllustrationKey.CUADERNO -> notebook()
        IllustrationKey.PROBETA -> testTube()
        IllustrationKey.PARTICULA_ENERGIA -> spark(LabColors.Amber)
        IllustrationKey.BURBUJA_OXIGENO -> bubble(LabColors.Sky)
        IllustrationKey.NUTRIENTE -> nutrient()
        IllustrationKey.DESECHO -> waste()
        IllustrationKey.GOTA -> drop(LabColors.Teal)
        IllustrationKey.BOCA -> mouth()
        IllustrationKey.TUBO -> tube()
        IllustrationKey.ESTOMAGO -> stomach()
        IllustrationKey.INTESTINO -> gut()
        IllustrationKey.NARIZ -> nose()
        IllustrationKey.PULMONES -> lungs()
        IllustrationKey.CORAZON -> heart()
        IllustrationKey.CAMINOS -> vessels()
        IllustrationKey.MUSCULO -> muscle()
        IllustrationKey.FILTRO -> filter()
    }
}

// --------------------------------------------------------------- personajes

private fun DrawScope.creature(
    tint: Color,
    smile: Boolean = false,
    openMouth: Boolean = false,
    running: Boolean = false,
    curious: Boolean = false
) {
    val body = Path().apply {
        moveTo(size.width * 0.5f, size.height * 0.12f)
        cubicTo(size.width * 0.92f, size.height * 0.18f, size.width * 0.90f, size.height * 0.78f, size.width * 0.5f, size.height * 0.92f)
        cubicTo(size.width * 0.10f, size.height * 0.78f, size.width * 0.08f, size.height * 0.18f, size.width * 0.5f, size.height * 0.12f)
        close()
    }
    drawPath(body, tint.copy(alpha = 0.30f))
    drawPath(body, tint, style = Stroke(width = u(0.05f)))
    drawLine(tint, p(0.5f, 0.12f), p(0.5f, 0.03f), u(0.04f))
    drawCircle(LabColors.Amber, u(0.07f), p(0.5f, 0.03f))
    val eyeY = if (curious) 0.36f else 0.40f
    drawCircle(LabColors.Ink, u(0.075f), p(0.38f, eyeY))
    drawCircle(LabColors.Ink, u(0.075f), p(0.62f, eyeY))
    drawCircle(Color.White, u(0.026f), p(0.40f, eyeY - 0.03f))
    drawCircle(Color.White, u(0.026f), p(0.64f, eyeY - 0.03f))
    when {
        openMouth -> drawCircle(LabColors.Ink, u(0.08f), p(0.5f, 0.62f))
        smile -> {
            val m = Path().apply {
                moveTo(size.width * 0.40f, size.height * 0.58f)
                quadraticBezierTo(size.width * 0.50f, size.height * 0.70f, size.width * 0.60f, size.height * 0.58f)
            }
            drawPath(m, LabColors.Ink, style = Stroke(width = u(0.045f)))
        }
        else -> drawLine(LabColors.Ink, p(0.42f, 0.62f), p(0.58f, 0.62f), u(0.045f))
    }
    if (running) {
        drawLine(tint.copy(alpha = 0.7f), p(0.06f, 0.42f), p(0.20f, 0.42f), u(0.035f))
        drawLine(tint.copy(alpha = 0.5f), p(0.04f, 0.56f), p(0.18f, 0.56f), u(0.03f))
    }
}

private fun DrawScope.nora() {
    // Sombrero de exploradora
    drawOval(LabColors.Amber, topLeft = p(0.14f, 0.16f), size = Size(u(0.72f), u(0.14f)))
    drawOval(LabColors.Amber, topLeft = p(0.28f, 0.05f), size = Size(u(0.44f), u(0.20f)))
    // Cara
    drawCircle(Color(0xFFF3C9A0), u(0.24f), p(0.50f, 0.48f))
    drawCircle(LabColors.Ink, u(0.035f), p(0.42f, 0.45f))
    drawCircle(LabColors.Ink, u(0.035f), p(0.58f, 0.45f))
    val smile = Path().apply {
        moveTo(size.width * 0.42f, size.height * 0.56f)
        quadraticBezierTo(size.width * 0.50f, size.height * 0.64f, size.width * 0.58f, size.height * 0.56f)
    }
    drawPath(smile, LabColors.Ink, style = Stroke(width = u(0.03f)))
    // Bata y lupa
    drawRoundRect(
        LabColors.Paper,
        topLeft = p(0.24f, 0.70f),
        size = Size(u(0.52f), u(0.30f)),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(u(0.08f), u(0.08f))
    )
    drawCircle(LabColors.Sky, u(0.10f), p(0.76f, 0.78f), style = Stroke(width = u(0.035f)))
    drawLine(LabColors.Sky, p(0.83f, 0.85f), p(0.92f, 0.94f), u(0.035f))
}

// --------------------------------------------------------------- alimentos

private fun DrawScope.apple() {
    drawCircle(LabColors.Coral, u(0.32f), p(0.40f, 0.60f))
    drawCircle(LabColors.Coral, u(0.32f), p(0.60f, 0.60f))
    drawLine(Color(0xFF7A4B2A), p(0.50f, 0.32f), p(0.52f, 0.12f), u(0.05f))
    val leaf = Path().apply {
        moveTo(size.width * 0.52f, size.height * 0.18f)
        quadraticBezierTo(size.width * 0.78f, size.height * 0.06f, size.width * 0.72f, size.height * 0.26f)
        quadraticBezierTo(size.width * 0.60f, size.height * 0.30f, size.width * 0.52f, size.height * 0.18f)
        close()
    }
    drawPath(leaf, LabColors.Lime)
}

private fun DrawScope.bread() {
    drawRoundRect(
        Color(0xFFD9A05B), topLeft = p(0.12f, 0.30f), size = Size(u(0.76f), u(0.44f)),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(u(0.22f), u(0.22f))
    )
    for (i in 0 until 3) {
        drawLine(Color(0xFFB57C3A), p(0.26f + i * 0.18f, 0.36f), p(0.32f + i * 0.18f, 0.50f), u(0.05f))
    }
}

private fun DrawScope.carrot() {
    val body = Path().apply {
        moveTo(size.width * 0.50f, size.height * 0.94f)
        lineTo(size.width * 0.30f, size.height * 0.34f)
        lineTo(size.width * 0.70f, size.height * 0.34f)
        close()
    }
    drawPath(body, LabColors.Amber)
    drawLine(LabColors.Lime, p(0.50f, 0.34f), p(0.36f, 0.10f), u(0.06f))
    drawLine(LabColors.Lime, p(0.50f, 0.34f), p(0.50f, 0.06f), u(0.06f))
    drawLine(LabColors.Lime, p(0.50f, 0.34f), p(0.66f, 0.12f), u(0.06f))
}

private fun DrawScope.glassOfWater() {
    val glass = Path().apply {
        moveTo(size.width * 0.28f, size.height * 0.22f)
        lineTo(size.width * 0.72f, size.height * 0.22f)
        lineTo(size.width * 0.64f, size.height * 0.90f)
        lineTo(size.width * 0.36f, size.height * 0.90f)
        close()
    }
    drawPath(glass, LabColors.Sky.copy(alpha = 0.35f))
    drawPath(glass, LabColors.Paper, style = Stroke(width = u(0.045f)))
    drawRect(LabColors.Sky.copy(alpha = 0.75f), topLeft = p(0.33f, 0.48f), size = Size(u(0.34f), u(0.40f)))
}

// ------------------------------------------------------- objetos cientificos

private fun DrawScope.magnifier() {
    drawCircle(LabColors.Sky.copy(alpha = 0.30f), u(0.30f), p(0.42f, 0.42f))
    drawCircle(LabColors.Paper, u(0.30f), p(0.42f, 0.42f), style = Stroke(width = u(0.06f)))
    drawLine(LabColors.Amber, p(0.62f, 0.62f), p(0.90f, 0.90f), u(0.09f))
}

private fun DrawScope.flask() {
    val body = Path().apply {
        moveTo(size.width * 0.40f, size.height * 0.12f)
        lineTo(size.width * 0.40f, size.height * 0.44f)
        lineTo(size.width * 0.16f, size.height * 0.86f)
        lineTo(size.width * 0.84f, size.height * 0.86f)
        lineTo(size.width * 0.60f, size.height * 0.44f)
        lineTo(size.width * 0.60f, size.height * 0.12f)
        close()
    }
    drawPath(body, LabColors.Lime.copy(alpha = 0.35f))
    drawPath(body, LabColors.Paper, style = Stroke(width = u(0.05f)))
    drawCircle(LabColors.Lime, u(0.05f), p(0.45f, 0.72f))
    drawCircle(LabColors.Lime, u(0.04f), p(0.60f, 0.66f))
}

private fun DrawScope.microscope() {
    drawRoundRect(LabColors.Paper, topLeft = p(0.18f, 0.80f), size = Size(u(0.64f), u(0.12f)),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(u(0.05f), u(0.05f)))
    drawLine(LabColors.Sky, p(0.40f, 0.80f), p(0.56f, 0.30f), u(0.10f))
    drawLine(LabColors.Amber, p(0.56f, 0.30f), p(0.34f, 0.22f), u(0.09f))
    drawRect(LabColors.Paper, topLeft = p(0.28f, 0.62f), size = Size(u(0.40f), u(0.05f)))
}

private fun DrawScope.notebook() {
    drawRoundRect(LabColors.Paper, topLeft = p(0.16f, 0.12f), size = Size(u(0.68f), u(0.76f)),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(u(0.06f), u(0.06f)))
    drawRect(LabColors.Coral, topLeft = p(0.16f, 0.12f), size = Size(u(0.08f), u(0.76f)))
    for (i in 0 until 4) {
        drawLine(LabColors.Sky, p(0.32f, 0.28f + i * 0.14f), p(0.76f, 0.28f + i * 0.14f), u(0.035f))
    }
}

private fun DrawScope.testTube() {
    drawRoundRect(LabColors.Paper, topLeft = p(0.38f, 0.10f), size = Size(u(0.24f), u(0.80f)),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(u(0.12f), u(0.12f)),
        style = Stroke(width = u(0.05f)))
    drawRoundRect(LabColors.Violet.copy(alpha = 0.7f), topLeft = p(0.40f, 0.50f), size = Size(u(0.20f), u(0.38f)),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(u(0.10f), u(0.10f)))
}

// ------------------------------------------------------------- particulas

private fun DrawScope.spark(tint: Color) {
    val star = Path()
    val cx = size.width / 2f
    val cy = size.height / 2f
    val outer = u(0.44f)
    val inner = u(0.18f)
    for (i in 0 until 8) {
        val angle = (Math.PI / 4.0) * i
        val radius = if (i % 2 == 0) outer else inner
        val x = cx + (radius * Math.cos(angle)).toFloat()
        val y = cy + (radius * Math.sin(angle)).toFloat()
        if (i == 0) star.moveTo(x, y) else star.lineTo(x, y)
    }
    star.close()
    drawPath(star, tint)
    drawCircle(Color.White.copy(alpha = 0.7f), u(0.09f), Offset(cx, cy))
}

private fun DrawScope.bubble(tint: Color) {
    drawCircle(tint.copy(alpha = 0.45f), u(0.40f))
    drawCircle(tint, u(0.40f), style = Stroke(width = u(0.05f)))
    drawCircle(Color.White.copy(alpha = 0.8f), u(0.09f), p(0.36f, 0.34f))
}

private fun DrawScope.nutrient() {
    drawCircle(LabColors.Lime, u(0.22f), p(0.36f, 0.40f))
    drawCircle(LabColors.Amber, u(0.18f), p(0.64f, 0.52f))
    drawCircle(LabColors.Sky, u(0.15f), p(0.44f, 0.70f))
}

private fun DrawScope.waste() {
    drawCircle(Color(0xFF8A8172), u(0.26f), p(0.42f, 0.52f))
    drawCircle(Color(0xFF6F6759), u(0.18f), p(0.66f, 0.62f))
    drawCircle(Color(0xFF9C9384), u(0.12f), p(0.36f, 0.74f))
}

private fun DrawScope.drop(tint: Color) {
    val path = Path().apply {
        moveTo(size.width * 0.5f, size.height * 0.10f)
        cubicTo(size.width * 0.90f, size.height * 0.52f, size.width * 0.78f, size.height * 0.92f, size.width * 0.5f, size.height * 0.92f)
        cubicTo(size.width * 0.22f, size.height * 0.92f, size.width * 0.10f, size.height * 0.52f, size.width * 0.5f, size.height * 0.10f)
        close()
    }
    drawPath(path, tint.copy(alpha = 0.65f))
    drawPath(path, tint, style = Stroke(width = u(0.05f)))
}

// ----------------------------------------------------------- partes de Vita

private fun DrawScope.mouth() {
    drawArc(LabColors.Coral, 0f, 180f, true, topLeft = p(0.14f, 0.30f), size = Size(u(0.72f), u(0.46f)))
    drawArc(LabColors.Paper, 0f, 180f, false, topLeft = p(0.14f, 0.30f), size = Size(u(0.72f), u(0.46f)),
        style = Stroke(width = u(0.05f)))
    for (i in 0 until 4) {
        drawRect(LabColors.Paper, topLeft = p(0.22f + i * 0.16f, 0.30f), size = Size(u(0.10f), u(0.12f)))
    }
}

private fun DrawScope.tube() {
    drawLine(LabColors.Lime.copy(alpha = 0.5f), p(0.5f, 0.06f), p(0.5f, 0.94f), u(0.30f))
    drawLine(LabColors.Lime, p(0.35f, 0.06f), p(0.35f, 0.94f), u(0.04f))
    drawLine(LabColors.Lime, p(0.65f, 0.06f), p(0.65f, 0.94f), u(0.04f))
}

private fun DrawScope.stomach() {
    val path = Path().apply {
        moveTo(size.width * 0.34f, size.height * 0.16f)
        cubicTo(size.width * 0.94f, size.height * 0.22f, size.width * 0.90f, size.height * 0.86f, size.width * 0.42f, size.height * 0.84f)
        cubicTo(size.width * 0.14f, size.height * 0.80f, size.width * 0.16f, size.height * 0.30f, size.width * 0.34f, size.height * 0.16f)
        close()
    }
    drawPath(path, LabColors.Lime.copy(alpha = 0.45f))
    drawPath(path, LabColors.Lime, style = Stroke(width = u(0.05f)))
    drawCircle(LabColors.Amber, u(0.07f), p(0.50f, 0.56f))
    drawCircle(LabColors.Amber, u(0.05f), p(0.64f, 0.44f))
}

private fun DrawScope.gut() {
    val path = Path().apply {
        moveTo(size.width * 0.14f, size.height * 0.30f)
        cubicTo(size.width * 0.92f, size.height * 0.20f, size.width * 0.10f, size.height * 0.54f, size.width * 0.88f, size.height * 0.52f)
        cubicTo(size.width * 0.14f, size.height * 0.62f, size.width * 0.92f, size.height * 0.86f, size.width * 0.30f, size.height * 0.84f)
    }
    drawPath(path, LabColors.Lime, style = Stroke(width = u(0.11f)))
}

private fun DrawScope.nose() {
    val path = Path().apply {
        moveTo(size.width * 0.50f, size.height * 0.16f)
        lineTo(size.width * 0.74f, size.height * 0.74f)
        lineTo(size.width * 0.26f, size.height * 0.74f)
        close()
    }
    drawPath(path, LabColors.Sky.copy(alpha = 0.55f))
    drawPath(path, LabColors.Sky, style = Stroke(width = u(0.05f)))
    drawCircle(LabColors.Ink, u(0.06f), p(0.40f, 0.66f))
    drawCircle(LabColors.Ink, u(0.06f), p(0.60f, 0.66f))
}

private fun DrawScope.lungs() {
    drawLine(LabColors.Sky, p(0.5f, 0.06f), p(0.5f, 0.36f), u(0.07f))
    drawOval(LabColors.Sky.copy(alpha = 0.55f), topLeft = p(0.10f, 0.30f), size = Size(u(0.34f), u(0.58f)))
    drawOval(LabColors.Sky.copy(alpha = 0.55f), topLeft = p(0.56f, 0.30f), size = Size(u(0.34f), u(0.58f)))
    drawOval(LabColors.Sky, topLeft = p(0.10f, 0.30f), size = Size(u(0.34f), u(0.58f)), style = Stroke(width = u(0.04f)))
    drawOval(LabColors.Sky, topLeft = p(0.56f, 0.30f), size = Size(u(0.34f), u(0.58f)), style = Stroke(width = u(0.04f)))
}

private fun DrawScope.heart() {
    val path = Path().apply {
        moveTo(size.width * 0.5f, size.height * 0.88f)
        cubicTo(size.width * -0.05f, size.height * 0.50f, size.width * 0.18f, size.height * 0.06f, size.width * 0.5f, size.height * 0.32f)
        cubicTo(size.width * 0.82f, size.height * 0.06f, size.width * 1.05f, size.height * 0.50f, size.width * 0.5f, size.height * 0.88f)
        close()
    }
    drawPath(path, LabColors.Coral)
    drawPath(path, LabColors.Paper.copy(alpha = 0.5f), style = Stroke(width = u(0.04f)))
}

private fun DrawScope.vessels() {
    drawLine(LabColors.Coral, p(0.10f, 0.30f), p(0.90f, 0.30f), u(0.08f))
    drawLine(LabColors.Sky, p(0.10f, 0.70f), p(0.90f, 0.70f), u(0.08f))
    for (i in 0 until 4) {
        drawLine(LabColors.Coral.copy(alpha = 0.6f), p(0.22f + i * 0.19f, 0.30f), p(0.22f + i * 0.19f, 0.70f), u(0.035f))
    }
}

private fun DrawScope.muscle() {
    val path = Path().apply {
        moveTo(size.width * 0.10f, size.height * 0.50f)
        cubicTo(size.width * 0.30f, size.height * 0.10f, size.width * 0.70f, size.height * 0.10f, size.width * 0.90f, size.height * 0.50f)
        cubicTo(size.width * 0.70f, size.height * 0.90f, size.width * 0.30f, size.height * 0.90f, size.width * 0.10f, size.height * 0.50f)
        close()
    }
    drawPath(path, LabColors.Amber.copy(alpha = 0.6f))
    drawPath(path, LabColors.Amber, style = Stroke(width = u(0.05f)))
    drawLine(LabColors.Paper.copy(alpha = 0.6f), p(0.30f, 0.42f), p(0.70f, 0.42f), u(0.03f))
    drawLine(LabColors.Paper.copy(alpha = 0.6f), p(0.30f, 0.58f), p(0.70f, 0.58f), u(0.03f))
}

private fun DrawScope.filter() {
    val path = Path().apply {
        moveTo(size.width * 0.20f, size.height * 0.18f)
        lineTo(size.width * 0.80f, size.height * 0.18f)
        lineTo(size.width * 0.56f, size.height * 0.62f)
        lineTo(size.width * 0.56f, size.height * 0.90f)
        lineTo(size.width * 0.44f, size.height * 0.90f)
        lineTo(size.width * 0.44f, size.height * 0.62f)
        close()
    }
    drawPath(path, LabColors.Teal.copy(alpha = 0.55f))
    drawPath(path, LabColors.Teal, style = Stroke(width = u(0.05f)))
    drawCircle(LabColors.Paper.copy(alpha = 0.8f), u(0.05f), p(0.42f, 0.32f))
    drawCircle(LabColors.Paper.copy(alpha = 0.6f), u(0.04f), p(0.60f, 0.36f))
}

/**
 * Dibuja una ilustracion dentro de un cuadrado del lienzo actual.
 * Sirve para colocar varias ilustraciones en un mismo Canvas.
 */
fun DrawScope.drawIllustrationIn(
    key: IllustrationKey,
    topLeft: Offset,
    boxSize: Float
) {
    if (size.width <= 0f || size.height <= 0f) return
    withTransform({
        translate(topLeft.x, topLeft.y)
        scale(
            scaleX = boxSize / size.width,
            scaleY = boxSize / size.height,
            pivot = Offset.Zero
        )
    }) {
        drawIllustration(key)
    }
}

/** Traduce la clave de icono usada en los datos a una ilustracion del atlas. */
fun illustrationForIcon(iconKey: String): IllustrationKey = when (iconKey) {
    "ic_leaf" -> IllustrationKey.MANZANA
    "ic_drop" -> IllustrationKey.GOTA
    "ic_lungs" -> IllustrationKey.PULMONES
    "ic_spark" -> IllustrationKey.PARTICULA_ENERGIA
    "ic_lens" -> IllustrationKey.LUPA
    "ic_flask" -> IllustrationKey.MATRAZ
    "ic_muscle" -> IllustrationKey.MUSCULO
    "ic_heartbeat" -> IllustrationKey.CORAZON
    "ic_notebook" -> IllustrationKey.CUADERNO
    "ic_badge" -> IllustrationKey.PROBETA
    "ic_link" -> IllustrationKey.CAMINOS
    "ic_path" -> IllustrationKey.INTESTINO
    else -> runCatching { IllustrationKey.valueOf(iconKey) }.getOrDefault(IllustrationKey.LUPA)
}
