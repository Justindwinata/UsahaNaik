package com.justindwinata.usahanaik.domain.report

import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessReportPeriod
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ExportReadyReportRendererTest {
    private val generator = BusinessReportGenerator(currentDateProvider = { LocalDate.parse("2026-07-20") })
    private val renderer = ExportReadyReportRenderer()

    @Test
    fun renderedReportIncludesCoreSections() {
        val report = renderer.render(sampleReport())

        assertTrue(report.body.contains("UsahaNaik Business Report"))
        assertTrue(report.body.contains("Business: Toko Maju"))
        assertTrue(report.body.contains("Period: This month"))
        assertTrue(report.body.contains("1. Financial Summary"))
        assertTrue(report.body.contains("2. Business Health"))
        assertTrue(report.body.contains("3. Weekly Execution"))
        assertTrue(report.body.contains("4. Content Activity"))
        assertTrue(report.body.contains("5. Retrospective"))
    }

    @Test
    fun renderedReportIncludesSafeDisclaimer() {
        val report = renderer.render(sampleReport())

        assertTrue(report.body.contains("not professional financial advice"))
        assertTrue(report.body.contains("official accounting/tax document"))
    }

    @Test
    fun renderedReportAvoidsGuaranteedProfitClaims() {
        val report = renderer.render(sampleReport())

        assertFalse(report.body.lowercase().contains("guaranteed profit"))
        assertFalse(report.body.lowercase().contains("guaranteed sales"))
    }

    private fun sampleReport() = generator.generate(
        period = BusinessReportPeriod.ThisMonth,
        input = BusinessReportInput(
            profile = BusinessProfile(
                draft = BusinessSetupDraft(
                    businessName = "Toko Maju",
                    categoryId = "food_beverage"
                ),
                createdAt = 1L,
                updatedAt = 1L
            ),
            financialEntries = listOf(
                FinancialEntry(
                    type = FinancialEntryType.Income,
                    title = "Sales",
                    amount = 2_000_000L,
                    category = "Product sales",
                    date = "2026-07-20"
                )
            ),
            contentCalendarItems = listOf(
                ContentCalendarSchedule(
                    contentIdeaId = 1L,
                    title = "Promo post",
                    platform = ContentPlatform.InstagramFeed,
                    scheduledDate = "2026-07-20",
                    status = ContentCalendarStatus.Posted
                )
            )
        )
    )
}
