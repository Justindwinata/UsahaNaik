package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.BusinessReportSnapshotDao
import com.justindwinata.usahanaik.data.local.BusinessReportSnapshotEntity
import com.justindwinata.usahanaik.domain.model.BusinessReportPeriod
import com.justindwinata.usahanaik.domain.model.BusinessReportSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LocalBusinessReportSnapshotRepositoryTest {
    @Test
    fun savesAndListsReportSnapshot() = runTest {
        val repository = LocalBusinessReportSnapshotRepository(FakeBusinessReportSnapshotDao(), nowProvider = { 100L })

        val saved = repository.saveSnapshot(sampleSnapshot())

        assertEquals(1L, saved.id)
        assertEquals(1, repository.listSnapshots().size)
        assertEquals(100L, saved.createdAt)
        assertEquals(100L, saved.updatedAt)
    }

    @Test
    fun returnsLatestSnapshot() = runTest {
        val repository = LocalBusinessReportSnapshotRepository(FakeBusinessReportSnapshotDao(), nowProvider = { 100L })

        repository.saveSnapshot(sampleSnapshot(generatedAt = "2026-07-01"))
        val newer = repository.saveSnapshot(sampleSnapshot(generatedAt = "2026-07-20", businessName = "Toko Maju"))

        assertEquals(newer.id, repository.getLatestSnapshot()?.id)
        assertEquals("Toko Maju", repository.getLatestSnapshot()?.businessName)
    }

    @Test
    fun deletesSnapshot() = runTest {
        val dao = FakeBusinessReportSnapshotDao()
        val repository = LocalBusinessReportSnapshotRepository(dao, nowProvider = { 100L })
        val saved = repository.saveSnapshot(sampleSnapshot())

        repository.deleteSnapshot(saved.id)

        assertNull(repository.getSnapshot(saved.id))
    }

    @Test
    fun clearsSnapshots() = runTest {
        val repository = LocalBusinessReportSnapshotRepository(FakeBusinessReportSnapshotDao(), nowProvider = { 100L })
        repository.saveSnapshot(sampleSnapshot())
        repository.saveSnapshot(sampleSnapshot(businessName = "Warung Sari"))

        repository.clearSnapshots()

        assertEquals(emptyList<BusinessReportSnapshot>(), repository.listSnapshots())
    }

    private fun sampleSnapshot(
        generatedAt: String = "2026-07-20",
        businessName: String = "Kedai Naik"
    ): BusinessReportSnapshot = BusinessReportSnapshot(
        period = BusinessReportPeriod.ThisMonth,
        businessName = businessName,
        generatedAt = generatedAt,
        headlineSummary = "Revenue and execution summary.",
        exportReadyText = "UsahaNaik Business Report",
        healthScore = 72,
        totalRevenue = 3_000_000,
        totalExpenses = 1_500_000,
        estimatedProfit = 1_500_000,
        taskCompletionRate = 0.5f,
        contentExecutionRate = 0.4f
    )

    private class FakeBusinessReportSnapshotDao : BusinessReportSnapshotDao {
        private val snapshots = MutableStateFlow<List<BusinessReportSnapshotEntity>>(emptyList())
        private var nextId = 1L

        override suspend fun insertSnapshot(snapshot: BusinessReportSnapshotEntity): Long {
            val id = if (snapshot.id == 0L) nextId++ else snapshot.id
            snapshots.value = snapshots.value.filterNot { it.id == id } + snapshot.copy(id = id)
            return id
        }

        override suspend fun getSnapshot(id: Long): BusinessReportSnapshotEntity? {
            return snapshots.value.firstOrNull { it.id == id }
        }

        override suspend fun listSnapshots(): List<BusinessReportSnapshotEntity> {
            return snapshots.value.sortedWith(
                compareByDescending<BusinessReportSnapshotEntity> { it.generatedAt }
                    .thenByDescending { it.updatedAt }
            )
        }

        override suspend fun getLatestSnapshot(): BusinessReportSnapshotEntity? {
            return listSnapshots().firstOrNull()
        }

        override fun observeSnapshots(): Flow<List<BusinessReportSnapshotEntity>> = snapshots

        override suspend fun deleteSnapshot(id: Long) {
            snapshots.value = snapshots.value.filterNot { it.id == id }
        }

        override suspend fun clearSnapshots() {
            snapshots.value = emptyList()
        }
    }
}
