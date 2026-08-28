package com.educalab.exploravida.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.educalab.exploravida.data.local.entity.ExplorerNotebookEntity
import com.educalab.exploravida.data.local.entity.NotebookDiscoveryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotebookDao {

    @Query("SELECT * FROM explorer_notebook WHERE profileId = :profileId ORDER BY pageIndex ASC")
    fun observePages(profileId: Long = 1): Flow<List<ExplorerNotebookEntity>>

    @Query("SELECT * FROM explorer_notebook WHERE profileId = :profileId ORDER BY pageIndex ASC")
    suspend fun pages(profileId: Long = 1): List<ExplorerNotebookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPages(pages: List<ExplorerNotebookEntity>)

    @Query("UPDATE explorer_notebook SET unlocked = 1 WHERE id = :pageId")
    suspend fun unlockPage(pageId: String)

    @Query("SELECT * FROM notebook_discovery ORDER BY discoveredAt ASC")
    fun observeDiscoveries(): Flow<List<NotebookDiscoveryEntity>>

    @Query("SELECT * FROM notebook_discovery WHERE notebookId = :notebookId ORDER BY discoveredAt ASC")
    suspend fun discoveriesOf(notebookId: String): List<NotebookDiscoveryEntity>

    @Query("SELECT COUNT(*) FROM notebook_discovery")
    suspend fun discoveryCount(): Int

    @Query("SELECT COUNT(*) FROM notebook_discovery WHERE conceptKey = :conceptKey")
    suspend fun hasDiscovery(conceptKey: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDiscovery(discovery: NotebookDiscoveryEntity): Long

    /**
     * Guarda un descubrimiento y desbloquea su pagina en una sola operacion.
     * Devuelve true solo si el descubrimiento era nuevo.
     */
    @Transaction
    suspend fun saveDiscovery(discovery: NotebookDiscoveryEntity): Boolean {
        if (hasDiscovery(discovery.conceptKey) > 0) return false
        val inserted = insertDiscovery(discovery)
        if (inserted <= 0L) return false
        unlockPage(discovery.notebookId)
        return true
    }

    @Query("DELETE FROM notebook_discovery")
    suspend fun clearDiscoveries()
}
