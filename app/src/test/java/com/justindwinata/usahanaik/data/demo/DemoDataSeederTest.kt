package com.justindwinata.usahanaik.data.demo

import com.justindwinata.usahanaik.data.repository.BusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.BusinessReminderRepository
import com.justindwinata.usahanaik.data.repository.BusinessReportSnapshotRepository
import com.justindwinata.usahanaik.data.repository.ContentCalendarRepository
import com.justindwinata.usahanaik.data.repository.ContentIdeaRepository
import com.justindwinata.usahanaik.data.repository.FinancialEntryRepository
import com.justindwinata.usahanaik.data.repository.WeeklyPlanRepository
import com.justindwinata.usahanaik.data.repository.WeeklyProgressHistoryRepository
import com.justindwinata.usahanaik.data.repository.WeeklyRetrospectiveRepository
import com.justindwinata.usahanaik.domain.finance.FinancialCalculator
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.BusinessReportSnapshot
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummary
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummaryCalculator
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaFilter
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderSummary
import com.justindwinata.usahanaik.domain.model.ReminderSummaryCalculator
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyProgressHistorySummary
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DemoDataSeederTest {
    @Test
    fun loadsCompleteDemoDataset() = runTest {
        val fakes = FakeDemoRepositories()
        val result = fakes.seeder.loadDemoData()

        assertTrue(result.profileLoaded)
        assertEquals("Dapur Rasa Nusantara", fakes.profileRepository.getActiveBusinessProfile()?.draft?.businessName)
        assertEquals(8, fakes.financialRepository.listEntries().size)
        assertEquals(5, fakes.weeklyPlanRepository.getActivePlan()?.tasks?.size)
        assertEquals(4, fakes.contentIdeaRepository.listIdeas().size)
        assertEquals(4, fakes.calendarRepository.listSchedules().size)
        assertEquals(1, fakes.progressRepository.listSnapshots().size)
        assertEquals(1, fakes.retrospectiveRepository.listRetrospectives().size)
        assertEquals(1, fakes.reportRepository.listSnapshots().size)
        assertEquals(4, fakes.reminderRepository.listReminders().size)
        assertEquals(4, result.reminderCount)
    }

    @Test
    fun loadingDemoDataTwiceDoesNotDuplicateRecords() = runTest {
        val fakes = FakeDemoRepositories()

        fakes.seeder.loadDemoData()
        fakes.seeder.loadDemoData()

        assertEquals(8, fakes.financialRepository.listEntries().size)
        assertEquals(4, fakes.contentIdeaRepository.listIdeas().size)
        assertEquals(4, fakes.calendarRepository.listSchedules().size)
        assertEquals(4, fakes.reminderRepository.listReminders().size)
        assertEquals(1, fakes.reportRepository.listSnapshots().size)
    }

    @Test
    fun clearsDemoData() = runTest {
        val fakes = FakeDemoRepositories()

        fakes.seeder.loadDemoData()
        fakes.seeder.clearDemoData()

        assertFalse(fakes.profileRepository.hasBusinessProfile())
        assertEquals(0, fakes.financialRepository.listEntries().size)
        assertEquals(0, fakes.contentIdeaRepository.listIdeas().size)
        assertEquals(0, fakes.calendarRepository.listSchedules().size)
        assertEquals(0, fakes.reminderRepository.listReminders().size)
    }

    private class FakeDemoRepositories {
        val profileRepository = FakeBusinessProfileRepository()
        val financialRepository = FakeFinancialEntryRepository()
        val weeklyPlanRepository = FakeWeeklyPlanRepository()
        val contentIdeaRepository = FakeContentIdeaRepository()
        val calendarRepository = FakeContentCalendarRepository()
        val progressRepository = FakeWeeklyProgressHistoryRepository()
        val retrospectiveRepository = FakeWeeklyRetrospectiveRepository()
        val reportRepository = FakeBusinessReportSnapshotRepository()
        val reminderRepository = FakeBusinessReminderRepository()

        val seeder = DemoDataSeeder(
            businessProfileRepository = profileRepository,
            financialEntryRepository = financialRepository,
            weeklyPlanRepository = weeklyPlanRepository,
            contentIdeaRepository = contentIdeaRepository,
            contentCalendarRepository = calendarRepository,
            progressHistoryRepository = progressRepository,
            retrospectiveRepository = retrospectiveRepository,
            reportSnapshotRepository = reportRepository,
            reminderRepository = reminderRepository
        )
    }

    private class FakeBusinessProfileRepository : BusinessProfileRepository {
        private val profile = MutableStateFlow<BusinessProfile?>(null)
        override suspend fun saveBusinessProfile(draft: BusinessSetupDraft): BusinessProfile {
            val saved = BusinessProfile(draft, createdAt = 1L, updatedAt = 2L)
            profile.value = saved
            return saved
        }
        override suspend fun getActiveBusinessProfile(): BusinessProfile? = profile.value
        override fun observeActiveBusinessProfile(): Flow<BusinessProfile?> = profile
        override suspend fun deleteBusinessProfile() {
            profile.value = null
        }
        override suspend fun hasBusinessProfile(): Boolean = profile.value != null
    }

    private class FakeFinancialEntryRepository : FinancialEntryRepository {
        private val entries = MutableStateFlow<List<FinancialEntry>>(emptyList())
        private var nextId = 1L
        override suspend fun addEntry(entry: FinancialEntry): FinancialEntry {
            val saved = entry.copy(id = nextId++)
            entries.value = entries.value + saved
            return saved
        }
        override suspend fun updateEntry(entry: FinancialEntry): FinancialEntry = entry
        override suspend fun deleteEntry(id: Long) {
            entries.value = entries.value.filterNot { it.id == id }
        }
        override suspend fun getEntry(id: Long): FinancialEntry? = entries.value.firstOrNull { it.id == id }
        override suspend fun listEntries(): List<FinancialEntry> = entries.value
        override fun observeEntries(): Flow<List<FinancialEntry>> = entries
        override suspend fun listEntriesForMonth(monthPrefix: String): List<FinancialEntry> = entries.value.filter { it.date.startsWith(monthPrefix) }
        override fun observeEntriesForMonth(monthPrefix: String): Flow<List<FinancialEntry>> = entries
        override suspend fun getFinancialSummary(monthPrefix: String, targetMonthlyRevenue: String?, targetMonthlyProfit: String?): FinancialTrackingSummary {
            return FinancialCalculator.buildSummary(listEntriesForMonth(monthPrefix), targetMonthlyRevenue, targetMonthlyProfit)
        }
    }

    private class FakeWeeklyPlanRepository : WeeklyPlanRepository {
        private val plan = MutableStateFlow<WeeklyGrowthPlan?>(null)
        override suspend fun savePlan(plan: WeeklyGrowthPlan): WeeklyGrowthPlan {
            this.plan.value = plan.copy(id = 1L)
            return checkNotNull(this.plan.value)
        }
        override suspend fun getActivePlan(): WeeklyGrowthPlan? = plan.value
        override fun observeActivePlan(): Flow<WeeklyGrowthPlan?> = plan
        override suspend fun updateTaskStatus(taskId: String, status: WeeklyTaskStatus): WeeklyGrowthPlan? = plan.value
        override suspend fun updateMilestoneStatus(milestoneId: String, status: MilestoneStatus): WeeklyGrowthPlan? = plan.value
        override suspend fun deleteActivePlan() {
            plan.value = null
        }
        override suspend fun hasActivePlan(): Boolean = plan.value != null
    }

    private class FakeContentIdeaRepository : ContentIdeaRepository {
        private val ideas = MutableStateFlow<List<ContentIdea>>(emptyList())
        private var nextId = 1L
        override suspend fun saveIdea(idea: ContentIdea): ContentIdea {
            val saved = idea.copy(id = nextId++)
            ideas.value = ideas.value + saved
            return saved
        }
        override suspend fun listIdeas(filter: ContentIdeaFilter): List<ContentIdea> = when (filter) {
            ContentIdeaFilter.All -> ideas.value
            ContentIdeaFilter.Draft -> ideas.value.filter { it.status == ContentIdeaStatus.Draft }
            ContentIdeaFilter.Planned -> ideas.value.filter { it.status == ContentIdeaStatus.Planned }
            ContentIdeaFilter.Done -> ideas.value.filter { it.status == ContentIdeaStatus.Done }
            ContentIdeaFilter.Favorite -> ideas.value.filter { it.isFavorite }
        }
        override fun observeIdeas(): Flow<List<ContentIdea>> = ideas
        override suspend fun updateStatus(id: Long, status: ContentIdeaStatus): ContentIdea? = null
        override suspend fun updateFavorite(id: Long, isFavorite: Boolean): ContentIdea? = null
        override suspend fun deleteIdea(id: Long) {
            ideas.value = ideas.value.filterNot { it.id == id }
        }
        override suspend fun clearIdeas() {
            ideas.value = emptyList()
        }
    }

    private class FakeContentCalendarRepository : ContentCalendarRepository {
        private val schedules = MutableStateFlow<List<ContentCalendarSchedule>>(emptyList())
        private var nextId = 1L
        override suspend fun scheduleContent(item: ContentCalendarSchedule): ContentCalendarSchedule {
            val saved = item.copy(id = nextId++)
            schedules.value = schedules.value + saved
            return saved
        }
        override suspend fun getSchedule(id: Long): ContentCalendarSchedule? = schedules.value.firstOrNull { it.id == id }
        override suspend fun listSchedules(): List<ContentCalendarSchedule> = schedules.value
        override fun observeSchedules(): Flow<List<ContentCalendarSchedule>> = schedules
        override suspend fun listUpcoming(fromDate: String): List<ContentCalendarSchedule> = schedules.value.filter { it.scheduledDate >= fromDate }
        override suspend fun listBetween(startDate: String, endDate: String): List<ContentCalendarSchedule> = schedules.value.filter { it.scheduledDate in startDate..endDate }
        override suspend fun updateStatus(id: Long, status: ContentCalendarStatus): ContentCalendarSchedule? = null
        override suspend fun deleteSchedule(id: Long) {
            schedules.value = schedules.value.filterNot { it.id == id }
        }
        override suspend fun getSummary(startDate: String?, endDate: String?): ContentCalendarSummary = ContentCalendarSummaryCalculator.summarize(schedules.value)
    }

    private class FakeWeeklyProgressHistoryRepository : WeeklyProgressHistoryRepository {
        private val snapshots = MutableStateFlow<List<WeeklyProgressSnapshot>>(emptyList())
        override suspend fun saveSnapshot(snapshot: WeeklyProgressSnapshot): WeeklyProgressSnapshot {
            snapshots.value = listOf(snapshot.copy(id = 1L))
            return checkNotNull(snapshots.value.firstOrNull())
        }
        override suspend fun getSnapshotForWeek(weekLabel: String): WeeklyProgressSnapshot? = snapshots.value.firstOrNull { it.weekLabel == weekLabel }
        override suspend fun listSnapshots(): List<WeeklyProgressSnapshot> = snapshots.value
        override fun observeSnapshots(): Flow<List<WeeklyProgressSnapshot>> = snapshots
        override suspend fun clearSnapshots() {
            snapshots.value = emptyList()
        }
        override suspend fun getHistorySummary(): WeeklyProgressHistorySummary = WeeklyProgressHistorySummary(snapshots.value.firstOrNull(), emptyList(), 0f)
    }

    private class FakeWeeklyRetrospectiveRepository : WeeklyRetrospectiveRepository {
        private val retrospectives = MutableStateFlow<List<WeeklyRetrospective>>(emptyList())
        override suspend fun saveRetrospective(retrospective: WeeklyRetrospective): WeeklyRetrospective {
            retrospectives.value = listOf(retrospective.copy(id = 1L))
            return checkNotNull(retrospectives.value.firstOrNull())
        }
        override suspend fun getRetrospectiveForWeek(weekLabel: String): WeeklyRetrospective? = retrospectives.value.firstOrNull { it.weekLabel == weekLabel }
        override suspend fun getLatestRetrospective(): WeeklyRetrospective? = retrospectives.value.firstOrNull()
        override suspend fun listRetrospectives(): List<WeeklyRetrospective> = retrospectives.value
        override fun observeRetrospectives(): Flow<List<WeeklyRetrospective>> = retrospectives
        override suspend fun clearRetrospectives() {
            retrospectives.value = emptyList()
        }
    }

    private class FakeBusinessReportSnapshotRepository : BusinessReportSnapshotRepository {
        private val snapshots = MutableStateFlow<List<BusinessReportSnapshot>>(emptyList())
        override suspend fun saveSnapshot(snapshot: BusinessReportSnapshot): BusinessReportSnapshot {
            snapshots.value = listOf(snapshot.copy(id = 1L))
            return checkNotNull(snapshots.value.firstOrNull())
        }
        override suspend fun getSnapshot(id: Long): BusinessReportSnapshot? = snapshots.value.firstOrNull { it.id == id }
        override suspend fun getLatestSnapshot(): BusinessReportSnapshot? = snapshots.value.firstOrNull()
        override suspend fun listSnapshots(): List<BusinessReportSnapshot> = snapshots.value
        override fun observeSnapshots(): Flow<List<BusinessReportSnapshot>> = snapshots
        override suspend fun deleteSnapshot(id: Long) {
            snapshots.value = snapshots.value.filterNot { it.id == id }
        }
        override suspend fun clearSnapshots() {
            snapshots.value = emptyList()
        }
    }

    private class FakeBusinessReminderRepository : BusinessReminderRepository {
        private val reminders = MutableStateFlow<List<BusinessReminder>>(emptyList())
        private var nextId = 1L

        override suspend fun createReminder(reminder: BusinessReminder): BusinessReminder {
            val saved = reminder.copy(id = nextId++)
            reminders.value = reminders.value + saved
            return saved
        }

        override suspend fun updateReminder(reminder: BusinessReminder): BusinessReminder {
            reminders.value = reminders.value.map { if (it.id == reminder.id) reminder else it }
            return reminder
        }

        override suspend fun getReminder(id: Long): BusinessReminder? = reminders.value.firstOrNull { it.id == id }

        override suspend fun listReminders(): List<BusinessReminder> = reminders.value

        override suspend fun listActiveReminders(): List<BusinessReminder> {
            return reminders.value.filter { it.status == ReminderStatus.Active }
        }

        override fun observeReminders(): Flow<List<BusinessReminder>> = reminders

        override suspend fun updateStatus(id: Long, status: ReminderStatus): BusinessReminder? {
            reminders.value = reminders.value.map { if (it.id == id) it.copy(status = status) else it }
            return getReminder(id)
        }

        override suspend fun pauseReminder(id: Long): BusinessReminder? = updateStatus(id, ReminderStatus.Paused)

        override suspend fun enableReminder(id: Long): BusinessReminder? = updateStatus(id, ReminderStatus.Active)

        override suspend fun deleteReminder(id: Long) {
            reminders.value = reminders.value.filterNot { it.id == id }
        }

        override suspend fun clearReminders() {
            reminders.value = emptyList()
        }

        override suspend fun getReminderSummary(): ReminderSummary {
            return ReminderSummaryCalculator.summarize(reminders.value)
        }
    }
}
