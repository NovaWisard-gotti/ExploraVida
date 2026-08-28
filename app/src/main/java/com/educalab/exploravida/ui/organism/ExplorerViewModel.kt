package com.educalab.exploravida.ui.organism

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.exploravida.data.repository.ExploraVidaRepository
import com.educalab.exploravida.domain.engine.InteractionEngine
import com.educalab.exploravida.domain.model.BadgeModel
import com.educalab.exploravida.domain.model.InteractiveElementModel
import com.educalab.exploravida.domain.model.LivingSystemModel
import com.educalab.exploravida.domain.usecase.DiscoveryMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExplorerUiState(
    val loading: Boolean = true,
    val systems: List<LivingSystemModel> = emptyList(),
    val elements: List<InteractiveElementModel> = emptyList(),
    val explored: Set<String> = emptySet(),
    val selected: InteractiveElementModel? = null,
    val selectedSystemName: String = "",
    val message: String = "Toca una zona iluminada de Vita.",
    val progress: Float = 0f,
    val newBadges: List<BadgeModel> = emptyList()
)

/** Modo Exploracion: tocar el organismo y descubrir que hace cada zona. */
class ExplorerViewModel(private val repository: ExploraVidaRepository) : ViewModel() {

    private val internalState = MutableStateFlow(ExplorerUiState())
    val state: StateFlow<ExplorerUiState> = internalState.asStateFlow()

    private var engine: InteractionEngine? = null
    private var loaded = false

    fun load() {
        if (loaded) return
        loaded = true
        viewModelScope.launch {
            val elements = repository.elements()
            val already = repository.exploredElementIds()
            val created = InteractionEngine(elements)
            created.restore(already)
            engine = created
            internalState.value = ExplorerUiState(
                loading = false,
                systems = repository.systems(),
                elements = elements,
                explored = created.activatedIds(),
                progress = created.progress()
            )
        }
    }

    /** Toque sobre Vita en coordenadas normalizadas (0..1). */
    fun tapAt(x: Float, y: Float, timeMs: Long) {
        val current = engine ?: return
        val element = current.elementAt(x, y)
        if (element == null) {
            internalState.value = internalState.value.copy(
                message = "Ahi no hay nada marcado. Busca los puntos que brillan."
            )
            return
        }
        when (val activation = current.activate(element.id, timeMs)) {
            is InteractionEngine.Activation.Accepted -> {
                val systemName = internalState.value.systems
                    .firstOrNull { it.id == element.systemId }?.name.orEmpty()
                internalState.value = internalState.value.copy(
                    selected = element,
                    selectedSystemName = systemName,
                    explored = current.activatedIds(),
                    progress = current.progress(),
                    message = element.description
                )
                if (activation.firstTime) persist(element)
            }
            is InteractionEngine.Activation.Ignored -> Unit
            InteractionEngine.Activation.Unknown -> Unit
        }
    }

    private fun persist(element: InteractiveElementModel) {
        viewModelScope.launch {
            repository.registerElementExplored(element.id, element.systemId)
            DiscoveryMap.conceptOfSystem(element.systemId)?.let { concept ->
                val fullyExplored = engine?.isSystemFullyExplored(element.systemId) == true
                if (fullyExplored) repository.saveDiscovery(concept)
            }
            val badges = repository.refreshBadges()
            if (badges.isNotEmpty()) {
                internalState.value = internalState.value.copy(newBadges = badges)
            }
        }
    }

    fun clearBadges() {
        internalState.value = internalState.value.copy(newBadges = emptyList())
    }

    fun finish() {
        viewModelScope.launch {
            if (internalState.value.progress >= 0.6f) {
                repository.completeExperience("exp_explorar")
                repository.saveDiscovery("ser_vivo")
                repository.saveDiscovery("sistema")
            }
        }
    }
}
