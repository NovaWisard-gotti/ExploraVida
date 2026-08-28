package com.educalab.exploravida.ui.lab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.exploravida.data.local.entity.ActivityEntity
import com.educalab.exploravida.data.local.entity.ExplorerNotebookEntity
import com.educalab.exploravida.data.local.entity.NotebookDiscoveryEntity
import com.educalab.exploravida.data.local.entity.UserProfileEntity
import com.educalab.exploravida.data.repository.ExploraVidaRepository
import com.educalab.exploravida.domain.engine.ProgressEngine
import com.educalab.exploravida.domain.model.BadgeModel
import com.educalab.exploravida.domain.model.ExperienceModel
import com.educalab.exploravida.domain.model.LivingSystemModel
import com.educalab.exploravida.domain.model.ModuleState
import com.educalab.exploravida.domain.model.ProgressStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExperienceCard(
    val experience: ExperienceModel,
    val state: ModuleState
)

data class LabUiState(
    val loading: Boolean = true,
    val profile: UserProfileEntity? = null,
    val stats: ProgressStats = ProgressStats(),
    val level: Int = 1,
    val levelTitle: String = "Explorador novato",
    val levelProgress: Float = 0f,
    val systems: List<LivingSystemModel> = emptyList(),
    val cards: List<ExperienceCard> = emptyList(),
    val next: ExperienceModel? = null,
    val badges: List<BadgeModel> = emptyList(),
    val earnedBadgeIds: Set<String> = emptySet(),
    val freshBadges: List<BadgeModel> = emptyList(),
    val pages: List<ExplorerNotebookEntity> = emptyList(),
    val discoveries: List<NotebookDiscoveryEntity> = emptyList(),
    val reviewActivities: List<ActivityEntity> = emptyList(),
    val totalExperiences: Int = 0
)

/**
 * Estado del laboratorio: perfil, progreso, experiencias, cuaderno e insignias.
 * Vive en el ViewModel, asi que sobrevive a rotaciones y recomposiciones.
 */
class LabViewModel(private val repository: ExploraVidaRepository) : ViewModel() {

    private val internalState = MutableStateFlow(LabUiState())
    val state: StateFlow<LabUiState> = internalState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            repository.unlockInitialExperiences()
            val fresh = repository.refreshBadges()
            val stats = repository.stats()
            val experiences = repository.experiences()
            val records = repository.unlockedSnapshot()
            val completed = records.filter { it.completed }.map { it.experienceId }.toSet()
            val started = records.filter { it.started }.map { it.experienceId }.toSet()
            val mastered = records.filter { it.mastered }.map { it.experienceId }.toSet()
            val level = ProgressEngine.levelFor(stats.xp)

            internalState.value = LabUiState(
                loading = false,
                profile = repository.currentProfile(),
                stats = stats,
                level = level,
                levelTitle = ProgressEngine.levelTitle(level),
                levelProgress = ProgressEngine.levelProgress(stats.xp),
                systems = repository.systems(),
                cards = experiences.map {
                    ExperienceCard(it, ProgressEngine.stateOf(it, stats.xp, completed, started, mastered))
                },
                next = ProgressEngine.nextExperience(experiences, stats.xp, completed),
                badges = repository.badges(),
                earnedBadgeIds = repository.earnedBadgeIdsSnapshot(),
                freshBadges = fresh,
                pages = repository.notebookPagesSnapshot(),
                discoveries = repository.discoveriesSnapshot(),
                reviewActivities = repository.activitiesToReview(),
                totalExperiences = experiences.size
            )
        }
    }

    fun dismissFreshBadges() {
        internalState.value = internalState.value.copy(freshBadges = emptyList())
    }

    fun savePreferences(sound: Boolean, haptics: Boolean) {
        viewModelScope.launch {
            repository.updatePreferences(sound, haptics)
            refresh()
        }
    }

    fun saveIdentity(alias: String, avatarId: Int) {
        viewModelScope.launch {
            if (repository.currentProfile() == null) {
                repository.createProfile(alias, avatarId)
            } else {
                repository.updateIdentity(alias, avatarId)
            }
            refresh()
        }
    }

    /** Crea el perfil local al terminar el onboarding. */
    fun createProfile(alias: String, avatarId: Int) {
        viewModelScope.launch {
            repository.createProfile(alias, avatarId)
            refresh()
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            repository.resetProgress()
            refresh()
        }
    }
}
