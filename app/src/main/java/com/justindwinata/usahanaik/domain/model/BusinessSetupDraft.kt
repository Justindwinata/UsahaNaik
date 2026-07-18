package com.justindwinata.usahanaik.domain.model

data class BusinessSetupDraft(
    val businessName: String = "",
    val ownerName: String = "",
    val categoryId: String? = null,
    val sellingChannel: SellingChannel? = null,
    val businessLocation: String = "",
    val businessStage: BusinessStage? = null,
    val startingCapital: String = "",
    val monthlyRevenue: String = "",
    val monthlyExpenses: String = "",
    val estimatedMonthlyProfit: String = "",
    val averageDailyTransactions: String = "",
    val averageTransactionValue: String = "",
    val productCount: String = "",
    val bestSellingProduct: String = "",
    val highestMarginProduct: String = "",
    val mainCostDriver: CostDriver? = null,
    val stockIssue: StockIssue? = null,
    val challenges: Set<BusinessChallenge> = emptySet(),
    val targetMonthlyRevenue: String = "",
    val targetMonthlyProfit: String = "",
    val mainFocus: MonthlyFocus? = null,
    val availableTime: AvailableTime? = null
) {
    val isEmpty: Boolean
        get() = this == BusinessSetupDraft()
}

enum class SellingChannel(val label: String) {
    OfflineStore("Offline store"),
    Instagram("Instagram"),
    TikTok("TikTok"),
    WhatsApp("WhatsApp"),
    Marketplace("Marketplace"),
    MixedOnlineOffline("Mixed online/offline"),
    Other("Other")
}

enum class BusinessStage(val label: String) {
    JustStarting("Just starting"),
    RunningUnderSixMonths("Running under 6 months"),
    RunningSixToTwelveMonths("Running 6-12 months"),
    RunningMoreThanOneYear("Running more than 1 year")
}

enum class CostDriver(val label: String) {
    RawMaterials("Raw materials"),
    Rent("Rent"),
    Payroll("Payroll"),
    AdsPromotions("Ads/promotions"),
    Packaging("Packaging"),
    Shipping("Shipping"),
    PlatformFees("Platform fees"),
    Other("Other")
}

enum class StockIssue(val label: String) {
    OftenOutOfStock("Often out of stock"),
    SlowMovingStock("Slow-moving stock"),
    NoStockIssue("No stock issue"),
    NotApplicable("Not applicable")
}

enum class BusinessChallenge(val label: String) {
    LowSales("Low sales"),
    LowProfitMargin("Low profit margin"),
    PoorFinancialRecords("Poor financial records"),
    InconsistentContent("Inconsistent content"),
    LowRepeatOrders("Low repeat orders"),
    StockProblems("Stock problems"),
    HighExpenses("High expenses"),
    UnclearTargetMarket("Unclear target market"),
    LackOfPromotionIdeas("Lack of promotion ideas"),
    TimeManagementProblem("Time management problem")
}

enum class MonthlyFocus(val label: String) {
    ImproveSales("Improve sales"),
    ReduceExpenses("Reduce expenses"),
    ImproveContentConsistency("Improve content consistency"),
    IncreaseRepeatOrders("Increase repeat orders"),
    OrganizeFinance("Organize finance"),
    ImproveProductStrategy("Improve product strategy"),
    ImproveOperations("Improve operations")
}

enum class AvailableTime(val label: String) {
    UnderThreeHours("Under 3 hours"),
    ThreeToFiveHours("3-5 hours"),
    SixToTenHours("6-10 hours"),
    MoreThanTenHours("More than 10 hours")
}
