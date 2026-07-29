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
        assertEquals("Berbasis aturan", strings.ruleBased)
        assertEquals("Lokal saja", strings.localOnly)
        assertEquals("Pilih bahasa", strings.selectLanguagePrefix)
        assertTrue(strings.profileSubtitle.contains("Kelola bahasa"))
        assertTrue(strings.noGuaranteedProfit.contains("tidak menjamin"))
    }

    @Test
    fun returnsEnglishDashboardCopy() {
        val strings = AppCopyProvider.strings(AppLanguage.English)

        assertEquals("Dashboard", strings.dashboard)
        assertEquals("Plan", strings.plan)
        assertEquals("Rule-based", strings.ruleBased)
        assertEquals("Local only", strings.localOnly)
        assertEquals("Select language", strings.selectLanguagePrefix)
        assertTrue(strings.profileSubtitle.contains("Manage language"))
        assertTrue(strings.noGuaranteedProfit.contains("does not guarantee"))
    }
}
