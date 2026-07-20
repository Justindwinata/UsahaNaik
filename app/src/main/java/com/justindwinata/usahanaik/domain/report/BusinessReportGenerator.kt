package com.justindwinata.usahanaik.domain.report

import com.justindwinata.usahanaik.domain.finance.FinancialCalculator
import com.justindwinata.usahanaik.domain.model.BusinessDiagnosis
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessReport
import com.justindwinata.usahanaik.domain.model.BusinessReportKpi
import com.justindwinata.usahanaik.domain.model.BusinessReportPeriod
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.ContentPerformanceSummary
import com.justindwinata.usahanaik.domain.model.ExportReadyReport
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.FinancialReportSummary
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.ReportChartData
import com.justindwinata.usahanaik.domain.model.ReportChartPoint
import com.justindwinata.usahanaik.domain.model.ReportDiagnosisSummary
import com.justindwinata.usahanaik.domain.model.ReportExpenseBreakdownItem
import com.justindwinata.usahanaik.domain.model.ReportInsight
import com.justindwinata.usahanaik.domain.model.ReportKpiStatus
import com.justindwinata.usahanaik.domain.model.ReportRetrospectiveSummary
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
import com.justindwinata.usahanaik.domain.model.WeeklyExecutionSummary
import com.justindwinata.usahanaik.domain.setup.BusinessSetupCalculator
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeParseException
import kotlin.math.roundToInt

data class BusinessReportInput(
    val profile: BusinessProfile?,
    val financialEntries: List<FinancialEntry> = emptyList(),
    val diagnosis: BusinessDiagnosis? = null,
    val activeWeeklyPlan: WeeklyGrowthPlan? = null,
    val contentIdeas: List<ContentIdea> = emptyList(),
    val contentCalendarItems: List<ContentCalendarSchedule> = emptyList(),
    val progressSnapshots: List<WeeklyProgressSnapshot> = emptyList(),
    val latestRetrospective: WeeklyRetrospective? = null
)

