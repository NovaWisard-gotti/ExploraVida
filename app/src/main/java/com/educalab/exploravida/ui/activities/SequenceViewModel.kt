package com.educalab.exploravida.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.exploravida.data.local.entity.SequenceItemEntity
import com.educalab.exploravida.data.repository.ExploraVidaRepository
import com.educalab.exploravida.domain.engine.SequenceEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SequenceUiState(
    val loading: Boolean = true,
    val title: String = "",
    val prompt: String = "",
    val situation: String = "",
    val pool: List<SequenceItemEntity> = emptyList(),
    val placed: List<SequenceItemEntity> = emptyList(),
    val feedback: String = "",
    val solved: Boolean = false,
    val attempts: Int = 0,
    val stars: Int = 0,
    val explanation: String = ""
)

/** Actividad de ordenar tarjetas ilustradas. */
class SequenceViewModel(private val repository: ExploraVidaRepository) : ViewModel() {

    private val engine = SequenceEngine()
    private val internalState = MutableStateFlow(SequenceUiState())
    val state: StateFlow<SequenceUiState> = internalState.asStateFlow()

    private var activityId: String = ""
    private var expected: List<String> = emptyList()

    fun load(id: String) {
        if (activityId == id) return
        activityId = id
        viewModelScope.launch {
            val items = repository.sequenceItemsFor(id)
            val activity = repository.allActivitiesById(id)
            expected = items.sortedBy { it.correctPosition }.map { it.label }
            val shuffledLabels = engine.shuffleDeterministic(expected, id.hashCode().toLong())
            val pool = shuffledLabels.mapNotNull { label -> items.firstOrNull { it.label == label } }
            internalState.value = SequenceUiState(
                loading = false,
                title = activity?.title ?: "Ordena lo que ocurre",
                prompt = activity?.prompt ?: "Coloca las tarjetas en orden.",
                situation = activity?.situation.orEmpty(),
                pool = pool,
                explanation = repository.sequenceExplanation(id)
            )
        }
    }

    fun place(item: SequenceItemEntity) {
        val current = internalState.value
        if (current.solved || item in current.placed) return
        internalState.value = current.copy(
            pool = current.pool - item,
            placed = current.placed + item,
            feedback = ""
        )
    }

    fun removeLast() {
        val current = internalState.value
        if (current.solved || current.placed.isEmpty()) return
        val last = current.placed.last()
        internalState.value = current.copy(
            placed = current.placed.dropLast(1),
            pool = current.pool + last,
            feedback = ""
        )
    }

    fun check() {
        val current = internalState.value
        if (current.solved) return
        val result = engine.validate(expected, current.placed.map { it.label })
        val attempts = current.attempts + 1
        val stars = engine.stars(attempts, result.solved)
        internalState.value = current.copy(
            attempts = attempts,
            solved = result.solved,
            stars = stars,
            feedback = if (result.solved) current.explanation.ifBlank { result.feedback } else result.feedback
        )
        viewModelScope.launch {
            repository.registerActivityAttempt(
                activityId = activityId,
                correct = result.solved,
                stars = stars,
                detail = "aciertos " + result.correctCount + "/" + result.total
            )
            if (result.solved) repository.refreshBadges()
        }
    }

    fun hint() {
        val current = internalState.value
        internalState.value = current.copy(
            feedback = engine.hint(expected, current.placed.map { it.label })
        )
    }

    fun retry() {
        val current = internalState.value
        if (current.solved) return
        val all = (current.pool + current.placed)
        internalState.value = current.copy(
            pool = engine.shuffleDeterministic(all.map { it.label }, (activityId + current.attempts).hashCode().toLong())
                .mapNotNull { label -> all.firstOrNull { it.label == label } },
            placed = emptyList(),
            feedback = "Vuelve a intentarlo. Piensa en que ocurre primero."
        )
    }
}
