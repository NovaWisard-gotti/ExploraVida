package com.educalab.exploravida.data.repository

import com.educalab.exploravida.data.local.ExploraVidaDatabase
import com.educalab.exploravida.data.local.entity.ActivityAttemptEntity
import com.educalab.exploravida.data.local.entity.ActivityEntity
import com.educalab.exploravida.data.local.entity.BadgeEntity
import com.educalab.exploravida.data.local.entity.ExplorerNotebookEntity
import com.educalab.exploravida.data.local.entity.NotebookDiscoveryEntity
import com.educalab.exploravida.data.local.entity.ProgressEntity
import com.educalab.exploravida.data.local.entity.SequenceItemEntity
import com.educalab.exploravida.data.local.entity.UnlockedExperienceEntity
import com.educalab.exploravida.data.local.entity.UserProfileEntity
import com.educalab.exploravida.data.local.seed.SeedActivities
import com.educalab.exploravida.domain.engine.BodySystemEngine
import com.educalab.exploravida.domain.engine.ProgressEngine
import com.educalab.exploravida.domain.engine.RewardEngine
import com.educalab.exploravida.domain.model.AnimationKey
import com.educalab.exploravida.domain.model.BadgeModel
import com.educalab.exploravida.domain.model.ExperienceKind
import com.educalab.exploravida.domain.model.ExperienceModel
import com.educalab.exploravida.domain.model.InteractiveElementModel
import com.educalab.exploravida.domain.model.LivingSystemModel
import com.educalab.exploravida.domain.model.ProgressStats
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.domain.model.StepModel
import com.educalab.exploravida.domain.model.SystemLink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Unico punto de acceso a los datos. La UI nunca toca Room directamente
 * y nunca escribe SQL dentro de un Composable.
 */
