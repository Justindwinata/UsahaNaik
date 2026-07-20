package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.WeeklyProgressSnapshotDao
import com.justindwinata.usahanaik.data.local.WeeklyProgressSnapshotEntity
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LocalWeeklyProgressHistoryRepositoryTest {
    @Test
    fun savesAndReadsSnapshot() = runTest {
        val repository = LocalWeeklyProgressHistoryRepository(FakeWeeklyProgressSnapshotDao(), nowProvider = { 100L })

        val saved = repository.saveSnapshot(sampleSnapshot())

        assertEquals(1L, saved.id)
        assertEquals("Week 30", repository.getSnapshotForWeek("Week 30")?.weekLabel)
    }

    @Test
    fun replacesExistingSnapshotForSameWeek() = runTest {
        val repository = LocalWeeklyProgressHistoryRepository(FakeWeeklyProgressSnapshotDao(), nowProvider = { 100L })
        repository.saveSnapshot(sampleSnapshot(completedTasks = 1))

        repository.saveSnapshot(sampleSnapshot(completedTasks = 4))

        val snapshots = repository.listSnapshots()
        assertEquals(1, snapshots.size)
        assertEquals(4, snapshots.first().completedTasks)
    }

    @Test
    fun buildsHistorySummary() = runTest {
        val repository = LocalWeeklyProgressHistoryRepository(FakeWeeklyProgressSnapshotDao(), nowProvider = { 100L })
        repository.saveSnapshot(sampleSnapshot(week = "Week 29", start = "2026-07-13", completedTasks = 1))
        repository.saveSnapshot(sampleSnapshot(week = "Week 30", start = "2026-07-20", completedTasks = 3))

        val summary = repository.getHistorySummary()

        assertEquals("Week 30", summary.latestSnapshot?.weekLabel)
        assertEquals(2, summary.trendPoints.size)
    }

    @Test
    fun clearsSnapshots() = runTest {
        val repository = LocalWeeklyProgressHistoryRepository(FakeWeeklyProgressSnapshotDao(), nowProvider = { 100L })
        repository.saveSnapshot(sampleSnapshot())

        repository.clearSnapshots()

        assertNull(repository.getSnapshotForWeek("Week 30"))
    }

    private fun sampleSnapshot(
        week: String = "Week 30",
        start: String = "2026-07-20",
        completedTasks: Int = 2
    ): WeeklyProgressSnapshot {
        return WeeklyProgressSnapshot(
            weekLabel = week,
            weekStartDate = start,
            totalTasks = 4,
            completedTasks = completedTasks,
            taskCompletionRate = completedTasks / 4f,
            milestoneProgress = 0.5f,
            weeklyIncome = 1_000_000L,
            weeklyExpenses = 500_000L,
            weeklyEstimatedProfit = 500_000L,
            profitMarginPercent = 50,
            savedIdeasCount = 5,
            plannedContentCount = 2,
            postedOrDoneContentCount = 1,
            skippedContentCount = 0,
            businessHealthScore = 72,
            warningInsightCount = 1,
            criticalInsightCount = 0
        )
    }

    private class FakeWeeklyProgressSnapshotDao : WeeklyProgressSnapshotDao {
        private val snapshots = MutableStateFlow<List<WeeklyProgressSnapshotEntity>>(emptyList())
        private var nextId = 1L

        override suspend fun insertSnapshot(snapshot: WeeklyProgressSnapshotEntity): Long {
            val id = if (snapshot.id == 0L) nextId++ else snapshot.id
            snapshots.value = snapshots.value.filterNot { it.id == id } + snapshot.copy(id = id)
            return id
        }

        override suspend fun getSnapshot(id: Long): WeeklyProgressSnapshotEntity? {
            return snapshots.value.firstOrNull { it.id == id }
        }

        override suspend fun getSnapshotByWeek(weekLabel: String): WeeklyProgressSnapshotEntity? {
            return snapshots.value.firstOrNull { it.weekLabel == weekLabel }
        }

        override suspend fun listSnapshots(): List<WeeklyProgressSnapshotEntity> {
            return snapshots.value.sortedByDescending { it.weekStartDate }
        }

        override fun observeSnapshots(): Flow<List<WeeklyProgressSnapshotEntity>> = snapshots

        override suspend fun deleteSnapshotByWeek(weekLabel: String) {
            snapshots.value = snapshots.value.filterNot { it.weekLabel == weekLabel }
        }

        override suspend fun clearSnapshots() {
            snapshots.value = emptyList()
        }
    }
}
