package com.educalab.exploravida.util

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Sonido y vibracion locales.
 *
 * Los efectos se generan en el propio dispositivo con ToneGenerator:
 * no hay archivos de audio descargados ni reproduccion automatica fuerte.
 * Todo puede silenciarse desde Ajustes.
 */
open class FeedbackController(
    var soundEnabled: Boolean = true,
    var hapticsEnabled: Boolean = true
) {

    private var generator: ToneGenerator? = null

    private fun tone(): ToneGenerator? {
        if (!soundEnabled) return null
        if (generator == null) {
            generator = runCatching {
                ToneGenerator(AudioManager.STREAM_MUSIC, VOLUME)
            }.getOrNull()
        }
        return generator
    }

    open fun tapSound() = play(ToneGenerator.TONE_PROP_BEEP, 60)

    open fun successSound() = play(ToneGenerator.TONE_PROP_ACK, 150)

    open fun errorSound() = play(ToneGenerator.TONE_PROP_NACK, 140)

    open fun unlockSound() = play(ToneGenerator.TONE_PROP_BEEP2, 200)

    private fun play(type: Int, durationMs: Int) {
        if (!soundEnabled) return
        runCatching { tone()?.startTone(type, durationMs) }
    }

    open fun release() {
        runCatching { generator?.release() }
        generator = null
    }

    companion object {
        private const val VOLUME = 55

        /** Controlador vacio para vistas previas y pruebas. */
        val Silent = object : FeedbackController(soundEnabled = false, hapticsEnabled = false) {
            override fun tapSound() = Unit
            override fun successSound() = Unit
            override fun errorSound() = Unit
            override fun unlockSound() = Unit
            override fun release() = Unit
        }
    }
}

val LocalFeedback = staticCompositionLocalOf { FeedbackController.Silent }
