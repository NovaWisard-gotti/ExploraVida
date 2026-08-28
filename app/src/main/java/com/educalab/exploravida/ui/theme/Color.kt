package com.educalab.exploravida.ui.theme

import androidx.compose.ui.graphics.Color

/** Paleta unica de ExploraVida: laboratorio azul profundo con luz de organismo. */
object LabColors {
    val Deep = Color(0xFF0E2A47)
    val DeepAlt = Color(0xFF11365C)
    val Glass = Color(0xFF123A5E)
    val GlassSoft = Color(0xFF1B4A73)
    val Paper = Color(0xFFFFF7E8)
    val Sand = Color(0xFFF6E7C1)

    val Lime = Color(0xFF7BE0A5)
    val Sky = Color(0xFF6EC6FF)
    val Coral = Color(0xFFFF8A80)
    val Amber = Color(0xFFFFB347)
    val Teal = Color(0xFF67D5C4)
    val Violet = Color(0xFFC79BFF)

    val Ink = Color(0xFF0B1F35)
    val InkSoft = Color(0xFF43607D)
    val Locked = Color(0xFF4A6684)

    fun ofSystem(id: String): Color = when (id) {
        "digestivo" -> Lime
        "respiratorio" -> Sky
        "circulatorio" -> Coral
        "movimiento" -> Amber
        "limpieza" -> Teal
        "relacion" -> Violet
        else -> Sand
    }

    fun parse(hex: String): Color = runCatching {
        Color(android.graphics.Color.parseColor(hex))
    }.getOrDefault(Sand)
}
