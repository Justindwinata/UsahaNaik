package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.BusinessCategory

interface BusinessCategoryRepository {
    fun getCategories(): List<BusinessCategory>
}
