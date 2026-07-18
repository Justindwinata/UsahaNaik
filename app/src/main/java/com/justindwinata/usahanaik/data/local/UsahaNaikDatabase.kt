package com.justindwinata.usahanaik.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        BusinessProfileEntity::class,
        FinancialEntryEntity::class,
        WeeklyGrowthPlanEntity::class,
        WeeklyTaskEntity::class,
        WeeklyMilestoneEntity::class,
        ContentIdeaEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class UsahaNaikDatabase : RoomDatabase() {
    abstract fun businessProfileDao(): BusinessProfileDao
    abstract fun financialEntryDao(): FinancialEntryDao
    abstract fun weeklyPlanDao(): WeeklyPlanDao
    abstract fun contentIdeaDao(): ContentIdeaDao

    companion object {
        private const val DATABASE_NAME = "usahanaik.db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS financial_entries (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        type TEXT NOT NULL,
                        title TEXT NOT NULL,
                        amount INTEGER NOT NULL,
                        category TEXT NOT NULL,
                        date TEXT NOT NULL,
                        note TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS weekly_growth_plans (
                        id INTEGER PRIMARY KEY NOT NULL,
                        title TEXT NOT NULL,
                        generatedDate TEXT NOT NULL,
                        businessName TEXT NOT NULL,
                        businessCategoryId TEXT,
                        businessCategoryName TEXT NOT NULL,
                        focusTitle TEXT NOT NULL,
                        focusCategory TEXT NOT NULL,
                        focusReason TEXT NOT NULL,
                        target TEXT NOT NULL,
                        priorityReason TEXT NOT NULL,
                        challengeTitle TEXT NOT NULL,
                        challengeDescription TEXT NOT NULL,
                        challengeChecklistItems TEXT NOT NULL,
                        challengeCompletionTarget TEXT NOT NULL,
                        challengeMotivationalCopy TEXT NOT NULL,
                        status TEXT NOT NULL,
                        limitationsNote TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS weekly_tasks (
                        id TEXT PRIMARY KEY NOT NULL,
                        planId INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        category TEXT NOT NULL,
                        estimatedTime TEXT NOT NULL,
                        difficulty TEXT NOT NULL,
                        status TEXT NOT NULL,
                        reason TEXT NOT NULL,
                        expectedOutcome TEXT NOT NULL,
                        sortOrder INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS weekly_milestones (
                        id TEXT PRIMARY KEY NOT NULL,
                        planId INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        status TEXT NOT NULL,
                        relatedTaskIds TEXT NOT NULL,
                        progressPercentage INTEGER NOT NULL,
                        sortOrder INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS content_ideas (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        businessProfileId INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        platform TEXT NOT NULL,
                        contentType TEXT NOT NULL,
                        goal TEXT NOT NULL,
                        angle TEXT NOT NULL,
                        captionDraft TEXT NOT NULL,
                        cta TEXT NOT NULL,
                        hook TEXT NOT NULL,
                        visualSuggestion TEXT NOT NULL,
                        postingNote TEXT NOT NULL,
                        relatedChallenge TEXT,
                        source TEXT NOT NULL,
                        safetyNote TEXT NOT NULL,
                        status TEXT NOT NULL,
                        isFavorite INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        @Volatile
        private var instance: UsahaNaikDatabase? = null

        fun getDatabase(context: Context): UsahaNaikDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    UsahaNaikDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                    .also { instance = it }
            }
        }
    }
}
