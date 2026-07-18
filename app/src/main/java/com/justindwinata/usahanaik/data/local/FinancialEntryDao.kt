package com.justindwinata.usahanaik.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: FinancialEntryEntity): Long

    @Update
    suspend fun updateEntry(entry: FinancialEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: FinancialEntryEntity)

    @Query("DELETE FROM financial_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)

    @Query("SELECT * FROM financial_entries WHERE id = :id LIMIT 1")
    suspend fun getEntry(id: Long): FinancialEntryEntity?

    @Query("SELECT * FROM financial_entries ORDER BY date DESC, updatedAt DESC")
    suspend fun listEntries(): List<FinancialEntryEntity>

    @Query("SELECT * FROM financial_entries ORDER BY date DESC, updatedAt DESC")
    fun observeEntries(): Flow<List<FinancialEntryEntity>>

    @Query("SELECT * FROM financial_entries WHERE date LIKE :monthPrefix || '%' ORDER BY date DESC, updatedAt DESC")
    suspend fun listEntriesForMonth(monthPrefix: String): List<FinancialEntryEntity>

    @Query("SELECT * FROM financial_entries WHERE date LIKE :monthPrefix || '%' ORDER BY date DESC, updatedAt DESC")
    fun observeEntriesForMonth(monthPrefix: String): Flow<List<FinancialEntryEntity>>
}
