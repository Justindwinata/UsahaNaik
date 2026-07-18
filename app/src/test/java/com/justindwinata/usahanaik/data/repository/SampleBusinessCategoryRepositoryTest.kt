package com.justindwinata.usahanaik.data.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SampleBusinessCategoryRepositoryTest {
    private val repository = SampleBusinessCategoryRepository()

    @Test
    fun categoriesContainRequiredInitialSet() {
        val categories = repository.getCategories()

        assertEquals(10, categories.size)
        assertTrue(categories.any { it.displayName == "Skincare & Beauty" })
        assertTrue(categories.any { it.displayName == "Warung / Toko Kelontong" })
        assertTrue(categories.any { it.displayName == "Other Business" })
    }

    @Test
    fun categoryMetadataIsCompleteAndUnique() {
        val categories = repository.getCategories()

        assertEquals(categories.size, categories.map { it.id }.distinct().size)
        assertTrue(categories.all { it.displayName.isNotBlank() })
        assertTrue(categories.all { it.description.isNotBlank() })
        assertTrue(categories.all { it.focusArea.isNotBlank() })
        assertTrue(categories.all { it.sampleRecommendedGoal.isNotBlank() })
    }

    @Test
    fun skincareAndWarungUseExpectedFocusDirection() {
        val categories = repository.getCategories()
        val skincare = categories.first { it.id == "skincare_beauty" }
        val warung = categories.first { it.id == "warung_kelontong" }

        assertTrue(skincare.focusArea.contains("repeat order"))
        assertTrue(skincare.focusArea.contains("edukasi produk"))
        assertTrue(warung.focusArea.contains("stock turnover"))
        assertTrue(warung.sampleRecommendedGoal.contains("stok lambat"))
    }
}
