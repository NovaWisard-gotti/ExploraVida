package com.educalab.exploravida.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.educalab.exploravida.data.repository.ExploraVidaRepository
import com.educalab.exploravida.ui.activities.ChallengeViewModel
import com.educalab.exploravida.ui.activities.ConnectionViewModel
import com.educalab.exploravida.ui.activities.SequenceViewModel
import com.educalab.exploravida.ui.exploration.ExperienceViewModel
import com.educalab.exploravida.ui.lab.LabViewModel
import com.educalab.exploravida.ui.organism.ExplorerViewModel

/** Fabrica sencilla de ViewModels. Evita librerias de inyeccion innecesarias. */
class VmFactory(private val repository: ExploraVidaRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(LabViewModel::class.java) -> LabViewModel(repository) as T
        modelClass.isAssignableFrom(ExperienceViewModel::class.java) -> ExperienceViewModel(repository) as T
        modelClass.isAssignableFrom(ExplorerViewModel::class.java) -> ExplorerViewModel(repository) as T
        modelClass.isAssignableFrom(SequenceViewModel::class.java) -> SequenceViewModel(repository) as T
        modelClass.isAssignableFrom(ConnectionViewModel::class.java) -> ConnectionViewModel(repository) as T
        modelClass.isAssignableFrom(ChallengeViewModel::class.java) -> ChallengeViewModel(repository) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: " + modelClass.name)
    }
}
