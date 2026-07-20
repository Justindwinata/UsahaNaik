package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.local.WeeklyRetrospectiveDao
import com.justindwinata.usahanaik.data.local.WeeklyRetrospectiveEntity
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.NextWeekSuggestion
import com.justindwinata.usahanaik.domain.model.RetrospectiveInsight
import com.justindwinata.usahanaik.domain.model.RetrospectiveSection
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LocalWeeklyRetrospectiveRepositoryTest {
    @Test
    fun savesAndLoadsRetrospective() = runTest {
        val repository = LocalWeeklyRetrospectiveRepository(FakeWeeklyRetrospectiveDao(), nowProvider = { 100L })

        val saved = repository.saveRetrospective(sampleRetrospective())

        assertEquals(1L, saved.id)
        assertEquals("Week 30", repository.getRetrospectiveForWeek("Week 30")?.weekLabel)
        assertEquals("What improved", saved.sections.first().title)
    }

    @Test
    fun replacesRetrospectiveForSameWeek() = runTest {
        val repository = LocalWeeklyRetrospectiveRepository(FakeWeeklyRetrospectiveDao(), nowProvider = { 100L })
        repository.saveRetrospective(sampleRetrospective(summary = "First"))

        repository.saveRetrospective(sampleRetrospective(summary = "Second"))

        val retrospectives = repository.listRetrospectives()
        assertEquals(1, retrospectives.size)
        assertEquals("Second", retrospectives.first().summaryTitle)
    }

    @Test
    fun returnsLatestRetrospective() = runTest {
        val repository = LocalWeeklyRetrospectiveRepository(FakeWeeklyRetrospectiveDao(), nowProvider = { 100L })
        repository.saveRetrospective(sampleRetrospective(week = "Week 29", date = "2026-07-13"))
        repository.saveRetrospective(sampleRetrospective(week = "Week 30", date = "2026-07-20"))

        assertEquals("Week 30", repository.getLatestRetrospective()?.weekLabel)
    }

    @Test
    fun clearsRetrospectives() = runTest {
        val repository = LocalWeeklyRetrospectiveRepository(FakeWeeklyRetrospectiveDao(), nowProvider = { 100L })
        repository.saveRetrospective(sampleRetrospective())

        repository.clearRetrospectives()

        assertNull(repository.getLatestRetrospective())
    }

    private fun sampleRetrospective(
        week: String = "Week 30",
        date: String = "2026-07-20",
        summary: String = "Weekly retrospective"
    ): WeeklyRetrospective {
        return WeeklyRetrospective(
            weekLabel = week,
            generatedDate = date,
            summaryTitle = summary,
            sections = listOf(
                RetrospectiveSection(
                    title = "What improved",
                    insights = listOf(RetrospectiveInsight("You completed tasks.", InsightSeverity.Positive))
                )
            ),
            nextWeekSuggestion = NextWeekSuggestion(
                focus = "Keep tracking",
                reason = "Progress was recorded.",
                recommendedAction = "Save another snapshot next week."
            )
        )
    }

    private class FakeWeeklyRetrospectiveDao : WeeklyRetrospectiveDao {
        private val retrospectives = MutableStateFlow<List<WeeklyRetrospectiveEntity>>(emptyList())
        private var nextId = 1L

        override suspend fun insertRetrospective(retrospective: WeeklyRetrospectiveEntity): Long {
            val id = if (retrospective.id == 0L) nextId++ else retrospective.id
            retrospectives.value = retrospectives.value.filterNot { it.id == id } + retrospective.copy(id = id)
            return id
        }

        override suspend fun getRetrospective(id: Long): WeeklyRetrospectiveEntity? {
            return retrospectives.value.firstOrNull { it.id == id }
        }

        override suspend fun getRetrospectiveForWeek(weekLabel: String): WeeklyRetrospectiveEntity? {
            return retrospectives.value.firstOrNull { it.weekLabel == weekLabel }
        }

        override suspend fun listRetrospectives(): List<WeeklyRetrospectiveEntity> {
            return retrospectives.value.sortedWith(compareByDescending<WeeklyRetrospectiveEntity> { it.generatedDate }.thenByDescending { it.updatedAt })
        }

        override suspend fun getLatestRetrospective(): WeeklyRetrospectiveEntity? = listRetrospectives().firstOrNull()

        override fun observeRetrospectives(): Flow<List<WeeklyRetrospectiveEntity>> = retrospectives

        override suspend fun deleteRetrospectiveForWeek(weekLabel: String) {
            retrospectives.value = retrospectives.value.filterNot { it.weekLabel == weekLabel }
        }

        override suspend fun clearRetrospectives() {
            retrospectives.value = emptyList()
        }
    }
}
