package com.justindwinata.usahanaik.domain.model

data class FinancialEntry(
    val id: Long = 0L,
    val type: FinancialEntryType,
    val title: String,
    val amount: Long,
    val category: String,
    val date: String,
    val note: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

enum class FinancialEntryType(val label: String) {
    Income("Income"),
    Expense("Expense")
}

enum class IncomeCategory(val label: String) {
    ProductSales("Product sales"),
    ServiceSales("Service sales"),
    OnlineSales("Online sales"),
    OfflineSales("Offline sales"),
    RepeatOrder("Repeat order"),
    OtherIncome("Other income")
}

enum class ExpenseCategory(val label: String) {
    RawMaterials("Raw materials"),
    StockPurchase("Stock purchase"),
    Rent("Rent"),
    Utilities("Utilities"),
    Payroll("Payroll"),
    Packaging("Packaging"),
    Shipping("Shipping"),
    AdsPromotion("Ads / promotion"),
    MarketplaceFee("Marketplace fee"),
    OperationalCost("Operational cost"),
    OtherExpense("Other expense")
}

data class FinancialTrackingSummary(
    val totalIncome: Long = 0L,
    val totalExpenses: Long = 0L,
    val estimatedProfit: Long = 0L,
    val profitMarginPercent: Int = 0,
    val averageDailyIncome: Long = 0L,
    val largestExpenseCategory: String = "-",
    val incomeEntryCount: Int = 0,
    val expenseEntryCount: Int = 0,
    val targetRevenueProgress: Float = 0f,
    val targetProfitProgress: Float = 0f,
    val recentEntries: List<FinancialEntry> = emptyList(),
    val trendPoints: List<FinancialTrendPoint> = emptyList()
) {
    val hasEntries: Boolean = incomeEntryCount + expenseEntryCount > 0
}

data class FinancialTrendPoint(
    val label: String,
    val income: Long,
    val expenses: Long
)
