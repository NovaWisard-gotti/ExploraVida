package com.educalab.exploravida.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.educalab.exploravida.data.repository.ExploraVidaRepository
import com.educalab.exploravida.domain.model.ActivityKind
import com.educalab.exploravida.ui.activities.ChallengeScreen
import com.educalab.exploravida.ui.activities.ChallengeViewModel
import com.educalab.exploravida.ui.activities.ConnectionScreen
import com.educalab.exploravida.ui.activities.ConnectionViewModel
import com.educalab.exploravida.ui.activities.SequenceScreen
import com.educalab.exploravida.ui.activities.SequenceViewModel
import com.educalab.exploravida.ui.exploration.ExperienceScreen
import com.educalab.exploravida.ui.exploration.ExperienceViewModel
import com.educalab.exploravida.ui.lab.LabScreen
import com.educalab.exploravida.ui.lab.LabViewModel
import com.educalab.exploravida.ui.notebook.NotebookScreen
import com.educalab.exploravida.ui.organism.ExplorerViewModel
import com.educalab.exploravida.ui.organism.OrganismExplorerScreen
import com.educalab.exploravida.ui.screens.BadgesScreen
import com.educalab.exploravida.ui.screens.OnboardingScreen
import com.educalab.exploravida.ui.screens.ProfileScreen
import com.educalab.exploravida.ui.screens.SettingsScreen
import com.educalab.exploravida.ui.theme.LabColors
import com.educalab.exploravida.util.FeedbackController
import com.educalab.exploravida.util.LocalFeedback

object Routes {
    const val ONBOARDING = "onboarding"
    const val LAB = "laboratorio"
    const val EXPERIENCE = "experiencia/{id}"
    const val EXPLORER = "explorar"
    const val ACTIVITY = "actividad/{id}"
    const val NOTEBOOK = "cuaderno"
    const val BADGES = "insignias"
    const val PROFILE = "perfil"
    const val SETTINGS = "ajustes"

    fun experience(id: String) = "experiencia/" + id
    fun activity(id: String) = "actividad/" + id
}

@Composable
fun ExploraVidaNavHost(
    repository: ExploraVidaRepository,
    navController: NavHostController = rememberNavController()
) {
    val factory = remember(repository) { VmFactory(repository) }
    val labViewModel: LabViewModel = viewModel(factory = factory)
    val labState by labViewModel.state.collectAsState()

    val feedback = remember { FeedbackController() }
    DisposableEffect(Unit) { onDispose { feedback.release() } }
    feedback.soundEnabled = labState.profile?.soundEnabled ?: true
    feedback.hapticsEnabled = labState.profile?.hapticsEnabled ?: true

    val profileReady = produceState(initialValue = null as Boolean?, labState.loading) {
        if (!labState.loading) value = repository.currentProfile() != null
    }

    when (profileReady.value) {
        null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Preparando el laboratorio...",
                style = MaterialTheme.typography.titleMedium,
                color = LabColors.Sand
            )
        }

        else -> CompositionLocalProvider(LocalFeedback provides feedback) {
            NavHost(
                navController = navController,
                startDestination = if (profileReady.value == true) Routes.LAB else Routes.ONBOARDING
            ) {
                composable(Routes.ONBOARDING) {
                    OnboardingScreen { alias, avatar ->
                        labViewModel.createProfile(alias, avatar)
                        navController.navigate(Routes.LAB) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    }
                }

                composable(Routes.LAB) {
                    LabScreen(
                        viewModel = labViewModel,
                        onOpenExperience = { navController.navigate(Routes.experience(it)) },
                        onExploreOrganism = { navController.navigate(Routes.EXPLORER) },
                        onOpenNotebook = { navController.navigate(Routes.NOTEBOOK) },
                        onOpenBadges = { navController.navigate(Routes.BADGES) },
                        onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                        onOpenProfile = { navController.navigate(Routes.PROFILE) },
                        onOpenReview = { navController.navigate(Routes.activity(it)) }
                    )
                }

                composable(
                    route = Routes.EXPERIENCE,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { entry ->
                    val id = entry.arguments?.getString("id").orEmpty()
                    val vm: ExperienceViewModel = viewModel(factory = factory)
                    ExperienceScreen(
                        experienceId = id,
                        viewModel = vm,
                        onBack = {
                            labViewModel.refresh()
                            navController.popBackStack()
                        },
                        onOpenActivity = { navController.navigate(Routes.activity(it)) }
                    )
                }

                composable(Routes.EXPLORER) {
                    val vm: ExplorerViewModel = viewModel(factory = factory)
                    OrganismExplorerScreen(
                        viewModel = vm,
                        onBack = {
                            labViewModel.refresh()
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    route = Routes.ACTIVITY,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { entry ->
                    val id = entry.arguments?.getString("id").orEmpty()
                    ActivityRoute(
                        activityId = id,
                        repository = repository,
                        factory = factory,
                        onBack = {
                            labViewModel.refresh()
                            navController.popBackStack()
                        }
                    )
                }

                composable(Routes.NOTEBOOK) {
                    NotebookScreen(labViewModel) { navController.popBackStack() }
                }

                composable(Routes.BADGES) {
                    BadgesScreen(labViewModel) { navController.popBackStack() }
                }

                composable(Routes.PROFILE) {
                    ProfileScreen(labViewModel) { navController.popBackStack() }
                }

                composable(Routes.SETTINGS) {
                    SettingsScreen(
                        viewModel = labViewModel,
                        onBack = { navController.popBackStack() },
                        onOpenProfile = { navController.navigate(Routes.PROFILE) }
                    )
                }
            }
        }
    }
}

/** Elige la pantalla segun el tipo real de la actividad guardada en Room. */
@Composable
private fun ActivityRoute(
    activityId: String,
    repository: ExploraVidaRepository,
    factory: VmFactory,
    onBack: () -> Unit
) {
    val kind = produceState(initialValue = null as String?, activityId) {
        value = repository.allActivitiesById(activityId)?.kind ?: ""
    }

    when (kind.value) {
        null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Abriendo el reto...", color = LabColors.Sand)
        }

        ActivityKind.ORDENAR.name -> {
            val vm: SequenceViewModel = viewModel(factory = factory)
            SequenceScreen(activityId, vm, onBack)
        }

        ActivityKind.CONECTAR.name -> {
            val vm: ConnectionViewModel = viewModel(factory = factory)
            ConnectionScreen(activityId, vm, onBack)
        }

        else -> {
            val vm: ChallengeViewModel = viewModel(factory = factory)
            ChallengeScreen(activityId, vm, onBack)
        }
    }
}
