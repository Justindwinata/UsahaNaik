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
import com.justindwinata.usahanaik.data.reminder.ReminderScheduler
import com.justindwinata.usahanaik.domain.model.ActionDifficulty
import com.justindwinata.usahanaik.domain.model.ActionEstimatedTime
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessMilestone
import com.justindwinata.usahanaik.domain.model.BusinessReminder
import com.justindwinata.usahanaik.domain.model.BusinessReportPeriod
import com.justindwinata.usahanaik.domain.model.BusinessReportSnapshot
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessStage
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentGenerationSource
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.ContentIdeaType
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.NextWeekSuggestion
import com.justindwinata.usahanaik.domain.model.RetrospectiveInsight
import com.justindwinata.usahanaik.domain.model.RetrospectiveSection
import com.justindwinata.usahanaik.domain.model.ReminderFrequency
import com.justindwinata.usahanaik.domain.model.ReminderStatus
import com.justindwinata.usahanaik.domain.model.ReminderType
import com.justindwinata.usahanaik.domain.model.SellingChannel
import com.justindwinata.usahanaik.domain.model.StockIssue
import com.justindwinata.usahanaik.domain.model.WeeklyChallenge
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyPlanFocus
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import com.justindwinata.usahanaik.domain.model.WeeklyTask
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus

data class DemoDataResult(
    val profileLoaded: Boolean,
    val financialEntryCount: Int,
    val weeklyTaskCount: Int,
    val contentIdeaCount: Int,
    val scheduledContentCount: Int,
    val reminderCount: Int,
    val reportSnapshotCount: Int
)