class ExploraVidaRepository(
    private val database: ExploraVidaDatabase,
    private val clock: () -> Long = { System.currentTimeMillis() }
) {

    private val contentDao = database.contentDao()
    private val profileDao = database.profileDao()
    private val progressDao = database.progressDao()
    private val notebookDao = database.notebookDao()
    private val badgeDao = database.badgeDao()

    // ----------------------------------------------------------------- perfil

    val profile: Flow<UserProfileEntity?> = profileDao.observeProfile()
    val progress: Flow<ProgressEntity?> = progressDao.observeProgress()
    val notebookPages: Flow<List<ExplorerNotebookEntity>> = notebookDao.observePages()
    val discoveries: Flow<List<NotebookDiscoveryEntity>> = notebookDao.observeDiscoveries()
    val userBadgeIds: Flow<List<String>> = badgeDao.observeUserBadges().map { list -> list.map { it.badgeId } }
    val unlockedExperiences: Flow<List<UnlockedExperienceEntity>> = progressDao.observeUnlocked()

    suspend fun currentProfile(): UserProfileEntity? = profileDao.profile()

    /** Crea el perfil local y prepara cuaderno y progreso. Sin datos personales. */
    suspend fun createProfile(alias: String, avatarId: Int) {
        val safeAlias = alias.trim().take(16).ifBlank { "Explorador" }
        profileDao.upsert(
            UserProfileEntity(
                id = 1,
                alias = safeAlias,
                avatarId = avatarId.coerceIn(0, 7),
                onboardingDone = true,
                createdAt = clock()
            )
        )
        if (progressDao.progress() == null) {
            progressDao.upsertProgress(ProgressEntity(profileId = 1, updatedAt = clock()))
        }
        if (notebookDao.pages().isEmpty()) {
            notebookDao.insertPages(
                SeedActivities.concepts.mapIndexed { index, concept ->
                    ExplorerNotebookEntity(
                        id = "page_" + concept.key,
                        profileId = 1,
                        title = concept.title,
                        pageIndex = index,
                        stickerKey = concept.sticker,
                        unlocked = false
                    )
                }
            )
        }
        unlockInitialExperiences()
    }

    suspend fun updateIdentity(alias: String, avatarId: Int) {
        val safeAlias = alias.trim().take(16).ifBlank { "Explorador" }
        profileDao.updateIdentity(safeAlias, avatarId.coerceIn(0, 7))
    }

    suspend fun updatePreferences(sound: Boolean, haptics: Boolean) =
        profileDao.updatePreferences(sound, haptics)

    // --------------------------------------------------------------- contenido

    suspend fun systems(): List<LivingSystemModel> = contentDao.systems().map {
        LivingSystemModel(it.id, it.name, it.shortDescription, it.colorHex, it.iconKey, it.orderIndex)
    }

    suspend fun links(): List<SystemLink> = contentDao.connections().map {
        SystemLink(it.fromSystemId, it.toSystemId, it.explanation)
    }

    suspend fun bodyEngine(): BodySystemEngine = BodySystemEngine(systems(), links())

    suspend fun experiences(): List<ExperienceModel> = contentDao.experiences().map { it.toModel() }

    suspend fun experience(id: String): ExperienceModel? = contentDao.experience(id)?.toModel()

    suspend fun steps(experienceId: String): List<StepModel> = contentDao.steps(experienceId).map {
        StepModel(
            id = it.id, experienceId = it.experienceId, orderIndex = it.orderIndex,
            title = it.title, text = it.text, systemId = it.systemId,
            animation = safeAnimation(it.animationKey), illustrationKey = it.illustrationKey
        )
    }

    suspend fun elements(): List<InteractiveElementModel> = contentDao.elements().map {
        InteractiveElementModel(it.id, it.name, it.systemId, it.description, it.x, it.y, it.radius, it.illustrationKey)
    }

    suspend fun activitiesOf(experienceId: String): List<ActivityEntity> =
        contentDao.activitiesOf(experienceId)

    suspend fun allActivitiesById(activityId: String): ActivityEntity? = contentDao.activity(activityId)

    suspend fun sequenceItemsFor(activityId: String): List<SequenceItemEntity> {
        val sequence = contentDao.sequenceOf(activityId) ?: return emptyList()
        return contentDao.sequenceItems(sequence.id)
    }

    suspend fun sequenceExplanation(activityId: String): String =
        contentDao.sequenceOf(activityId)?.explanation.orEmpty()

    suspend fun badges(): List<BadgeModel> = badgeDao.badges().map { it.toModel() }

    // -------------------------------------------------------------- progreso

    private suspend fun currentProgress(): ProgressEntity =
        progressDao.progress() ?: ProgressEntity(profileId = 1, updatedAt = clock()).also {
            progressDao.upsertProgress(it)
        }

    suspend fun stats(): ProgressStats {
        val entity = currentProgress()
        return ProgressStats(
            xp = entity.xp,
            experiencesCompleted = entity.experiencesCompleted,
            activitiesCompleted = entity.activitiesCompleted,
            perfectActivities = entity.perfectActivities,
            journeysCompleted = entity.journeysCompleted,
            sequencesSolved = entity.sequencesSolved,
            connectionsMade = entity.connectionsMade,
            elementsExplored = entity.elementsExplored,
            discoveries = entity.discoveries,
            systemsVisited = entity.visitedSystems.toSet()
        )
    }

    private suspend fun save(progress: ProgressEntity) {
        progressDao.upsertProgress(
            progress.copy(level = ProgressEngine.levelFor(progress.xp), updatedAt = clock())
        )
    }

    suspend fun addXp(amount: Int) {
        val current = currentProgress()
        save(current.copy(xp = ProgressEngine.addXp(current.xp, amount)))
    }

    suspend fun registerStepViewed(experienceId: String) {
        progressDao.markExperience(experienceId, 1, started = true, completed = false, mastered = false, explored = emptyList(), now = clock())
        addXp(ProgressEngine.XP_STEP_VIEWED)
    }

    /** Marca una zona del cuerpo como explorada. Solo suma la primera vez. */
    suspend fun registerElementExplored(elementId: String, systemId: String): Boolean {
        val record = progressDao.unlockedFor("exp_explorar", 1)
        if (record != null && elementId in record.exploredElements) return false
        progressDao.markExperience(
            "exp_explorar", 1, started = true, completed = false, mastered = false,
            explored = listOf(elementId), now = clock()
        )
        val current = currentProgress()
        val visited = (current.visitedSystems + systemId).distinct()
        save(
            current.copy(
                xp = ProgressEngine.addXp(current.xp, ProgressEngine.XP_ELEMENT_EXPLORED),
                elementsExplored = current.elementsExplored + 1,
                visitedSystems = visited
            )
        )
        return true
    }

    suspend fun registerConnection(count: Int) {
        val current = currentProgress()
        if (count <= current.connectionsMade) return
        save(current.copy(connectionsMade = count, xp = ProgressEngine.addXp(current.xp, 6)))
    }

    /** Guarda el intento real y actualiza el progreso segun el resultado. */
    suspend fun registerActivityAttempt(
        activityId: String,
        correct: Boolean,
        stars: Int,
        detail: String
    ) {
        val previous = progressDao.attemptCount(activityId, 1)
        val alreadySolved = progressDao.attemptsOf(activityId, 1).any { it.correct }
        progressDao.insertAttempt(
            ActivityAttemptEntity(
                activityId = activityId, profileId = 1, correct = correct,
                stars = stars.coerceIn(0, 3), attemptNumber = previous + 1,
                detail = detail.take(160), attemptedAt = clock()
            )
        )
        if (!correct || alreadySolved) return
        val current = currentProgress()
        val activity = contentDao.activity(activityId)
        val reward = activity?.xpReward ?: ProgressEngine.XP_ACTIVITY_OK
        val bonus = if (stars >= 3) ProgressEngine.XP_ACTIVITY_PERFECT_BONUS else 0
        val isSequence = activity?.kind == com.educalab.exploravida.domain.model.ActivityKind.ORDENAR.name
        save(
            current.copy(
                xp = ProgressEngine.addXp(current.xp, reward + bonus),
                activitiesCompleted = current.activitiesCompleted + 1,
                perfectActivities = current.perfectActivities + if (stars >= 3) 1 else 0,
                sequencesSolved = current.sequencesSolved + if (isSequence) 1 else 0
            )
        )
    }

    suspend fun completeExperience(experienceId: String): Boolean {
        val record = progressDao.unlockedFor(experienceId, 1)
        val alreadyCompleted = record?.completed == true
        progressDao.markExperience(
            experienceId, 1, started = true, completed = true,
            mastered = alreadyCompleted, explored = emptyList(), now = clock()
        )
        if (alreadyCompleted) return false
        val experience = contentDao.experience(experienceId)
        val isJourney = experience?.kind == ExperienceKind.RECORRIDO.name
        val current = currentProgress()
        save(
            current.copy(
                xp = ProgressEngine.addXp(current.xp, ProgressEngine.XP_EXPERIENCE_COMPLETED),
                experiencesCompleted = current.experiencesCompleted + 1,
                journeysCompleted = current.journeysCompleted + if (isJourney) 1 else 0
            )
        )
        unlockInitialExperiences()
        return true
    }

    /** Registra en el cuaderno la primera vez que se entiende un concepto. */
    suspend fun saveDiscovery(conceptKey: String): Boolean {
        val concept = SeedActivities.concepts.firstOrNull { it.key == conceptKey } ?: return false
        val saved = notebookDao.saveDiscovery(
            NotebookDiscoveryEntity(
                notebookId = "page_" + concept.key,
                conceptKey = concept.key,
                text = concept.text,
                stickerKey = concept.sticker,
                discoveredAt = clock()
            )
        )
        if (!saved) return false
        val current = currentProgress()
        save(
            current.copy(
                discoveries = current.discoveries + 1,
                xp = ProgressEngine.addXp(current.xp, ProgressEngine.XP_DISCOVERY)
            )
        )
        return true
    }

    /** Escribe en la base las experiencias que el XP actual ya permite abrir. */
    suspend fun unlockInitialExperiences() {
        val xp = currentProgress().xp
        val open = ProgressEngine.unlocked(experiences(), xp)
        val known = progressDao.unlocked(1).map { it.experienceId }.toSet()
        open.filter { it.id !in known }.forEach {
            progressDao.upsertUnlocked(
                UnlockedExperienceEntity(experienceId = it.id, profileId = 1, unlockedAt = clock())
            )
        }
    }

    /** Comprueba insignias y entrega las nuevas. Devuelve solo las recien ganadas. */
    suspend fun refreshBadges(): List<BadgeModel> {
        val allBadges = badges()
        val engine = RewardEngine(allBadges)
        val earned = badgeDao.earnedIds(1).toSet()
        val fresh = engine.newlyEarned(stats(), earned)
        if (fresh.isEmpty()) return emptyList()
        val awarded = badgeDao.awardAll(fresh.map { it.id }, 1, clock())
        return allBadges.filter { it.id in awarded }
    }

    suspend fun activitiesToReview(): List<ActivityEntity> {
        val ids = progressDao.activitiesToReview(1)
        if (ids.isEmpty()) return emptyList()
        return contentDao.allActivities().filter { it.id in ids }
    }

    // Instantaneas puntuales para el ViewModel (ademas de los Flow continuos).
    suspend fun unlockedSnapshot(): List<UnlockedExperienceEntity> = progressDao.unlocked(1)

    suspend fun earnedBadgeIdsSnapshot(): Set<String> = badgeDao.earnedIds(1).toSet()

    suspend fun notebookPagesSnapshot(): List<ExplorerNotebookEntity> = notebookDao.pages()

    suspend fun discoveriesSnapshot(): List<NotebookDiscoveryEntity> =
        notebookDao.pages().flatMap { notebookDao.discoveriesOf(it.id) }

    suspend fun exploredElementIds(): List<String> =
        progressDao.unlockedFor("exp_explorar", 1)?.exploredElements.orEmpty()

    /** Reinicio total del progreso. El contenido educativo se conserva. */
    suspend fun resetProgress() {
        progressDao.clearAttempts(1)
        progressDao.clearUnlocked(1)
        progressDao.clearProgress(1)
        notebookDao.clearDiscoveries()
        badgeDao.clearUserBadges(1)
        progressDao.upsertProgress(ProgressEntity(profileId = 1, updatedAt = clock()))
        notebookDao.pages().forEach { /* las paginas se conservan bloqueadas */ }
        unlockInitialExperiences()
    }

    // --------------------------------------------------------------- mapeadores

    private fun com.educalab.exploravida.data.local.entity.LearningExperienceEntity.toModel() =
        ExperienceModel(
            id = id, title = title, subtitle = subtitle, noraIntro = noraIntro,
            kind = runCatching { ExperienceKind.valueOf(kind) }.getOrDefault(ExperienceKind.RECORRIDO),
            orderIndex = orderIndex, requiredXp = requiredXp,
            background = runCatching { SceneBackground.valueOf(backgroundKey) }
                .getOrDefault(SceneBackground.LABORATORIO),
            iconKey = iconKey
        )

    private fun BadgeEntity.toModel() =
        BadgeModel(id, name, description, iconKey, ruleKey, threshold)

    private fun safeAnimation(key: String): AnimationKey =
        runCatching { AnimationKey.valueOf(key) }.getOrDefault(AnimationKey.SISTEMA_ILUMINA)
}
