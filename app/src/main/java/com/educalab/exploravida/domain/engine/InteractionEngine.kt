package com.educalab.exploravida.domain.engine

import com.educalab.exploravida.domain.model.InteractiveElementModel

/**
 * Controla la exploracion del organismo: que zonas se han tocado,
 * cuales pertenecen a cada sistema y cuando la exploracion esta completa.
 * Tambien protege contra el doble toque accidental.
 */
class InteractionEngine(
    private val elements: List<InteractiveElementModel>,
    private val doubleTapWindowMs: Long = 350L
) {

    sealed class Activation {
        data class Accepted(val element: InteractiveElementModel, val firstTime: Boolean) : Activation()
        data class Ignored(val reason: String) : Activation()
        object Unknown : Activation()
    }

    private val byId = elements.associateBy { it.id }
    private val activated = LinkedHashSet<String>()
    private var lastId: String? = null
    private var lastTime: Long = -1L

    fun activate(elementId: String, timeMs: Long): Activation {
        val element = byId[elementId] ?: return Activation.Unknown
        if (elementId == lastId && lastTime >= 0 && timeMs - lastTime < doubleTapWindowMs) {
            return Activation.Ignored("doble toque")
        }
        lastId = elementId
        lastTime = timeMs
        val firstTime = activated.add(elementId)
        return Activation.Accepted(element, firstTime)
    }

    /** Elemento tocado a partir de una coordenada normalizada (0..1). */
    fun elementAt(x: Float, y: Float): InteractiveElementModel? =
        elements.filter { candidate ->
            val dx = candidate.x - x
            val dy = candidate.y - y
            dx * dx + dy * dy <= candidate.radius * candidate.radius
        }.minByOrNull { candidate ->
            val dx = candidate.x - x
            val dy = candidate.y - y
            dx * dx + dy * dy
        }

    fun activatedIds(): Set<String> = activated.toSet()

    fun activatedCount(): Int = activated.size

    fun elementsOf(systemId: String): List<InteractiveElementModel> =
        elements.filter { it.systemId == systemId }

    fun isSystemFullyExplored(systemId: String): Boolean {
        val ofSystem = elementsOf(systemId)
        return ofSystem.isNotEmpty() && ofSystem.all { it.id in activated }
    }

    fun visitedSystems(): Set<String> =
        activated.mapNotNull { byId[it]?.systemId }.toSet()

    fun progress(): Float =
        if (elements.isEmpty()) 0f else activated.size.toFloat() / elements.size

    fun restore(ids: Collection<String>) {
        activated.clear()
        ids.filter { byId.containsKey(it) }.forEach { activated.add(it) }
    }

    fun reset() {
        activated.clear()
        lastId = null
        lastTime = -1L
    }
}
