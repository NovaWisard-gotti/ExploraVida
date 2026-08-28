package com.educalab.exploravida.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.educalab.exploravida.data.local.entity.ActivityAttemptEntity
import com.educalab.exploravida.data.local.entity.ProgressEntity
import com.educalab.exploravida.data.local.entity.UnlockedExperienceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    @Query("SELECT * FROM progress WHERE profileId = :profileId")
    fun observeProgress(profileId: Long = 1): Flow<ProgressEntity?>

    @Query("SELECT * FROM progress WHERE profileId = :profileId")
    suspend fun progress(profileId: Long = 1): ProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: ProgressEntity)

    @Query("SELECT * FROM unlocked_experience WHERE profileId = :profileId")
    fun observeUnlocked(profileId: Long = 1): Flow<List<UnlockedExperienceEntity>>

    @Query("SELECT * FROM unlocked_experience WHERE profileId = :profileId")
    suspend fun unlocked(profileId: Long = 1): List<UnlockedExperienceEntity>

    @Query("SELECT * FROM unlocked_experience WHERE experienceId = :experienceId AND profileId = :profileId")
    suspend fun unlockedFor(experienceId: String, profileId: Long = 1): UnlockedExperienceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUnlocked(entity: UnlockedExperienceEntity)

    /** Marca inicio o final de una experiencia sin duplicar filas. */
    @Transaction
    suspend fun markExperience(
        experienceId: String,
        profileId: Long,
        started: Boolean,
        completed: Boolean,
        mastered: Boolean,
        explored: List<String>,
        now: Long
    ) {
        val current = unlockedFor(experienceId, profileId)
        val merged = UnlockedExperienceEntity(
            id = current?.id ?: 0,
            experienceId = experienceId,
            profileId = profileId,
            started = started || (current?.started ?: false),
            completed = completed || (current?.completed ?: false),
            mastered = mastered || (current?.mastered ?: false),
            exploredElements = (current?.exploredElements.orEmpty() + explored).distinct(),
            unlockedAt = current?.unlockedAt ?: now
        )
        upsertUnlocked(merged)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: ActivityAttemptEntity)

    @Query("SELECT * FROM activity_attempt WHERE profileId = :profileId ORDER BY attemptedAt DESC")
    fun observeAttempts(profileId: Long = 1): Flow<List<ActivityAttemptEntity>>

    @Query("SELECT * FROM activity_attempt WHERE activityId = :activityId AND profileId = :profileId ORDER BY attemptNumber ASC")
    suspend fun attemptsOf(activityId: String, profileId: Long = 1): List<ActivityAttemptEntity>

    @Query("SELECT COUNT(*) FROM activity_attempt WHERE activityId = :activityId AND profileId = :profileId")
    suspend fun attemptCount(activityId: String, profileId: Long = 1): Int

    @Query("SELECT COUNT(DISTINCT activityId) FROM activity_attempt WHERE correct = 1 AND profileId = :profileId")
    suspend fun solvedActivities(profileId: Long = 1): Int

    @Query("SELECT COUNT(DISTINCT activityId) FROM activity_attempt WHERE correct = 1 AND stars = 3 AND profileId = :profileId")
    suspend fun perfectActivities(profileId: Long = 1): Int

    /** Actividades falladas al menos una vez y todavia no resueltas: base del repaso. */
    @Query(
        "SELECT DISTINCT activityId FROM activity_attempt WHERE profileId = :profileId AND correct = 0 " +
            "AND activityId NOT IN (SELECT activityId FROM activity_attempt WHERE profileId = :profileId AND correct = 1)"
    )
    suspend fun activitiesToReview(profileId: Long = 1): List<String>

    @Query("DELETE FROM activity_attempt WHERE profileId = :profileId")
    suspend fun clearAttempts(profileId: Long = 1)

    @Query("DELETE FROM unlocked_experience WHERE profileId = :profileId")
    suspend fun clearUnlocked(profileId: Long = 1)

    @Query("DELETE FROM progress WHERE profileId = :profileId")
    suspend fun clearProgress(profileId: Long = 1)
}
