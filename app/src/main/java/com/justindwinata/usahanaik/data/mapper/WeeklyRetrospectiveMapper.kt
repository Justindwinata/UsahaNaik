package com.justindwinata.usahanaik.data.mapper

import com.justindwinata.usahanaik.data.local.WeeklyRetrospectiveEntity
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.NextWeekSuggestion
import com.justindwinata.usahanaik.domain.model.RetrospectiveInsight
import com.justindwinata.usahanaik.domain.model.RetrospectiveSection
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective

object WeeklyRetrospectiveMapper {
    private const val SECTION_SEPARATOR = "\u001E"
    private const val FIELD_SEPARATOR = "\u001F"
    private const val INSIGHT_SEPARATOR = "\u001D"

    fun WeeklyRetrospective.toEntity(now: Long): WeeklyRetrospectiveEntity {
        return WeeklyRetrospectiveEntity(
            id = id,
            weekLabel = weekLabel,
            generatedDate = generatedDate,
            summaryTitle = summaryTitle,
            sections = encodeSections(sections),
            nextWeekFocus = nextWeekSuggestion.focus,
            nextWeekReason = nextWeekSuggestion.reason,
            nextWeekRecommendedAction = nextWeekSuggestion.recommendedAction,
            createdAt = createdAt.takeIf { it > 0L } ?: now,
            updatedAt = now
        )
    }

    fun WeeklyRetrospectiveEntity.toDomain(): WeeklyRetrospective {
        return WeeklyRetrospective(
            id = id,
            weekLabel = weekLabel,
            generatedDate = generatedDate,
            summaryTitle = summaryTitle,
            sections = decodeSections(sections),
            nextWeekSuggestion = NextWeekSuggestion(
                focus = nextWeekFocus,
                reason = nextWeekReason,
                recommendedAction = nextWeekRecommendedAction
            ),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun encodeSections(sections: List<RetrospectiveSection>): String {
        return sections.joinToString(SECTION_SEPARATOR) { section ->
            val insights = section.insights.joinToString(INSIGHT_SEPARATOR) { insight ->
                listOf(escape(insight.message), insight.severity.name).joinToString(FIELD_SEPARATOR)
            }
            listOf(escape(section.title), insights).joinToString(FIELD_SEPARATOR)
        }
    }

    private fun decodeSections(value: String): List<RetrospectiveSection> {
        if (value.isBlank()) return emptyList()
        return value.split(SECTION_SEPARATOR).mapNotNull { sectionValue ->
            val parts = sectionValue.split(FIELD_SEPARATOR, limit = 2)
            val title = parts.getOrNull(0)?.let(::unescape) ?: return@mapNotNull null
            val insights = parts.getOrNull(1).orEmpty()
                .split(INSIGHT_SEPARATOR)
                .filter { it.isNotBlank() }
                .map { insightValue ->
                    val insightParts = insightValue.split(FIELD_SEPARATOR, limit = 2)
                    RetrospectiveInsight(
                        message = unescape(insightParts.getOrNull(0).orEmpty()),
                        severity = runCatching {
                            InsightSeverity.valueOf(insightParts.getOrNull(1).orEmpty())
                        }.getOrDefault(InsightSeverity.Info)
                    )
                }
            RetrospectiveSection(title = title, insights = insights)
        }
    }

    private fun escape(value: String): String {
        return value
            .replace("\\", "\\\\")
            .replace(SECTION_SEPARATOR, "")
            .replace(FIELD_SEPARATOR, "")
            .replace(INSIGHT_SEPARATOR, "")
    }

    private fun unescape(value: String): String = value.replace("\\\\", "\\")
}
