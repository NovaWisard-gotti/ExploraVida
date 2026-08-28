package com.educalab.exploravida.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.exploravida.data.repository.ExploraVidaRepository
import com.educalab.exploravida.domain.engine.ConnectionEngine
import com.educalab.exploravida.domain.model.LivingSystemModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ConnectionUiState(
    val loading: Boolean = true,
    val title: String = "",
    val prompt: String = "",
    val situation: String = "",
    val systems: List<LivingSystemModel> = emptyList(),
    val made: List<Pair<String, String>> = emptyList(),
    val selected: String? = null,
    val feedback: String = "Arrastra desde un sistema hasta otro.",
    val lastStatus: ConnectionEngine.Status? = null,
    val total: Int = 0,
    val solved: Boolean = false
)

/** Modo Conexiones: unir sistemas que se ayudan de verdad. */
class ConnectionViewModel(private val repository: ExploraVidaRepository) : ViewModel() {

    private var engine: ConnectionEngine? = null
    private val internalState = MutableStateFlow(ConnectionUiState())
    val state: StateFlow<ConnectionUiState> = internalState.asStateFlow()

    private var activityId: String = ""
    private var goal: Int = 0

    fun load(id: String) {
        if (activityId == id) return
        activityId = id
        viewModelScope.launch {
            val body = repository.bodyEngine()
            val created = ConnectionEngine(body)
            engine = created
            val activity = repository.allActivitiesById(id)
            goal = when (id) {
                "act_conexiones_libre" -> body.totalLinks()
                "act_conexiones_reto" -> 3
                else -> 2
            }
            internalState.value = ConnectionUiState(
                loading = false,
                title = activity?.title ?: "Conecta los sistemas",
                prompt = activity?.prompt ?: "Une dos sistemas que se ayuden.",
                situation = activity?.situation.orEmpty(),
                systems = repository.systems(),
                total = goal
            )
        }
    }

    fun select(systemId: String) {
        val current = internalState.value
        if (current.solved) return
        val origin = current.selected
        if (origin == null) {
            internalState.value = current.copy(
                selected = systemId,
                feedback = "Ahora elige el sistema que recibe la ayuda."
            )
            return
        }
        connect(origin, systemId)
    }

    fun connect(from: String, to: String) {
        val current = internalState.value
        val active = engine ?: return
        val result = active.connect(from, to)
        val made = active.madeConnections().toList()
        val solved = made.size >= goal
        internalState.value = current.copy(
            made = made,
            selected = null,
            feedback = result.explanation,
            lastStatus = result.status,
            solved = solved
        )
        viewModelScope.launch {
            repository.registerConnection(made.size)
            if (result.status == ConnectionEngine.Status.VALIDA) {
                repository.saveDiscovery("conexion")
            }
            if (solved && !current.solved) {
                repository.registerActivityAttempt(
                    activityId = activityId,
                    correct = true,
                    stars = if (made.size >= goal) 3 else 2,
                    detail = "conexiones " + made.size
                )
                repository.refreshBadges()
            }
        }
    }

    fun clearSelection() {
        internalState.value = internalState.value.copy(selected = null)
    }
}
