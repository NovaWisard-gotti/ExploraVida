package com.educalab.exploravida.domain.engine

import com.educalab.exploravida.domain.model.BodyAction
import com.educalab.exploravida.domain.model.LivingSystemModel
import com.educalab.exploravida.domain.model.SystemLink
import com.educalab.exploravida.domain.model.Systems

/**
 * Motor central de ExploraVida.
 *
 * Sabe que sistemas existen, como se relacionan y que sistemas participan
 * en cada accion de Vita. No conoce nada de Compose ni de Android.
 */
class BodySystemEngine(
    private val systems: List<LivingSystemModel>,
    private val links: List<SystemLink>
) {

    private val byId: Map<String, LivingSystemModel> = systems.associateBy { it.id }

    private val adjacency: Map<String, List<String>> =
        links.groupBy { it.fromSystemId }.mapValues { entry -> entry.value.map { it.toSystemId } }

    fun allSystems(): List<LivingSystemModel> = systems.sortedBy { it.orderIndex }

    fun system(id: String): LivingSystemModel? = byId[id]

    fun exists(id: String): Boolean = byId.containsKey(id)

    fun neighbors(id: String): List<String> = adjacency[id].orEmpty()

    fun isValidLink(from: String, to: String): Boolean =
        links.any { it.fromSystemId == from && it.toSystemId == to }

    fun explanationFor(from: String, to: String): String? =
        links.firstOrNull { it.fromSystemId == from && it.toSystemId == to }?.explanation

    /**
     * Que sistemas participan en una accion. La respuesta siempre es integrada:
     * ninguna accion usa un unico sistema.
     */
    fun systemsInvolved(action: BodyAction): List<String> = when (action) {
        BodyAction.COMER -> listOf(Systems.DIGESTIVO, Systems.CIRCULATORIO, Systems.LIMPIEZA)
        BodyAction.BEBER -> listOf(Systems.DIGESTIVO, Systems.CIRCULATORIO, Systems.LIMPIEZA)
        BodyAction.RESPIRAR -> listOf(Systems.RESPIRATORIO, Systems.CIRCULATORIO)
        BodyAction.MOVERSE -> listOf(Systems.MOVIMIENTO, Systems.CIRCULATORIO, Systems.RESPIRATORIO)
        BodyAction.CORRER -> listOf(
            Systems.MOVIMIENTO, Systems.CIRCULATORIO, Systems.RESPIRATORIO, Systems.DIGESTIVO
        )
        BodyAction.DESCANSAR -> listOf(Systems.CIRCULATORIO, Systems.RESPIRATORIO)
        BodyAction.ELIMINAR -> listOf(Systems.LIMPIEZA, Systems.CIRCULATORIO, Systems.DIGESTIVO)
        BodyAction.PERCIBIR -> listOf(Systems.RELACION, Systems.MOVIMIENTO)
        BodyAction.COMER_Y_CORRER -> Systems.ALL
    }

    /** Ningun sistema trabaja solo: toda accion involucra dos o mas sistemas. */
    fun isIntegrated(action: BodyAction): Boolean = systemsInvolved(action).size >= 2

    /**
     * Recorrido mas corto entre dos sistemas siguiendo relaciones reales.
     * Devuelve lista vacia si no hay camino o si algun sistema no existe.
     */
    fun path(from: String, to: String): List<String> {
        if (!exists(from) || !exists(to)) return emptyList()
        if (from == to) return listOf(from)
        val previous = HashMap<String, String>()
        val visited = HashSet<String>()
        val queue = ArrayDeque<String>()
        queue.add(from)
        visited.add(from)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            for (next in neighbors(current)) {
                if (next in visited) continue
                visited.add(next)
                previous[next] = current
                if (next == to) return rebuild(previous, from, to)
                queue.add(next)
            }
        }
        return emptyList()
    }

    private fun rebuild(previous: Map<String, String>, from: String, to: String): List<String> {
        val result = ArrayList<String>()
        var cursor: String? = to
        while (cursor != null) {
            result.add(cursor)
            if (cursor == from) break
            cursor = previous[cursor]
        }
        return result.reversed()
    }

    /** Cuantas relaciones distintas conoce ya el nino, en tanto por uno. */
    fun coverage(discoveredLinks: Set<Pair<String, String>>): Float {
        if (links.isEmpty()) return 0f
        val valid = discoveredLinks.count { isValidLink(it.first, it.second) }
        return (valid.toFloat() / links.size).coerceIn(0f, 1f)
    }

    fun linksOf(systemId: String): List<SystemLink> =
        links.filter { it.fromSystemId == systemId || it.toSystemId == systemId }

    fun totalLinks(): Int = links.size
}