class DemoDataSeeder(
    private val businessProfileRepository: BusinessProfileRepository,
    private val financialEntryRepository: FinancialEntryRepository,
    private val weeklyPlanRepository: WeeklyPlanRepository,
    private val contentIdeaRepository: ContentIdeaRepository,
    private val contentCalendarRepository: ContentCalendarRepository,
    private val progressHistoryRepository: WeeklyProgressHistoryRepository,
    private val retrospectiveRepository: WeeklyRetrospectiveRepository,
    private val reportSnapshotRepository: BusinessReportSnapshotRepository,
    private val reminderRepository: BusinessReminderRepository,
    private val reminderScheduler: ReminderScheduler? = null
) {
    suspend fun loadDemoData(): DemoDataResult {
        clearDemoData()
        businessProfileRepository.saveBusinessProfile(demoDraft)
        demoFinancialEntries.forEach { financialEntryRepository.addEntry(it) }
        val plan = weeklyPlanRepository.savePlan(demoWeeklyPlan)
        val savedIdeas = demoContentIdeas.map { contentIdeaRepository.saveIdea(it) }
        demoCalendarItems(savedIdeas).forEach { contentCalendarRepository.scheduleContent(it) }
        progressHistoryRepository.saveSnapshot(demoProgressSnapshot)
        retrospectiveRepository.saveRetrospective(demoRetrospective)
        reportSnapshotRepository.saveSnapshot(demoReportSnapshot)
        val savedReminders = demoReminders.map { reminderRepository.createReminder(it) }
        savedReminders.filter { it.isActive }.forEach { reminderScheduler?.schedule(it) }

        return DemoDataResult(
            profileLoaded = true,
            financialEntryCount = demoFinancialEntries.size,
            weeklyTaskCount = plan.tasks.size,
            contentIdeaCount = savedIdeas.size,
            scheduledContentCount = savedIdeas.size.coerceAtMost(4),
            reminderCount = demoReminders.size,
            reportSnapshotCount = 1
        )
    }

    suspend fun clearDemoData() {
        contentCalendarRepository.listSchedules().forEach { contentCalendarRepository.deleteSchedule(it.id) }
        contentIdeaRepository.clearIdeas()
        weeklyPlanRepository.deleteActivePlan()
        financialEntryRepository.listEntries().forEach { financialEntryRepository.deleteEntry(it.id) }
        progressHistoryRepository.clearSnapshots()
        retrospectiveRepository.clearRetrospectives()
        reportSnapshotRepository.clearSnapshots()
        reminderRepository.clearReminders()
        businessProfileRepository.deleteBusinessProfile()
    }

    companion object {
        val demoDraft = BusinessSetupDraft(
            businessName = "Dapur Rasa Nusantara",
            ownerName = "Ibu Ratna",
            categoryId = "food_beverage",
            sellingChannel = SellingChannel.MixedOnlineOffline,
            businessLocation = "Bandung",
            businessStage = BusinessStage.RunningSixToTwelveMonths,
            startingCapital = "Rp 25.000.000",
            monthlyRevenue = "Rp 18.500.000",
            monthlyExpenses = "Rp 13.200.000",
            estimatedMonthlyProfit = "Rp 5.300.000",
            averageDailyTransactions = "42",
            averageTransactionValue = "Rp 22.000",
            productCount = "18",
            bestSellingProduct = "Nasi ayam sambal matah",
            highestMarginProduct = "Paket rice bowl hemat",
            mainCostDriver = CostDriver.RawMaterials,
            stockIssue = StockIssue.SlowMovingStock,
            challenges = setOf(
                BusinessChallenge.LowSales,
                BusinessChallenge.InconsistentContent,
                BusinessChallenge.HighExpenses
            ),
            targetMonthlyRevenue = "Rp 24.000.000",
            targetMonthlyProfit = "Rp 7.500.000",
            mainFocus = MonthlyFocus.ImproveSales,
            availableTime = AvailableTime.SixToTenHours
        )

        val demoFinancialEntries = listOf(
            FinancialEntry(type = FinancialEntryType.Income, title = "Demo - dine-in sales", amount = 3_250_000, category = "Offline sales", date = "2026-07-14", note = "Sample demo data"),
            FinancialEntry(type = FinancialEntryType.Income, title = "Demo - online orders", amount = 2_850_000, category = "Online sales", date = "2026-07-15", note = "Sample demo data"),
            FinancialEntry(type = FinancialEntryType.Income, title = "Demo - lunch bundles", amount = 3_600_000, category = "Product sales", date = "2026-07-17", note = "Sample demo data"),
            FinancialEntry(type = FinancialEntryType.Income, title = "Demo - repeat customer orders", amount = 1_950_000, category = "Repeat order", date = "2026-07-19", note = "Sample demo data"),
            FinancialEntry(type = FinancialEntryType.Expense, title = "Demo - raw materials", amount = 3_850_000, category = "Raw materials", date = "2026-07-14", note = "Sample demo data"),
            FinancialEntry(type = FinancialEntryType.Expense, title = "Demo - packaging", amount = 720_000, category = "Packaging", date = "2026-07-15", note = "Sample demo data"),
            FinancialEntry(type = FinancialEntryType.Expense, title = "Demo - ads promotion", amount = 650_000, category = "Ads / promotion", date = "2026-07-16", note = "Sample demo data"),
            FinancialEntry(type = FinancialEntryType.Expense, title = "Demo - utilities", amount = 480_000, category = "Utilities", date = "2026-07-18", note = "Sample demo data")
        )

        val demoWeeklyPlan = WeeklyGrowthPlan(
            title = "Demo Week - Improve Sales And Content Rhythm",
            generatedDate = "2026-07-20",
            businessName = "Dapur Rasa Nusantara",
            businessCategoryId = "food_beverage",
            businessCategoryName = "Food & Beverage",
            focus = WeeklyPlanFocus(
                title = "Improve daily sales activity",
                category = InsightCategory.Sales,
                reason = "Demo profile has low sales, inconsistent content, and high expenses as selected challenges."
            ),
            target = "Track daily sales, promote bundle menu, and publish three simple content posts.",
            priorityReason = "A consistent sales and content rhythm may help the owner see which offer receives better response.",
            tasks = listOf(
                WeeklyTask("demo-task-1", "Record every sale for 7 days", "Use the finance form after each operating day.", InsightCategory.Finance, ActionEstimatedTime.UnderFifteenMinutes, ActionDifficulty.Easy, WeeklyTaskStatus.Completed, "Financial clarity improves dashboard accuracy.", "May help identify daily sales patterns."),
                WeeklyTask("demo-task-2", "Review top 3 menu items", "Compare best-selling menu with high-margin menu.", InsightCategory.Sales, ActionEstimatedTime.FifteenToThirtyMinutes, ActionDifficulty.Easy, WeeklyTaskStatus.Completed, "Best sellers should guide promotion focus.", "May help choose stronger offers."),
                WeeklyTask("demo-task-3", "Create bundle lunch offer", "Combine rice bowl and drink into one clear package.", InsightCategory.Marketing, ActionEstimatedTime.ThirtyToSixtyMinutes, ActionDifficulty.Medium, WeeklyTaskStatus.Completed, "Bundle offers can simplify purchase decisions.", "May help increase average order value."),
                WeeklyTask("demo-task-4", "Post behind-the-scenes prep", "Show clean preparation and packaging process.", InsightCategory.Content, ActionEstimatedTime.FifteenToThirtyMinutes, ActionDifficulty.Easy, WeeklyTaskStatus.Pending, "Trust content supports online ordering.", "May help customers feel more confident."),
                WeeklyTask("demo-task-5", "Check raw material spending", "Review whether the largest cost category can be controlled.", InsightCategory.Expense, ActionEstimatedTime.ThirtyToSixtyMinutes, ActionDifficulty.Medium, WeeklyTaskStatus.Pending, "Raw materials are the largest demo expense.", "May help protect estimated margin.")
            ),
            challenge = WeeklyChallenge(
                title = "Demo 7-Day Sales Tracking Challenge",
                description = "Record sales and expenses consistently for one full week.",
                checklistItems = listOf("Record daily income", "Record daily expenses", "Review largest expense", "Publish 3 content posts"),
                completionTarget = "Complete at least 5 daily records and 3 content actions.",
                motivationalCopy = "Small consistent records make weekly review easier."
            ),
            milestones = listOf(
                BusinessMilestone("demo-ms-1", "Complete 3 daily finance records", "Demo milestone for finance discipline.", MilestoneStatus.Completed, listOf("demo-task-1"), 100),
                BusinessMilestone("demo-ms-2", "Prepare one bundle campaign", "Demo milestone for sales action.", MilestoneStatus.InProgress, listOf("demo-task-2", "demo-task-3"), 60),
                BusinessMilestone("demo-ms-3", "Publish first trust-building content", "Demo milestone for content rhythm.", MilestoneStatus.NotStarted, listOf("demo-task-4"), 0)
            ),
            limitationsNote = "Demo plan is deterministic sample data for portfolio presentation. It does not guarantee sales or profit growth."
        )

        val demoContentIdeas = listOf(
            demoIdea("Demo - 3 alasan rice bowl cocok untuk makan siang", ContentIdeaType.Educational, ContentPlatform.InstagramReels, ContentGoal.ProductEducation, ContentIdeaStatus.Planned, true),
            demoIdea("Demo - Paket hemat makan siang minggu ini", ContentIdeaType.BundleCampaign, ContentPlatform.WhatsAppStory, ContentGoal.AnnounceBundleOffer, ContentIdeaStatus.Planned, false),
            demoIdea("Demo - Proses packing pesanan online", ContentIdeaType.BehindTheScenes, ContentPlatform.TikTok, ContentGoal.BuildTrust, ContentIdeaStatus.Done, false),
            demoIdea("Demo - Testimoni pelanggan repeat order", ContentIdeaType.Testimonial, ContentPlatform.InstagramFeed, ContentGoal.CustomerTestimonial, ContentIdeaStatus.Draft, true)
        )

        val demoProgressSnapshot = WeeklyProgressSnapshot(
            weekLabel = "2026-W30 Demo",
            weekStartDate = "2026-07-20",
            totalTasks = 5,
            completedTasks = 3,
            taskCompletionRate = 0.6f,
            milestoneProgress = 0.53f,
            weeklyIncome = 11_650_000,
            weeklyExpenses = 5_700_000,
            weeklyEstimatedProfit = 5_950_000,
            profitMarginPercent = 51,
            savedIdeasCount = 4,
            plannedContentCount = 2,
            postedOrDoneContentCount = 1,
            skippedContentCount = 1,
            businessHealthScore = 74,
            warningInsightCount = 2,
            criticalInsightCount = 0
        )

        val demoRetrospective = WeeklyRetrospective(
            weekLabel = "2026-W30 Demo",
            generatedDate = "2026-07-20",
            summaryTitle = "Demo weekly review for Dapur Rasa Nusantara",
            sections = listOf(
                RetrospectiveSection("What improved this week", listOf(RetrospectiveInsight("You completed 3 of 5 weekly tasks in the demo plan.", InsightSeverity.Positive))),
                RetrospectiveSection("What still needs attention", listOf(RetrospectiveInsight("Content consistency still needs follow-through because one scheduled post was skipped.", InsightSeverity.Warning))),
                RetrospectiveSection("Financial summary", listOf(RetrospectiveInsight("Recorded demo income is higher than expenses, but raw material cost should still be reviewed.", InsightSeverity.Info)))
            ),
            nextWeekSuggestion = NextWeekSuggestion(
                focus = "Keep sales tracking and publish trust-building content",
                reason = "The demo data suggests sales activity and content rhythm still need attention.",
                recommendedAction = "Review the bundle offer result and schedule two more posts before regenerating next week's plan."
            )
        )

        val demoReportSnapshot = BusinessReportSnapshot(
            period = BusinessReportPeriod.ThisMonth,
            businessName = "Dapur Rasa Nusantara",
            generatedAt = "2026-07-20",
            headlineSummary = "Demo report from local sample profile, finance, weekly plan, content, and retrospective data.",
            exportReadyText = "UsahaNaik Business Report\nBusiness: Dapur Rasa Nusantara\nNote: Demo data for portfolio presentation. This is not professional financial advice or an official accounting/tax document.",
            healthScore = 74,
            totalRevenue = 11_650_000,
            totalExpenses = 5_700_000,
            estimatedProfit = 5_950_000,
            taskCompletionRate = 0.6f,
            contentExecutionRate = 0.25f
        )

        val demoReminders = listOf(
            BusinessReminder(
                title = "Demo - record today\u2019s sales",
                description = "Catat pemasukan dan pengeluaran Dapur Rasa Nusantara setelah tutup toko.",
                type = ReminderType.DailyFinancialTracking,
                frequency = ReminderFrequency.Daily,
                timeLabel = "20:00",
                status = ReminderStatus.Active
            ),
            BusinessReminder(
                title = "Demo - review weekly plan",
                description = "Buka weekly plan dan pilih satu task prioritas untuk minggu ini.",
                type = ReminderType.WeeklyPlanTask,
                frequency = ReminderFrequency.Weekly,
                scheduledDay = "Monday",
                timeLabel = "09:00",
                status = ReminderStatus.Active
            ),
            BusinessReminder(
                title = "Demo - prepare scheduled content",
                description = "Review caption dan visual konten sebelum posting.",
                type = ReminderType.ContentSchedule,
                frequency = ReminderFrequency.Once,
                scheduledDate = "2026-07-21",
                timeLabel = "12:00",
                relatedEntityId = 1L,
                status = ReminderStatus.Active
            ),
            BusinessReminder(
                title = "Demo - weekly retrospective",
                description = "Evaluasi progress, konten, dan ringkasan keuangan lokal minggu ini.",
                type = ReminderType.WeeklyRetrospective,
                frequency = ReminderFrequency.Weekly,
                scheduledDay = "Sunday",
                timeLabel = "18:00",
                status = ReminderStatus.Active
            )
        )

        private fun demoIdea(
            title: String,
            type: ContentIdeaType,
            platform: ContentPlatform,
            goal: ContentGoal,
            status: ContentIdeaStatus,
            favorite: Boolean
        ): ContentIdea = ContentIdea(
            title = title,
            category = type.label,
            platformSuggestion = platform.label,
            angle = "Demo angle for Food & Beverage content planning.",
            cta = "Pesan menu hari ini lewat WhatsApp.",
            type = type,
            platform = platform,
            goal = goal,
            captionDraft = "Demo caption: kenalkan menu favorit Dapur Rasa Nusantara dengan wording yang jujur dan mudah dipahami.",
            hook = title.removePrefix("Demo - "),
            visualSuggestion = "Show the real menu, packaging, or preparation process clearly.",
            recommendedPostingNote = "Review details before posting. Demo data is sample content only.",
            relatedBusinessChallenge = BusinessChallenge.InconsistentContent,
            source = ContentGenerationSource.Local,
            safetyNote = "Demo content ideas are suggestions and should be reviewed before posting.",
            status = status,
            isFavorite = favorite
        )

        private fun demoCalendarItems(ideas: List<ContentIdea>): List<ContentCalendarSchedule> = ideas.take(4).mapIndexed { index, idea ->
            ContentCalendarSchedule(
                contentIdeaId = idea.id,
                title = idea.title,
                platform = idea.platform,
                scheduledDate = listOf("2026-07-20", "2026-07-21", "2026-07-22", "2026-07-23")[index],
                timeLabel = listOf("09:00", "12:00", "16:00", "19:00")[index],
                postingNote = "Demo schedule item. Review before posting.",
                status = listOf(ContentCalendarStatus.Posted, ContentCalendarStatus.Planned, ContentCalendarStatus.Skipped, ContentCalendarStatus.Done)[index]
            )
        }
    }
}
