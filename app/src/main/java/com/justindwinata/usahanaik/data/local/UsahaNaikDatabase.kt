package com.justindwinata.usahanaik.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [BusinessProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UsahaNaikDatabase : RoomDatabase() {
    abstract fun businessProfileDao(): BusinessProfileDao

    companion object {
        private const val DATABASE_NAME = "usahanaik.db"

        @Volatile
        private var instance: UsahaNaikDatabase? = null

        fun getDatabase(context: Context): UsahaNaikDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    UsahaNaikDatabase::class.java,
                    DATABASE_NAME
                ).build().also { instance = it }
            }
        }
    }
}
