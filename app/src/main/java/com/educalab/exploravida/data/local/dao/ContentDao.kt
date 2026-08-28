package com.educalab.exploravida.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.educalab.exploravida.data.local.entity.ActivityEntity
import com.educalab.exploravida.data.local.entity.ConnectionChallengeEntity
import com.educalab.exploravida.data.local.entity.ExperienceStepEntity
import com.educalab.exploravida.data.local.entity.InteractiveElementEntity
import com.educalab.exploravida.data.local.entity.LearningExperienceEntity
import com.educalab.exploravida.data.local.entity.LivingSystemEntity
import com.educalab.exploravida.data.local.entity.SequenceEntity
import com.educalab.exploravida.data.local.entity.SequenceItemEntity
import com.educalab.exploravida.data.local.entity.SystemConnectionEntity
import kotlinx.coroutines.flow.Flow

/** Acceso al contenido educativo semilla (solo lectura en tiempo de ejecucion). */
@Dao
interface ContentDao {

    @Query("SELECT * FROM living_system ORDER BY orderIndex ASC")
    fun observeSystems(): Flow<List<LivingSystemEntity>>

    @Query("SELECT * FROM living_system ORDER BY orderIndex ASC")
    suspend fun systems(): List<LivingSystemEntity>

    @Query("SELECT * FROM living_system WHERE id = :id")
    suspend fun system(id: String): LivingSystemEntity?

    @Query("SELECT * FROM system_connection")
    suspend fun connections(): List<SystemConnectionEntity>

    @Query("SELECT * FROM system_connection")
    fun observeConnections(): Flow<List<SystemConnectionEntity>>

    @Query("SELECT * FROM learning_experience ORDER BY orderIndex ASC")
    fun observeExperiences(): Flow<List<LearningExperienceEntity>>

    @Query("SELECT * FROM learning_experience ORDER BY orderIndex ASC")
    suspend fun experiences(): List<LearningExperienceEntity>

    @Query("SELECT * FROM learning_experience WHERE id = :id")
    suspend fun experience(id: String): LearningExperienceEntity?

    @Query("SELECT * FROM experience_step WHERE experienceId = :experienceId ORDER BY orderIndex ASC")
    suspend fun steps(experienceId: String): List<ExperienceStepEntity>

    @Query("SELECT COUNT(*) FROM experience_step")
    suspend fun stepCount(): Int

    @Query("SELECT * FROM interactive_element")
    suspend fun elements(): List<InteractiveElementEntity>

    @Query("SELECT * FROM interactive_element WHERE systemId = :systemId")
    suspend fun elementsOfSystem(systemId: String): List<InteractiveElementEntity>

    @Query("SELECT * FROM activity WHERE experienceId = :experienceId")
    suspend fun activitiesOf(experienceId: String): List<ActivityEntity>

    @Query("SELECT * FROM activity WHERE id = :id")
    suspend fun activity(id: String): ActivityEntity?

    @Query("SELECT * FROM activity")
    suspend fun allActivities(): List<ActivityEntity>

    @Query("SELECT * FROM sequence WHERE activityId = :activityId")
    suspend fun sequenceOf(activityId: String): SequenceEntity?

    @Query("SELECT * FROM sequence_item WHERE sequenceId = :sequenceId ORDER BY correctPosition ASC")
    suspend fun sequenceItems(sequenceId: String): List<SequenceItemEntity>

    @Query("SELECT * FROM connection_challenge WHERE activityId = :activityId")
    suspend fun connectionChallenges(activityId: String): List<ConnectionChallengeEntity>

    @Transaction
    suspend fun seedContent(
        systems: List<LivingSystemEntity>,
        connections: List<SystemConnectionEntity>,
        experiences: List<LearningExperienceEntity>,
        steps: List<ExperienceStepEntity>,
        elements: List<InteractiveElementEntity>,
        activities: List<ActivityEntity>,
        sequences: List<SequenceEntity>,
        sequenceItems: List<SequenceItemEntity>,
        challenges: List<ConnectionChallengeEntity>
    ) {
        insertSystems(systems)
        insertConnections(connections)
        insertExperiences(experiences)
        insertSteps(steps)
        insertElements(elements)
        insertActivities(activities)
        insertSequences(sequences)
        insertSequenceItems(sequenceItems)
        insertChallenges(challenges)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSystems(items: List<LivingSystemEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConnections(items: List<SystemConnectionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperiences(items: List<LearningExperienceEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSteps(items: List<ExperienceStepEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElements(items: List<InteractiveElementEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(items: List<ActivityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSequences(items: List<SequenceEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSequenceItems(items: List<SequenceItemEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChallenges(items: List<ConnectionChallengeEntity>)
}