class BusinessReportGenerator(
    private val currentDateProvider: () -> LocalDate = { LocalDate.now() }
) {
    fun generate(
        period: BusinessReportPeriod,
        input: BusinessReportInput
    ): BusinessReport {
        val currentDate = currentDateProvider()
        val filteredEntries = filterByPeriod(input.financialEntries, period, currentDate) { it.date }
        val filteredCalendar = filterByPeriod(input.contentCalendarItems, period, currentDate) { it.scheduledDate }
        val financialSummary = FinancialCalculator.buildSummary(
            entries = filteredEntries,
            targetMonthlyRevenue = input.profile?.draft?.targetMonthlyRevenue,
            targetMonthlyProfit = input.profile?.draft?.targetMonthlyProfit
        )
        val reportFinancialSummary = financialSummary.toReportSummary(filteredEntries)
        val weeklyExecution = input.activeWeeklyPlan.toWeeklyExecutionSummary()
        val contentPerformance = buildContentPerformance(input.contentIdeas, filteredCalendar)
        val diagnosisSummary = buildDiagnosisSummary(input.diagnosis)
        val retrospectiveSummary = buildRetrospectiveSummary(input.latestRetrospective)
        val insights = buildInsights(
            profile = input.profile,
            financialSummary = reportFinancialSummary,
            weeklyExecution = weeklyExecution,
            contentPerformance = contentPerformance,
            diagnosisSummary = diagnosisSummary
        )
        val businessName = input.profile?.draft?.businessName?.takeIf { it.isNotBlank() } ?: "Complete business setup first"
        val categoryName = input.profile?.draft?.categoryId ?: "-"
        val kpis = buildKpis(reportFinancialSummary, diagnosisSummary, weeklyExecution, contentPerformance)

        return BusinessReport(
            period = period,
            businessName = businessName,
            categoryName = categoryName,
            generatedAt = currentDate.toString(),
            kpis = kpis,
            financialSummary = reportFinancialSummary,
            revenueExpenseChart = buildRevenueExpenseChart(filteredEntries),
            expenseBreakdown = buildExpenseBreakdown(filteredEntries),
            weeklyExecution = weeklyExecution,
            contentPerformance = contentPerformance,
            diagnosisSummary = diagnosisSummary,
            retrospectiveSummary = retrospectiveSummary,
            insights = insights,
            exportReadyReport = ExportReadyReport(
                title = "UsahaNaik Business Report",
                body = ""
            ),
            isLimitedData = input.profile == null || filteredEntries.isEmpty() || input.activeWeeklyPlan == null
        )
    }

    fun <T> filterByPeriod(
        items: List<T>,
        period: BusinessReportPeriod,
        currentDate: LocalDate,
        dateSelector: (T) -> String
    ): List<T> {
        if (period == BusinessReportPeriod.AllTime) return items
        val range = rangeFor(period, currentDate)
        return items.filter { item ->
            val date = parseDate(dateSelector(item)) ?: return@filter false
            !date.isBefore(range.first) && !date.isAfter(range.second)
        }
    }

    private fun rangeFor(period: BusinessReportPeriod, currentDate: LocalDate): Pair<LocalDate, LocalDate> {
        return when (period) {
            BusinessReportPeriod.ThisWeek -> {
                val start = currentDate.minusDays((currentDate.dayOfWeek.value - DayOfWeek.MONDAY.value).toLong())
                start to start.plusDays(6)
            }
            BusinessReportPeriod.ThisMonth -> currentDate.withDayOfMonth(1) to currentDate
            BusinessReportPeriod.LastThirtyDays -> currentDate.minusDays(29) to currentDate
            BusinessReportPeriod.AllTime -> LocalDate.MIN to currentDate
        }
    }

    private fun parseDate(value: String): LocalDate? {
        return try {
            LocalDate.parse(value)
        } catch (_: DateTimeParseException) {
            null
        }
    }

    private fun com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary.toReportSummary(
        entries: List<FinancialEntry>
    ): FinancialReportSummary {
        return FinancialReportSummary(
            totalRevenue = totalIncome,
            totalExpenses = totalExpenses,
            estimatedProfit = estimatedProfit,
            profitMarginPercent = profitMarginPercent,
            largestExpenseCategory = largestExpenseCategory,
            recentEntryCount = recentEntries.size,
            cautionMessage = if (entries.isEmpty()) {
                "Record income and expenses to make this report more complete."
            } else {
                "Estimated profit is calculated from local income minus expenses."
            }
        )
    }

    private fun WeeklyGrowthPlan?.toWeeklyExecutionSummary(): WeeklyExecutionSummary {
        val progress = this?.progressSummary
        return WeeklyExecutionSummary(
            focusTitle = this?.focus?.title ?: "No active weekly plan",
            completedTasks = progress?.completedTasks ?: 0,
            totalTasks = progress?.totalTasks ?: 0,
            taskCompletionRate = progress?.taskProgress ?: 0f,
            milestoneProgress = progress?.milestoneProgress ?: 0f,
            nextTaskTitle = progress?.nextTask?.title ?: "Generate or complete a weekly plan."
        )
    }

    private fun buildContentPerformance(
        ideas: List<ContentIdea>,
        calendarItems: List<ContentCalendarSchedule>
    ): ContentPerformanceSummary {
        val postedOrDone = calendarItems.count {
            it.status == ContentCalendarStatus.Posted || it.status == ContentCalendarStatus.Done
        }
        val scheduled = calendarItems.size
        return ContentPerformanceSummary(
            savedIdeasCount = ideas.size,
            plannedIdeasCount = ideas.count { it.status == ContentIdeaStatus.Planned },
            favoriteIdeasCount = ideas.count { it.isFavorite },
            doneIdeasCount = ideas.count { it.status == ContentIdeaStatus.Done },
            scheduledContentCount = scheduled,
            postedOrDoneContentCount = postedOrDone,
            skippedContentCount = calendarItems.count { it.status == ContentCalendarStatus.Skipped },
            nextScheduledContent = calendarItems
                .sortedWith(compareBy<ContentCalendarSchedule> { it.scheduledDate }.thenBy { it.timeLabel })
                .firstOrNull { it.status == ContentCalendarStatus.Planned }
                ?.title ?: "No scheduled content yet",
            executionRate = if (scheduled == 0) 0f else (postedOrDone.toFloat() / scheduled).coerceIn(0f, 1f)
        )
    }

    private fun buildDiagnosisSummary(diagnosis: BusinessDiagnosis?): ReportDiagnosisSummary {
        return ReportDiagnosisSummary(
            healthScore = diagnosis?.healthScore?.score ?: 0,
            statusLabel = diagnosis?.healthScore?.statusLabel ?: "Setup needed",
            warningCount = diagnosis?.summary?.warningCount ?: 0,
            criticalCount = diagnosis?.riskSignals?.count { it.severity == InsightSeverity.Critical }.orZero(),
            topInsights = diagnosis?.insights.orEmpty().take(3).map { it.title },
            priorityActions = diagnosis?.priorityActions.orEmpty().take(3).map { it.title }
        )
    }

    private fun buildRetrospectiveSummary(retrospective: WeeklyRetrospective?): ReportRetrospectiveSummary {
        return ReportRetrospectiveSummary(
            weekLabel = retrospective?.weekLabel ?: "No retrospective yet",
            keyTakeaway = retrospective?.nextWeekSuggestion?.focus ?: "Generate a weekly retrospective to add report highlights.",
            whatImproved = retrospective?.sections?.firstOrNull { it.title == "What improved this week" }
                ?.insights?.firstOrNull()?.message ?: "-",
            needsAttention = retrospective?.sections?.firstOrNull { it.title == "What still needs attention" }
                ?.insights?.firstOrNull()?.message ?: "-",
            nextWeekSuggestion = retrospective?.nextWeekSuggestion?.recommendedAction ?: "-"
        )
    }

    private fun buildRevenueExpenseChart(entries: List<FinancialEntry>): ReportChartData {
        val points = entries
            .groupBy { it.date }
            .toSortedMap()
            .map { (date, dayEntries) ->
                ReportChartPoint(
                    label = date.takeLast(5),
                    primaryValue = dayEntries.filter { it.type == FinancialEntryType.Income }.sumOf { it.amount },
                    secondaryValue = dayEntries.filter { it.type == FinancialEntryType.Expense }.sumOf { it.amount }
                )
            }
            .takeLast(7)
        return ReportChartData("Revenue vs expenses", points)
    }

    private fun buildExpenseBreakdown(entries: List<FinancialEntry>): List<ReportExpenseBreakdownItem> {
        val expenseEntries = entries.filter { it.type == FinancialEntryType.Expense }
        val total = expenseEntries.sumOf { it.amount }.takeIf { it > 0L } ?: return emptyList()
        return expenseEntries
            .groupBy { it.category }
            .map { (category, values) ->
                val amount = values.sumOf { it.amount }
                ReportExpenseBreakdownItem(
                    category = category,
                    amount = amount,
                    percentage = ((amount.toDouble() / total.toDouble()) * 100).roundToInt()
                )
            }
            .sortedByDescending { it.amount }
    }

    private fun buildKpis(
        financial: FinancialReportSummary,
        diagnosis: ReportDiagnosisSummary,
        weekly: WeeklyExecutionSummary,
        content: ContentPerformanceSummary
    ): List<BusinessReportKpi> {
        return listOf(
            BusinessReportKpi("Revenue", BusinessSetupCalculator.formatRupiah(financial.totalRevenue), "Local entries", ReportKpiStatus.Neutral),
            BusinessReportKpi("Expenses", BusinessSetupCalculator.formatRupiah(financial.totalExpenses), financial.largestExpenseCategory, ReportKpiStatus.Warning),
            BusinessReportKpi("Profit", BusinessSetupCalculator.formatRupiah(financial.estimatedProfit), "Estimated", if (financial.estimatedProfit >= 0) ReportKpiStatus.Positive else ReportKpiStatus.Warning),
            BusinessReportKpi("Margin", "${financial.profitMarginPercent}%", "Estimated", ReportKpiStatus.Neutral),
            BusinessReportKpi("Health", "${diagnosis.healthScore}/100", diagnosis.statusLabel, ReportKpiStatus.Neutral),
            BusinessReportKpi("Tasks", "${(weekly.taskCompletionRate * 100).toInt()}%", "${weekly.completedTasks}/${weekly.totalTasks}", ReportKpiStatus.Neutral),
            BusinessReportKpi("Content", "${(content.executionRate * 100).toInt()}%", "${content.postedOrDoneContentCount}/${content.scheduledContentCount}", ReportKpiStatus.Neutral)
        )
    }

    private fun buildInsights(
        profile: BusinessProfile?,
        financialSummary: FinancialReportSummary,
        weeklyExecution: WeeklyExecutionSummary,
        contentPerformance: ContentPerformanceSummary,
        diagnosisSummary: ReportDiagnosisSummary
    ): List<ReportInsight> {
        return buildList {
            if (profile == null) {
                add(ReportInsight("Complete business setup", "Complete business setup first to generate a richer report.", InsightSeverity.Warning))
            }
            if (financialSummary.totalRevenue == 0L && financialSummary.totalExpenses == 0L) {
                add(ReportInsight("Financial data is limited", "Record income and expenses to improve report accuracy.", InsightSeverity.Warning))
            }
            if (financialSummary.estimatedProfit < 0L) {
                add(ReportInsight("Estimated profit is negative", "Expenses are higher than recorded income in this period.", InsightSeverity.Critical))
            }
            if (weeklyExecution.totalTasks == 0) {
                add(ReportInsight("No weekly plan progress", "Generate a weekly plan to include execution progress.", InsightSeverity.Info))
            }
            if (contentPerformance.scheduledContentCount == 0) {
                add(ReportInsight("No scheduled content", "Schedule saved content ideas to track content execution.", InsightSeverity.Info))
            }
            if (diagnosisSummary.warningCount > 0 || diagnosisSummary.criticalCount > 0) {
                add(ReportInsight("Diagnosis needs attention", "Review warning and risk signals before the next planning cycle.", InsightSeverity.Warning))
            }
            if (isEmpty()) {
                add(ReportInsight("Report is building momentum", "Local data shows enough activity for a basic monitoring report.", InsightSeverity.Positive))
            }
        }
    }

    private fun Int?.orZero(): Int = this ?: 0
}
