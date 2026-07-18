package com.justindwinata.usahanaik.domain.setup

import com.justindwinata.usahanaik.data.repository.SampleBusinessCategoryRepository
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessCategorySetupHintsTest {
    private val categories = SampleBusinessCategoryRepository().getCategories()

    @Test
    fun requiredCategoriesExposeSpecificSetupHints() {
        val food = BusinessCategorySetupHints.guidanceFor(categories.first { it.id == "food_beverage" })
        val warung = BusinessCategorySetupHints.guidanceFor(categories.first { it.id == "warung_kelontong" })
        val skincare = BusinessCategorySetupHints.guidanceFor(categories.first { it.id == "skincare_beauty" })
        val laundry = BusinessCategorySetupHints.guidanceFor(categories.first { it.id == "laundry" })
        val onlineShop = BusinessCategorySetupHints.guidanceFor(categories.first { it.id == "online_shop_reseller" })

        assertTrue(food.setupHints.any { it.contains("food cost", ignoreCase = true) })
        assertTrue(warung.setupHints.any { it.contains("stock turnover", ignoreCase = true) })
        assertTrue(skincare.setupHints.any { it.contains("education", ignoreCase = true) })
        assertTrue(laundry.setupHints.any { it.contains("repeat customers", ignoreCase = true) })
        assertTrue(onlineShop.setupHints.any { it.contains("marketplace fees", ignoreCase = true) })
    }

    @Test
    fun guidanceCarriesRecommendedMonthlyFocus() {
        val skincare = BusinessCategorySetupHints.guidanceFor(categories.first { it.id == "skincare_beauty" })

        assertEquals(MonthlyFocus.IncreaseRepeatOrders, skincare.recommendedMonthlyFocus)
        assertEquals("Skincare & Beauty", skincare.categoryName)
    }
}
