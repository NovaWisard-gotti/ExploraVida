package com.educalab.exploravida.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.exploravida.data.repository.ExploraVidaRepository
import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.domain.model.LivingSystemModel
import com.educalab.exploravida.domain.usecase.Challenge
import com.educalab.exploravida.domain.usecase.ChallengeCatalog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChallengeUiState(
    val loading: Boolean = true,
    val title: String = "",
    val prompt: String = "",
    val situation: String = "",
    val challenge: Challenge? = null,
    val systems: List<LivingSystemModel> = emptyList(),
    val chosenSystems: Set<String> = emptySet(),
    val observedCount: Int = 0,
    val droppedItem: IllustrationKey? = null,
    val chosenSide: Boolean? = null,
    val feedback: String = "",
    val solved: Boolean = false,
    val attempts: Int = 0,
    val stars: Int = 0
)

/** Retos que no son secuencia ni conexion: arrastrar, predecir, comparar y observar. */
class ChallengeViewModel(private val repository: ExploraVidaRepository) : ViewModel() {

    private val internalState = MutableStateFlow(ChallengeUiState())
    val state: StateFlow<ChallengeUiState> = internalState.asStateFlow()

    private var activityId: String = ""

    fun load(id: String) {
        if (activityId == id) return
        activityId = id
        viewModelScope.launch {
            val activity = repository.allActivitiesById(id)
            internalState.value = ChallengeUiState(
                loading = false,
                title = activity?.title ?: "Reto del laboratorio",
                prompt = activity?.prompt.orEmpty(),
                situation = activity?.situation.orEmpty(),
                challenge = ChallengeCatalog.forActivity(id),
                systems = repository.systems()
            )
        }
    }

    fun toggleSystem(systemId: String) {
        val current = internalState.value
        if (current.solved) return
        val chosen = if (systemId in current.chosenSystems) {
            current.chosenSystems - systemId
        } else {
            current.chosenSystems + systemId
        }
        internalState.value = current.copy(chosenSystems = chosen, feedback = "")
    }

    fun submitPredict() {
        val current = internalState.value
        val challenge = current.challenge as? Challenge.Predict ?: return
        val correct = ChallengeCatalog.evaluatePredict(challenge, current.chosenSystems)
        finish(correct, ChallengeCatalog.predictFeedback(challenge, current.chosenSystems))
    }

    fun dropItem(item: IllustrationKey) {
        val current = internalState.value
        val challenge = current.challenge as? Challenge.Drag ?: return
        val correct = item in challenge.correctItems
        internalState.value = current.copy(droppedItem = item)
        finish(correct, if (correct) challenge.successText else challenge.retryText)
    }

    fun chooseSide(isA: Boolean) {
        val current = internalState.value
        val challenge = current.challenge as? Challenge.Compare ?: return
        val correct = challenge.correctIsA == isA
        internalState.value = current.copy(chosenSide = isA)
        finish(
            correct,
            if (correct) challenge.explanation
            else "Fijate otra vez en las dos barras de energia antes de decidir."
        )
    }

    fun countObservation() {
        val current = internalState.value
        val challenge = current.challenge as? Challenge.Observe ?: return
        if (current.solved) return
        val count = current.observedCount + 1
        internalState.value = current.copy(observedCount = count)
        if (count >= challenge.target) finish(true, challenge.explanation)
    }

    fun retry() {
        val current = internalState.value
        if (current.solved) return
        internalState.value = current.copy(
            chosenSystems = emptySet(),
            droppedItem = null,
            chosenSide = null,
            observedCount = 0,
            feedback = "Vuelve a intentarlo con calma."
        )
    }

    private fun finish(correct: Boolean, message: String) {
        val current = internalState.value
        val attempts = current.attempts + 1
        val stars = when {
            !correct -> 0
            attempts <= 1 -> 3
            attempts == 2 -> 2
            else -> 1
        }
        internalState.value = current.copy(
            attempts = attempts,
            solved = correct,
            stars = stars,
            feedback = message
        )
        viewModelScope.launch {
            repository.registerActivityAttempt(
                activityId = activityId,
                correct = correct,
                stars = stars,
                detail = "intento " + attempts
            )
            if (correct) repository.refreshBadges()
        }
    }
}
