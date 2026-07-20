package com.justindwinata.usahanaik.domain.report

import com.justindwinata.usahanaik.domain.model.ActionDifficulty
import com.justindwinata.usahanaik.domain.model.ActionEstimatedTime
import com.justindwinata.usahanaik.domain.model.BusinessDiagnosis
import com.justindwinata.usahanaik.domain.model.BusinessInsight
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessReportPeriod
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import com.justindwinata.usahanaik.domain.model.DashboardInsightSummary
import com.justindwinata.usahanaik.domain.model.DiagnosisHealthScore
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.NextWeekSuggestion
import com.justindwinata.usahanaik.domain.model.PriorityAction
import com.justindwinata.usahanaik.domain.model.RetrospectiveInsight
import com.justindwinata.usahanaik.domain.model.RetrospectiveSection
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class BusinessReportGeneratorTest {
    private val generator = BusinessReportGenerator(currentDateProvider = { LocalDate.parse("2026-07-20") })

    @Test
    fun reportWithNoProfileShowsSetupWarning() {
        val report = generator.generate(BusinessReportPeriod.ThisMonth, BusinessReportInput(profile = null))

        assertEquals("Complete business setup first", report.businessName)
        assertTrue(report.isLimitedData)
        assertTrue(report.insights.any { it.title == "Complete business setup" })
    }

    @Test
    fun filtersWeeklyPeriodFromMondayToSunday() {
        val report = generator.generate(
            period = BusinessReportPeriod.ThisWeek,
            input = BusinessReportInput(
                profile = sampleProfile(),
                financialEntries = listOf(
                    income("2026-07-19", 9_000_000L),
                    income("2026-07-20", 1_000_000L),
                    expense("2026-07-21", 300_000L)
                )
            )
        )

        assertEquals(1_000_000L, report.financialSummary.totalRevenue)
        assertEquals(300_000L, report.financialSummary.totalExpenses)
        assertEquals(700_000L, report.financialSummary.estimatedProfit)
    }

    @Test
    fun monthlyReportCalculatesRevenueExpenseProfitAndMargin() {
        val report = generator.generate(
            period = BusinessReportPeriod.ThisMonth,
            input = BusinessReportInput(
                profile = sampleProfile(),
                financialEntries = listOf(
                    income("2026-07-01", 2_000_000L),
                    expense("2026-07-02", 500_000L, "Raw materials"),
                    expense("2026-07-03", 500_000L, "Ads / promotion")
                )
            )
        )

        assertEquals(2_000_000L, report.financialSummary.totalRevenue)
        assertEquals(1_000_000L, report.financialSummary.totalExpenses)
        assertEquals(1_000_000L, report.financialSummary.estimatedProfit)
        assertEquals(50, report.financialSummary.profitMarginPercent)
        assertEquals(2, report.expenseBreakdown.size)
    }

    @Test
    fun mapsContentDiagnosisAndRetrospectiveSummaries() {
        val report = generator.generate(
            period = BusinessReportPeriod.AllTime,
            input = BusinessReportInput(
                profile = sampleProfile(),
                diagnosis = sampleDiagnosis(),
                contentIdeas = listOf(sampleIdea(ContentIdeaStatus.Planned), sampleIdea(ContentIdeaStatus.Done)),
                contentCalendarItems = listOf(
                    sampleCalendar(ContentCalendarStatus.Posted),
                    sampleCalendar(ContentCalendarStatus.Skipped)
                ),
                latestRetrospective = sampleRetrospective()
            )
        )

        assertEquals(2, report.contentPerformance.savedIdeasCount)
        assertEquals(1, report.contentPerformance.postedOrDoneContentCount)
        assertEquals(72, report.diagnosisSummary.healthScore)
        assertEquals("Keep tracking", report.retrospectiveSummary.keyTakeaway)
    }

    @Test
    fun generatesLimitedDataWarningForProfileWithoutActivity() {
        val report = generator.generate(
            period = BusinessReportPeriod.ThisMonth,
            input = BusinessReportInput(profile = sampleProfile())
        )

        assertTrue(report.isLimitedData)
        assertTrue(report.financialSummary.cautionMessage.contains("Record income and expenses"))
    }

    private fun sampleProfile(): BusinessProfile {
        return BusinessProfile(
            draft = BusinessSetupDraft(
                businessName = "Toko Maju",
                categoryId = "food_beverage",
                targetMonthlyRevenue = "Rp 10.000.000",
                targetMonthlyProfit = "Rp 3.000.000"
            ),
            createdAt = 1L,
            updatedAt = 1L
        )
    }

    private fun income(date: String, amount: Long) = FinancialEntry(
        type = FinancialEntryType.Income,
        title = "Sales",
        amount = amount,
        category = "Product sales",
        date = date
    )

    private fun expense(date: String, amount: Long, category: String = "Raw materials") = FinancialEntry(
        type = FinancialEntryType.Expense,
        title = "Expense",
        amount = amount,
        category = category,
        date = date
    )

    private fun sampleIdea(status: ContentIdeaStatus) = ContentIdea(
        title = "Promo idea",
        category = "Food & Beverage",
        platformSuggestion = "Instagram Feed",
        angle = "Menu promo",
        cta = "Order today",
        platform = ContentPlatform.InstagramFeed,
        goal = ContentGoal.PromotionCampaign,
        status = status
    )

    private fun sampleCalendar(status: ContentCalendarStatus) = ContentCalendarSchedule(
        contentIdeaId = 1L,
        title = "Promo idea",
        platform = ContentPlatform.InstagramFeed,
        scheduledDate = "2026-07-20",
        status = status
    )

    private fun sampleDiagnosis() = BusinessDiagnosis(
        healthScore = DiagnosisHealthScore(72, "On track", "Rule-based score."),
        breakdown = emptyList(),
        insights = listOf(BusinessInsight("Profit is positive", "Keep tracking.", InsightCategory.Finance, InsightSeverity.Positive)),
        riskSignals = emptyList(),
        priorityActions = listOf(
            PriorityAction(
                title = "Review cost",
                description = "Check costs.",
                category = InsightCategory.Expense,
                difficulty = ActionDifficulty.Easy,
                estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                reason = "Expense visibility.",
                expectedOutcome = "May help margin visibility."
            )
        ),
        summary = DashboardInsightSummary(
            financeInsightCount = 1,
            warningCount = 0,
            priorityActionCount = 1,
            goalProgressStatus = "On track"
        )
    )

    private fun sampleRetrospective() = WeeklyRetrospective(
        weekLabel = "Week 30",
        generatedDate = "2026-07-20",
        summaryTitle = "Review",
        sections = listOf(
            RetrospectiveSection("What improved this week", listOf(RetrospectiveInsight("You completed tasks.", InsightSeverity.Positive))),
            RetrospectiveSection("What still needs attention", listOf(RetrospectiveInsight("Review costs.", InsightSeverity.Warning)))
        ),
        nextWeekSuggestion = NextWeekSuggestion(
            focus = "Keep tracking",
            reason = "Progress exists.",
            recommendedAction = "Save another snapshot."
        )
    )
}
