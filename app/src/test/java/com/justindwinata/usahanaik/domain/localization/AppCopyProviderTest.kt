package com.justindwinata.usahanaik.domain.localization

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppCopyProviderTest {
    @Test
    fun defaultLanguageIsIndonesian() {
        assertEquals(AppLanguage.Indonesian, AppLanguage.Default)
        assertEquals(AppLanguage.Indonesian, AppLanguage.fromCode(null))
    }

    @Test
    fun returnsIndonesianDashboardCopy() {
        val strings = AppCopyProvider.strings(AppLanguage.Indonesian)

        assertEquals("Dashboard", strings.dashboard)
        assertEquals("Rencana", strings.plan)
        assertTrue(strings.noGuaranteedProfit.contains("tidak menjamin"))
    }

    @Test
    fun returnsEnglishDashboardCopy() {
        val strings = AppCopyProvider.strings(AppLanguage.English)

        assertEquals("Dashboard", strings.dashboard)
        assertEquals("Plan", strings.plan)
        assertTrue(strings.noGuaranteedProfit.contains("does not guarantee"))
    }
}
