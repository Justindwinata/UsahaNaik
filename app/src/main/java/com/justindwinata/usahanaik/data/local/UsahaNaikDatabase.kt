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
        ContentIdeaEntity::class,
        ContentCalendarEntity::class,
        WeeklyProgressSnapshotEntity::class,
        WeeklyRetrospectiveEntity::class,
        BusinessReportSnapshotEntity::class,
        BusinessReminderEntity::class
    ],
    version = 9,
    exportSchema = false
)
abstract class UsahaNaikDatabase : RoomDatabase() {
    abstract fun businessProfileDao(): BusinessProfileDao
    abstract fun financialEntryDao(): FinancialEntryDao
    abstract fun weeklyPlanDao(): WeeklyPlanDao
    abstract fun contentIdeaDao(): ContentIdeaDao
    abstract fun contentCalendarDao(): ContentCalendarDao
    abstract fun weeklyProgressSnapshotDao(): WeeklyProgressSnapshotDao
    abstract fun weeklyRetrospectiveDao(): WeeklyRetrospectiveDao
    abstract fun businessReportSnapshotDao(): BusinessReportSnapshotDao
    abstract fun businessReminderDao(): BusinessReminderDao

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

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS content_calendar_items (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        contentIdeaId INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        platform TEXT NOT NULL,
                        scheduledDate TEXT NOT NULL,
                        timeLabel TEXT NOT NULL,
                        postingNote TEXT NOT NULL,
                        status TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS weekly_progress_snapshots (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        weekLabel TEXT NOT NULL,
                        weekStartDate TEXT NOT NULL,
                        totalTasks INTEGER NOT NULL,
                        completedTasks INTEGER NOT NULL,
                        taskCompletionRate REAL NOT NULL,
                        milestoneProgress REAL NOT NULL,
                        weeklyIncome INTEGER NOT NULL,
                        weeklyExpenses INTEGER NOT NULL,
                        weeklyEstimatedProfit INTEGER NOT NULL,
                        profitMarginPercent INTEGER NOT NULL,
                        savedIdeasCount INTEGER NOT NULL,
                        plannedContentCount INTEGER NOT NULL,
                        postedOrDoneContentCount INTEGER NOT NULL,
                        skippedContentCount INTEGER NOT NULL,
                        businessHealthScore INTEGER NOT NULL,
                        warningInsightCount INTEGER NOT NULL,
                        criticalInsightCount INTEGER NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS weekly_retrospectives (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        weekLabel TEXT NOT NULL,
                        generatedDate TEXT NOT NULL,
                        summaryTitle TEXT NOT NULL,
                        sections TEXT NOT NULL,
                        nextWeekFocus TEXT NOT NULL,
                        nextWeekReason TEXT NOT NULL,
                        nextWeekRecommendedAction TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS business_report_snapshots (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        period TEXT NOT NULL,
                        businessName TEXT NOT NULL,
                        generatedAt TEXT NOT NULL,
                        headlineSummary TEXT NOT NULL,
                        exportReadyText TEXT NOT NULL,
                        healthScore INTEGER NOT NULL,
                        totalRevenue INTEGER NOT NULL,
                        totalExpenses INTEGER NOT NULL,
                        estimatedProfit INTEGER NOT NULL,
                        taskCompletionRate REAL NOT NULL,
                        contentExecutionRate REAL NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS business_reminders (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        type TEXT NOT NULL,
                        frequency TEXT NOT NULL,
                        scheduledDay TEXT NOT NULL,
                        scheduledDate TEXT NOT NULL,
                        timeLabel TEXT NOT NULL,
                        status TEXT NOT NULL,
                        relatedEntityId INTEGER,
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
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8,
                        MIGRATION_8_9
                    )
                    .build()
                    .also { instance = it }
            }
        }
    }
}
