package com.educalab.exploravida.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.educalab.exploravida.data.local.converters.Converters
import com.educalab.exploravida.data.local.dao.BadgeDao
import com.educalab.exploravida.data.local.dao.ContentDao
import com.educalab.exploravida.data.local.dao.NotebookDao
import com.educalab.exploravida.data.local.dao.ProfileDao
import com.educalab.exploravida.data.local.dao.ProgressDao
import com.educalab.exploravida.data.local.entity.ActivityAttemptEntity
import com.educalab.exploravida.data.local.entity.ActivityEntity
import com.educalab.exploravida.data.local.entity.BadgeEntity
import com.educalab.exploravida.data.local.entity.ConnectionChallengeEntity
import com.educalab.exploravida.data.local.entity.ExperienceStepEntity
import com.educalab.exploravida.data.local.entity.ExplorerNotebookEntity
import com.educalab.exploravida.data.local.entity.InteractiveElementEntity
import com.educalab.exploravida.data.local.entity.LearningExperienceEntity
import com.educalab.exploravida.data.local.entity.LivingSystemEntity
import com.educalab.exploravida.data.local.entity.NotebookDiscoveryEntity
import com.educalab.exploravida.data.local.entity.ProgressEntity
import com.educalab.exploravida.data.local.entity.SequenceEntity
import com.educalab.exploravida.data.local.entity.SequenceItemEntity
import com.educalab.exploravida.data.local.entity.SystemConnectionEntity
import com.educalab.exploravida.data.local.entity.UnlockedExperienceEntity
import com.educalab.exploravida.data.local.entity.UserBadgeEntity
import com.educalab.exploravida.data.local.entity.UserProfileEntity
import com.educalab.exploravida.data.local.seed.SeedActivities
import com.educalab.exploravida.data.local.seed.SeedContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfileEntity::class,
        LivingSystemEntity::class,
        SystemConnectionEntity::class,
        LearningExperienceEntity::class,
        ExperienceStepEntity::class,
        InteractiveElementEntity::class,
        ActivityEntity::class,
        ActivityAttemptEntity::class,
        SequenceEntity::class,
        SequenceItemEntity::class,
        ConnectionChallengeEntity::class,
        ExplorerNotebookEntity::class,
        NotebookDiscoveryEntity::class,
        ProgressEntity::class,
        BadgeEntity::class,
        UserBadgeEntity::class,
        UnlockedExperienceEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ExploraVidaDatabase : RoomDatabase() {

    abstract fun contentDao(): ContentDao
    abstract fun profileDao(): ProfileDao
    abstract fun progressDao(): ProgressDao
    abstract fun notebookDao(): NotebookDao
    abstract fun badgeDao(): BadgeDao

    companion object {
        const val NAME = "exploravida.db"

        @Volatile
        private var instance: ExploraVidaDatabase? = null

        fun get(context: Context): ExploraVidaDatabase =
            instance ?: synchronized(this) {
                instance ?: build(context.applicationContext).also { instance = it }
            }

        private fun build(context: Context): ExploraVidaDatabase {
            val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
            return Room.databaseBuilder(context, ExploraVidaDatabase::class.java, NAME)
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        scope.launch { seed(get(context)) }
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        db.execSQL("PRAGMA foreign_keys = ON;")
                        scope.launch { seedIfEmpty(get(context)) }
                    }
                })
                .build()
        }

        /** Inserta el contenido educativo. Es idempotente. */
        suspend fun seed(database: ExploraVidaDatabase) {
            database.contentDao().seedContent(
                systems = SeedContent.systems,
                connections = SeedContent.connections,
                experiences = SeedContent.experiences,
                steps = SeedContent.steps,
                elements = SeedContent.elements,
                activities = SeedActivities.activities,
                sequences = SeedActivities.sequences,
                sequenceItems = SeedActivities.sequenceItems,
                challenges = SeedActivities.connectionChallenges
            )
            database.badgeDao().insertBadges(SeedActivities.badges)
        }

        /** Red de seguridad: si la base existe pero quedo vacia, se vuelve a sembrar. */
        suspend fun seedIfEmpty(database: ExploraVidaDatabase) {
            if (database.contentDao().systems().isEmpty()) seed(database)
        }
    }
}
