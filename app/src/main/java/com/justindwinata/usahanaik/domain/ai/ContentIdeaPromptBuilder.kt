package com.justindwinata.usahanaik.domain.ai

import com.justindwinata.usahanaik.domain.model.ContentIdeaRequest

class ContentIdeaPromptBuilder {
    fun buildPrompt(request: ContentIdeaRequest): String {
        val draft = request.businessProfile?.draft
        val category = draft?.categoryId ?: "unknown"
        val challenges = draft?.challenges?.joinToString { it.label }.orEmpty().ifBlank { "none provided" }
        val weeklyFocus = request.weeklyPlan?.focus?.title ?: "no active weekly plan"
        val financialContext = "income=${request.financialSummary.totalIncome}, expenses=${request.financialSummary.totalExpenses}, estimatedProfit=${request.financialSummary.estimatedProfit}, margin=${request.financialSummary.profitMarginPercent}%"

        return """
            You are helping an Indonesian UMKM owner draft safe content ideas.
            Business category: $category
            Business challenges: $challenges
            Content goal: ${request.goal.label}
            Platform: ${request.platform.label}
            Weekly focus: $weeklyFocus
            Financial context summary: $financialContext

            Safety rules:
            - Do not promise guaranteed sales, profit, viral reach, or business success.
            - Do not make medical, legal, tax, or professional financial claims.
            - For skincare/beauty, avoid cure claims and guaranteed before/after results.
            - For food/beverage, avoid health claims unless proof is provided.
            - Avoid fake scarcity such as "only today" unless the campaign truly plans it.
            - Ideas are suggestions and must be reviewed before posting.

            Return structured content ideas with title, platform, content type, goal, angle, caption draft, hook, CTA, visual suggestion, posting note, related challenge, and safety note.
        """.trimIndent()
    }
}
