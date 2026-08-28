package com.educalab.exploravida.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Perfil local del nino. Solo alias y avatar: nunca nombre real,
 * ni correo, ni telefono, ni ubicacion.
 */
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Long = 1,
    val alias: String,
    val avatarId: Int,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val onboardingDone: Boolean = false,
    val createdAt: Long = 0L
)

/** Avance acumulado del perfil. Se recalcula desde acciones reales. */
@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["profileId"], unique = true)]
)
data class ProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profileId: Long = 1,
    val xp: Int = 0,
    val level: Int = 1,
    val experiencesCompleted: Int = 0,
    val activitiesCompleted: Int = 0,
    val perfectActivities: Int = 0,
    val journeysCompleted: Int = 0,
    val sequencesSolved: Int = 0,
    val connectionsMade: Int = 0,
    val elementsExplored: Int = 0,
    val discoveries: Int = 0,
    val visitedSystems: List<String> = emptyList(),
    val updatedAt: Long = 0L
)

/** Intento real de una actividad. Es el historial persistido. */
@Entity(
    tableName = "activity_attempt",
    foreignKeys = [
        ForeignKey(
            entity = ActivityEntity::class,
            parentColumns = ["id"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["activityId"]), Index(value = ["profileId"])]
)
data class ActivityAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val activityId: String,
    val profileId: Long = 1,
    val correct: Boolean,
    val stars: Int,
    val attemptNumber: Int,
    val detail: String,
    val attemptedAt: Long = 0L
)

/** Cuaderno del explorador: una pagina por tema descubierto. */
@Entity(
    tableName = "explorer_notebook",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["profileId", "pageIndex"], unique = true)]
)
data class ExplorerNotebookEntity(
    @PrimaryKey val id: String,
    val profileId: Long = 1,
    val title: String,
    val pageIndex: Int,
    val stickerKey: String,
    val unlocked: Boolean = false
)

/** Pegatina/descubrimiento guardado dentro de una pagina del cuaderno. */
@Entity(
    tableName = "notebook_discovery",
    foreignKeys = [
        ForeignKey(
            entity = ExplorerNotebookEntity::class,
            parentColumns = ["id"],
            childColumns = ["notebookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["notebookId"]), Index(value = ["conceptKey"], unique = true)]
)
data class NotebookDiscoveryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val notebookId: String,
    val conceptKey: String,
    val text: String,
    val stickerKey: String,
    val discoveredAt: Long = 0L
)

/** Insignia realmente ganada por el perfil. */
@Entity(
    tableName = "user_badge",
    foreignKeys = [
        ForeignKey(
            entity = BadgeEntity::class,
            parentColumns = ["id"],
            childColumns = ["badgeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["badgeId", "profileId"], unique = true)]
)
data class UserBadgeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val badgeId: String,
    val profileId: Long = 1,
    val earnedAt: Long = 0L
)

/** Experiencia desbloqueada y su estado educativo. */
@Entity(
    tableName = "unlocked_experience",
    foreignKeys = [
        ForeignKey(
            entity = LearningExperienceEntity::class,
            parentColumns = ["id"],
            childColumns = ["experienceId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["experienceId", "profileId"], unique = true)]
)
data class UnlockedExperienceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val experienceId: String,
    val profileId: Long = 1,
    val started: Boolean = false,
    val completed: Boolean = false,
    val mastered: Boolean = false,
    val exploredElements: List<String> = emptyList(),
    val unlockedAt: Long = 0L
)
