package com.justindwinata.usahanaik.ui.report

import com.justindwinata.usahanaik.domain.model.BusinessReport
import com.justindwinata.usahanaik.domain.model.BusinessReportPeriod
import com.justindwinata.usahanaik.domain.model.BusinessReportSnapshot
import com.justindwinata.usahanaik.domain.model.ExportReadyReport

data class BusinessReportUiState(
    val isLoading: Boolean = false,
    val isSavingSnapshot: Boolean = false,
    val selectedPeriod: BusinessReportPeriod = BusinessReportPeriod.ThisMonth,
    val report: BusinessReport? = null,
    val exportReadyReport: ExportReadyReport? = null,
    val snapshots: List<BusinessReportSnapshot> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null
) {
    val emptyStateMessage: String?
        get() = when {
            isLoading -> null
            report == null -> "Complete business setup first."
            report.businessName == "Complete business setup first" -> "Complete business setup first."
            report.isLimitedData -> "Record income, expenses, tasks, and content activity to make this report more complete."
            else -> null
        }
}
