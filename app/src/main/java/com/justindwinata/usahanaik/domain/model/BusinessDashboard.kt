package com.justindwinata.usahanaik.domain.model

data class BusinessDashboard(
    val summary: BusinessSummary,
    val healthScore: BusinessHealthScore,
    val financialSummary: FinancialSummary,
    val trend: FinancialTrend,
    val milestones: List<Milestone>,
    val tasks: List<BusinessTask>,
    val productPerformance: List<ProductPerformance>,
    val recommendations: List<String>,
    val contentIdeas: List<ContentIdea>
)

data class BusinessSummary(
    val businessName: String,
    val categoryName: String,
    val weekLabel: String
)

data class BusinessHealthScore(
    val score: Int,
    val explanation: String
)

data class FinancialSummary(
    val monthlyRevenue: String,
    val monthlyExpenses: String,
    val estimatedProfit: String,
    val profitMargin: String,
    val expenseBreakdown: List<ExpenseItem>,
    val reportSummary: String
)

data class ExpenseItem(
    val label: String,
    val percentage: Int
)

data class FinancialTrend(
    val revenuePoints: List<Float>,
    val expensePoints: List<Float>
)

data class Milestone(
    val title: String,
    val status: String,
    val progress: Float
)

data class BusinessTask(
    val title: String,
    val description: String,
    val completed: Boolean
)

data class ProductPerformance(
    val name: String,
    val signal: String,
    val marginLabel: String
)
