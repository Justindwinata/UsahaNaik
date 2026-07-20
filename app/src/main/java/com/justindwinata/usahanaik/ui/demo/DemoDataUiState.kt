package com.justindwinata.usahanaik.ui.demo

import com.justindwinata.usahanaik.data.demo.DemoDataResult

data class DemoDataUiState(
    val isLoadingDemoData: Boolean = false,
    val isClearingDemoData: Boolean = false,
    val lastResult: DemoDataResult? = null,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
