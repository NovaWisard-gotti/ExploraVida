package com.educalab.exploravida.ui.exploration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.exploravida.data.repository.ExploraVidaRepository
import com.educalab.exploravida.domain.model.BadgeModel
import com.educalab.exploravida.domain.model.ExperienceModel
import com.educalab.exploravida.domain.model.StepModel
import com.educalab.exploravida.domain.usecase.DiscoveryMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExperienceUiState(
    val loading: Boolean = true,
    val experience: ExperienceModel? = null,
    val steps: List<StepModel> = emptyList(),
    val index: Int = 0,
    val visited: Set<Int> = setOf(0),
    val finished: Boolean = false,
    val noraMessage: String = "",
    val systemsInvolved: List<String> = emptyList(),
    val newBadges: List<BadgeModel> = emptyList(),
    val newDiscoveries: List<String> = emptyList(),
    val primaryActivityId: String? = null
) {
    val current: StepModel? get() = steps.getOrNull(index)
    val progress: Float get() = if (steps.isEmpty()) 0f else (visited.size.toFloat() / steps.size)
    val isLast: Boolean get() = steps.isNotEmpty() && index == steps.lastIndex
}

/** Controla una experiencia paso a paso. La logica educativa no vive en la UI. */
class ExperienceViewModel(private val repository: ExploraVidaRepository) : ViewModel() {

    private val internalState = MutableStateFlow(ExperienceUiState())
    val state: StateFlow<ExperienceUiState> = internalState.asStateFlow()

    private var loadedId: String? = null

    fun load(experienceId: String) {
        if (loadedId == experienceId) return
        loadedId = experienceId
        viewModelScope.launch {
            val experience = repository.experience(experienceId)
            val steps = repository.steps(experienceId)
            internalState.value = ExperienceUiState(
                loading = false,
                experience = experience,
                steps = steps,
                index = 0,
                visited = if (steps.isEmpty()) emptySet() else setOf(0),
                noraMessage = experience?.noraIntro.orEmpty(),
                systemsInvolved = steps.mapNotNull { it.systemId }.distinct(),
                primaryActivityId = repository.activitiesOf(experienceId).firstOrNull()?.id
            )
            if (experience != null) repository.registerStepViewed(experienceId)
        }
    }

    fun goTo(target: Int) {
        val current = internalState.value
        if (current.steps.isEmpty()) return
        val safe = target.coerceIn(0, current.steps.lastIndex)
        if (safe == current.index) return
        internalState.value = current.copy(
            index = safe,
            visited = current.visited + safe,
            noraMessage = current.steps[safe].title
        )
        viewModelScope.launch { repository.registerStepViewed(current.experience?.id ?: return@launch) }
    }

    fun next() {
        val current = internalState.value
        if (current.isLast) finish() else goTo(current.index + 1)
    }

    fun previous() = goTo(internalState.value.index - 1)

    fun finish() {
        val current = internalState.value
        val experienceId = current.experience?.id ?: return
        viewModelScope.launch {
            repository.completeExperience(experienceId)
            val discovered = mutableListOf<String>()
            DiscoveryMap.conceptsOf(experienceId).forEach {
                if (repository.saveDiscovery(it)) discovered.add(it)
            }
            val badges = repository.refreshBadges()
            internalState.value = internalState.value.copy(
                finished = true,
                newBadges = badges,
                newDiscoveries = discovered,
                noraMessage = "Buen trabajo. Ya has visto como se ayudan estos sistemas."
            )
        }
    }

    fun restart() {
        val current = internalState.value
        internalState.value = current.copy(
            index = 0,
            visited = setOf(0),
            finished = false,
            newBadges = emptyList(),
            newDiscoveries = emptyList(),
            noraMessage = current.experience?.noraIntro.orEmpty()
        )
    }
}
