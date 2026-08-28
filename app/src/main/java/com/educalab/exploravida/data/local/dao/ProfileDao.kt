package com.educalab.exploravida.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.educalab.exploravida.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT * FROM user_profile WHERE id = :id")
    fun observeProfile(id: Long = 1): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = :id")
    suspend fun profile(id: Long = 1): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: UserProfileEntity)

    @Update
    suspend fun update(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET alias = :alias, avatarId = :avatarId WHERE id = :id")
    suspend fun updateIdentity(alias: String, avatarId: Int, id: Long = 1)

    @Query("UPDATE user_profile SET soundEnabled = :sound, hapticsEnabled = :haptics WHERE id = :id")
    suspend fun updatePreferences(sound: Boolean, haptics: Boolean, id: Long = 1)

    @Query("UPDATE user_profile SET onboardingDone = :done WHERE id = :id")
    suspend fun setOnboardingDone(done: Boolean, id: Long = 1)

    @Query("DELETE FROM user_profile")
    suspend fun clear()
}
