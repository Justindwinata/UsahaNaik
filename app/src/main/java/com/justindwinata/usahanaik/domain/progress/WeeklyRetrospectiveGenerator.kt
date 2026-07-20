package com.justindwinata.usahanaik.domain.progress

import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.NextWeekSuggestion
import com.justindwinata.usahanaik.domain.model.RetrospectiveInsight
import com.justindwinata.usahanaik.domain.model.RetrospectiveSection
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus

class WeeklyRetrospectiveGenerator {
    fun generate(
        weekLabel: String,
        generatedDate: String,
        snapshot: WeeklyProgressSnapshot,
        activePlan: WeeklyGrowthPlan?
    ): WeeklyRetrospective {
        val completedTasks = activePlan?.tasks.orEmpty().filter { it.status == WeeklyTaskStatus.Completed }
        val missedTasks = activePlan?.tasks.orEmpty().filterNot { it.status == WeeklyTaskStatus.Completed }
        return WeeklyRetrospective(
            weekLabel = weekLabel,
            generatedDate = generatedDate,
            summaryTitle = "Weekly retrospective for $weekLabel",
            sections = listOf(
                improvedSection(snapshot),
                attentionSection(snapshot, missedTasks.map { it.title }),
                completedTaskSection(snapshot, completedTasks.map { it.title }),
                missedTaskSection(missedTasks.map { it.title }),
                contentSection(snapshot),
                financeSection(snapshot)
            ),
            nextWeekSuggestion = nextWeekSuggestion(snapshot, missedTasks.firstOrNull()?.title)
        )
    }

    private fun improvedSection(snapshot: WeeklyProgressSnapshot): RetrospectiveSection {
        val message = when {
            snapshot.completedTasks > 0 -> "You completed ${snapshot.completedTasks} of ${snapshot.totalTasks} weekly tasks."
            snapshot.postedOrDoneContentCount > 0 -> "Content execution improved because ${snapshot.postedOrDoneContentCount} scheduled items were marked posted or done."
            snapshot.weeklyIncome > 0 -> "Recorded income this week may indicate better financial visibility."
            else -> "No clear improvement signal was recorded yet. Start by saving one progress action this week."
        }
        return RetrospectiveSection(
            title = "What improved this week",
            insights = listOf(RetrospectiveInsight(message, InsightSeverity.Positive))
        )
    }

    private fun attentionSection(snapshot: WeeklyProgressSnapshot, missedTaskTitles: List<String>): RetrospectiveSection {
        val insights = buildList {
            if (missedTaskTitles.isNotEmpty()) {
                add(RetrospectiveInsight("Some planned tasks were not completed. Consider reducing scope or choosing the most important task first.", InsightSeverity.Warning))
            }
            if (snapshot.criticalInsightCount > 0) {
                add(RetrospectiveInsight("Critical diagnosis signals still need attention before next week planning.", InsightSeverity.Critical))
            }
            if (snapshot.weeklyEstimatedProfit < 0L) {
                add(RetrospectiveInsight("Expenses were higher than income this week. Review the largest cost area before creating the next plan.", InsightSeverity.Critical))
            }
            if (isEmpty()) {
                add(RetrospectiveInsight("No major attention signal was saved for this week.", InsightSeverity.Info))
            }
        }
        return RetrospectiveSection("What still needs attention", insights)
    }

    private fun completedTaskSection(snapshot: WeeklyProgressSnapshot, completedTaskTitles: List<String>): RetrospectiveSection {
        val insights = if (completedTaskTitles.isEmpty()) {
            listOf(RetrospectiveInsight("No weekly tasks were marked completed yet.", InsightSeverity.Warning))
        } else {
            completedTaskTitles.map { RetrospectiveInsight(it, InsightSeverity.Positive) } +
                RetrospectiveInsight("Task completion rate: ${(snapshot.taskCompletionRate * 100).toInt()}%.", InsightSeverity.Info)
        }
        return RetrospectiveSection("Completed tasks", insights)
    }

    private fun missedTaskSection(missedTaskTitles: List<String>): RetrospectiveSection {
        val insights = if (missedTaskTitles.isEmpty()) {
            listOf(RetrospectiveInsight("No missed tasks were recorded.", InsightSeverity.Positive))
        } else {
            missedTaskTitles.map { RetrospectiveInsight(it, InsightSeverity.Warning) }
        }
        return RetrospectiveSection("Missed tasks", insights)
    }

    private fun contentSection(snapshot: WeeklyProgressSnapshot): RetrospectiveSection {
        val message = when {
            snapshot.postedOrDoneContentCount > 0 -> "Content consistency may be improving because ${snapshot.postedOrDoneContentCount} scheduled content items were posted or completed."
            snapshot.plannedContentCount > 0 -> "${snapshot.plannedContentCount} content items are still planned. Consider posting or rescheduling them."
            snapshot.savedIdeasCount > 0 -> "You have saved content ideas, but no posted calendar item was recorded this week."
            else -> "No content planning activity was recorded yet."
        }
        return RetrospectiveSection("Content execution summary", listOf(RetrospectiveInsight(message)))
    }

    private fun financeSection(snapshot: WeeklyProgressSnapshot): RetrospectiveSection {
        val severity = if (snapshot.weeklyEstimatedProfit < 0L) InsightSeverity.Critical else InsightSeverity.Info
        val message = "Weekly income: ${snapshot.weeklyIncome}, expenses: ${snapshot.weeklyExpenses}, estimated profit: ${snapshot.weeklyEstimatedProfit}, margin: ${snapshot.profitMarginPercent}%."
        return RetrospectiveSection("Financial summary", listOf(RetrospectiveInsight(message, severity)))
    }

    private fun nextWeekSuggestion(snapshot: WeeklyProgressSnapshot, firstMissedTaskTitle: String?): NextWeekSuggestion {
        return when {
            snapshot.weeklyEstimatedProfit < 0L -> NextWeekSuggestion(
                focus = "Review expenses before scaling activity",
                reason = "This week's estimated profit was negative.",
                recommendedAction = "Check the largest expense category and choose one cost to reduce or monitor."
            )
            snapshot.taskCompletionRate < 0.5f -> NextWeekSuggestion(
                focus = "Improve weekly execution rhythm",
                reason = "Less than half of weekly tasks were completed.",
                recommendedAction = firstMissedTaskTitle?.let { "Start with: $it." } ?: "Choose three small tasks for next week."
            )
            snapshot.postedOrDoneContentCount == 0 && snapshot.savedIdeasCount > 0 -> NextWeekSuggestion(
                focus = "Turn saved ideas into posted content",
                reason = "Saved ideas exist but content execution was not recorded.",
                recommendedAction = "Schedule one saved idea and mark it posted after publishing."
            )
            else -> NextWeekSuggestion(
                focus = "Continue steady growth tracking",
                reason = "This week's saved progress suggests the current rhythm can continue.",
                recommendedAction = "Generate the next weekly plan and keep recording finances and content execution."
            )
        }
    }
}
