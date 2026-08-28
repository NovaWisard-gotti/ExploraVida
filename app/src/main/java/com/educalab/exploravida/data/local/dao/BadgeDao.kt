package com.educalab.exploravida.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.educalab.exploravida.data.local.entity.BadgeEntity
import com.educalab.exploravida.data.local.entity.UserBadgeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {

    @Query("SELECT * FROM badge")
    fun observeBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM badge")
    suspend fun badges(): List<BadgeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(items: List<BadgeEntity>)

    @Query("SELECT * FROM user_badge WHERE profileId = :profileId")
    fun observeUserBadges(profileId: Long = 1): Flow<List<UserBadgeEntity>>

    @Query("SELECT badgeId FROM user_badge WHERE profileId = :profileId")
    suspend fun earnedIds(profileId: Long = 1): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun award(badge: UserBadgeEntity): Long

    /** Entrega varias insignias evitando duplicados. Devuelve las realmente nuevas. */
    @Transaction
    suspend fun awardAll(badgeIds: List<String>, profileId: Long, now: Long): List<String> {
        val already = earnedIds(profileId).toSet()
        val fresh = badgeIds.distinct().filter { it !in already }
        fresh.forEach { award(UserBadgeEntity(badgeId = it, profileId = profileId, earnedAt = now)) }
        return fresh
    }

    @Query("DELETE FROM user_badge WHERE profileId = :profileId")
    suspend fun clearUserBadges(profileId: Long = 1)
}
