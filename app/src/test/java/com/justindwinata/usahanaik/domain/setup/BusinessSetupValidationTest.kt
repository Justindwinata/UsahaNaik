package com.justindwinata.usahanaik.domain.setup

import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessSetupValidationTest {
    @Test
    fun defaultDraftIsEmptyAndInvalid() {
        val draft = BusinessSetupDraft()
        val result = BusinessSetupValidator.validate(draft)

        assertTrue(draft.isEmpty)
        assertFalse(result.isValid)
        assertEquals("Business name is required.", result.errorFor(BusinessSetupField.BusinessName))
    }

    @Test
    fun parserAcceptsIndonesianStyleCurrencyInput() {
        assertEquals(18_000_000L, BusinessSetupCalculator.parseIndonesianNumber("Rp 18.000.000"))
        assertEquals(18_500_000L, BusinessSetupCalculator.parseIndonesianNumber("18,5 juta"))
        assertEquals(250_000L, BusinessSetupCalculator.parseIndonesianNumber("250 rb"))
        assertNull(BusinessSetupCalculator.parseIndonesianNumber("-1"))
    }

    @Test
    fun requiredValidationShowsFriendlyErrors() {
        val result = BusinessSetupValidator.validate(validDraft().copy(businessName = "", challenges = emptySet()))

        assertFalse(result.isValid)
        assertEquals("Business name is required.", result.errorFor(BusinessSetupField.BusinessName))
        assertEquals(
            "Select at least one business challenge.",
            result.errorFor(BusinessSetupField.Challenges)
        )
    }

    @Test
    fun numericValidationRejectsNegativeAndInvalidValues() {
        val result = BusinessSetupValidator.validate(
            validDraft().copy(monthlyRevenue = "-1000", targetMonthlyProfit = "abc")
        )

        assertEquals(
            "Monthly revenue must be zero or higher.",
            result.errorFor(BusinessSetupField.MonthlyRevenue)
        )
        assertEquals(
            "Target monthly profit must be zero or higher.",
            result.errorFor(BusinessSetupField.TargetMonthlyProfit)
        )
    }

    @Test
    fun validDraftPassesValidation() {
        val result = BusinessSetupValidator.validate(validDraft())

        assertTrue(result.isValid)
    }

    @Test
    fun derivedCalculationsReturnMarginAndTargetGaps() {
        val draft = validDraft()

        assertEquals(25, BusinessSetupCalculator.profitMarginPercent(draft))
        assertEquals(2_000_000L, BusinessSetupCalculator.revenueTargetGap(draft))
        assertEquals(1_500_000L, BusinessSetupCalculator.profitTargetGap(draft))
        assertEquals("Rp2.000.000", BusinessSetupCalculator.formatRupiah(2_000_000L))
    }

    private fun validDraft(): BusinessSetupDraft = BusinessSetupDraft(
        businessName = "Warung Naik",
        categoryId = "warung_kelontong",
        monthlyRevenue = "Rp 10.000.000",
        monthlyExpenses = "Rp 7.500.000",
        estimatedMonthlyProfit = "Rp 2.500.000",
        productCount = "25",
        challenges = setOf(BusinessChallenge.PoorFinancialRecords),
        targetMonthlyRevenue = "Rp 12.000.000",
        targetMonthlyProfit = "Rp 4.000.000",
        mainFocus = MonthlyFocus.OrganizeFinance,
        availableTime = AvailableTime.ThreeToFiveHours
    )
}
