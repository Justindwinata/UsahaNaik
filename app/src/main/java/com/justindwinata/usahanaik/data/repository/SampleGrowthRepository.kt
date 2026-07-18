package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.ai.LocalContentIdeaProvider
import com.justindwinata.usahanaik.domain.model.BusinessDashboard
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessHealthScore
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessSummary
import com.justindwinata.usahanaik.domain.model.BusinessTask
import com.justindwinata.usahanaik.domain.model.ExpenseItem
import com.justindwinata.usahanaik.domain.model.FinancialSummary
import com.justindwinata.usahanaik.domain.model.FinancialTrend
import com.justindwinata.usahanaik.domain.model.Milestone
import com.justindwinata.usahanaik.domain.model.ProductPerformance
import com.justindwinata.usahanaik.domain.model.WeeklyPlan
import com.justindwinata.usahanaik.domain.setup.BusinessSetupCalculator

class SampleGrowthRepository(
    private val categoryRepository: BusinessCategoryRepository = SampleBusinessCategoryRepository(),
    private val contentIdeaProvider: LocalContentIdeaProvider = LocalContentIdeaProvider()
) {
    private val sampleBusinessName = "Toko Rasa Naik"
    private val sampleCategory = categoryRepository.getCategories().first { it.id == "food_beverage" }

    fun getDashboardPreview(draft: BusinessSetupDraft? = null): BusinessDashboard {
        val draftCategory = draft?.categoryId?.let { id ->
            categoryRepository.getCategories().firstOrNull { it.id == id }
        }
        val activeCategory = draftCategory ?: sampleCategory
        val activeBusinessName = draft?.businessName?.takeIf { it.isNotBlank() } ?: sampleBusinessName
        val revenue = draft?.monthlyRevenue?.let(BusinessSetupCalculator::parseIndonesianNumber)
        val expenses = draft?.monthlyExpenses?.let(BusinessSetupCalculator::parseIndonesianNumber)
        val profit = draft?.estimatedMonthlyProfit?.let(BusinessSetupCalculator::parseIndonesianNumber)
        val margin = draft?.let(BusinessSetupCalculator::profitMarginPercent)
        val revenueGap = draft?.let(BusinessSetupCalculator::revenueTargetGap)
        val profitGap = draft?.let(BusinessSetupCalculator::profitTargetGap)
        val contentIdeas = contentIdeaProvider.generateIdeas(activeCategory, activeBusinessName)

        return BusinessDashboard(
            summary = BusinessSummary(
                businessName = activeBusinessName,
                categoryName = activeCategory.displayName,
                weekLabel = "Minggu 1 - Juli 2026"
            ),
            healthScore = BusinessHealthScore(
                score = if (draft == null) 72 else 74,
                explanation = if (draft == null) {
                    "Fondasi bisnis mulai rapi. Fokus minggu ini: catatan penjualan, margin menu utama, dan konten edukasi."
                } else {
                    "Dashboard ini memakai draft setup. Fokus awal: ${draft.mainFocus?.label ?: activeCategory.sampleRecommendedGoal}."
                }
            ),
            financialSummary = FinancialSummary(
                monthlyRevenue = revenue?.let(BusinessSetupCalculator::formatRupiah) ?: "Rp18,4jt",
                monthlyExpenses = expenses?.let(BusinessSetupCalculator::formatRupiah) ?: "Rp13,6jt",
                estimatedProfit = profit?.let(BusinessSetupCalculator::formatRupiah) ?: "Rp4,8jt",
                profitMargin = margin?.let { "$it%" } ?: "26%",
                expenseBreakdown = listOf(
                    ExpenseItem("Bahan baku", 46),
                    ExpenseItem("Operasional", 28),
                    ExpenseItem("Marketing", 14),
                    ExpenseItem("Lainnya", 12)
                ),
                reportSummary = if (draft == null) {
                    "Arus masuk terlihat stabil, tetapi biaya bahan baku perlu dipantau agar margin menu utama tidak turun."
                } else {
                    "Target revenue gap ${BusinessSetupCalculator.formatRupiah(revenueGap)} dan target profit gap ${BusinessSetupCalculator.formatRupiah(profitGap)}. Angka ini preview, bukan nasihat finansial profesional."
                }
            ),
            trend = draftTrendOrSample(revenue, expenses),
            milestones = listOf(
                Milestone("Catat penjualan 7 hari", "On track", 0.71f),
                Milestone("Hitung margin 5 produk", "Mulai", 0.4f),
                Milestone("Buat 3 konten edukasi", "Belum selesai", 0.33f)
            ),
            tasks = draftTasksOrSample(draft),
            productPerformance = listOf(
                ProductPerformance(draft?.bestSellingProduct?.takeIf { it.isNotBlank() } ?: "Paket Ayam Geprek", "Terlaris", "Margin 31%"),
                ProductPerformance(draft?.highestMarginProduct?.takeIf { it.isNotBlank() } ?: "Es Kopi Susu", "Margin tinggi", "Margin 42%"),
                ProductPerformance("Snack Box", "Perlu promo", "Margin 24%")
            ),
            recommendations = listOf(
                "Naikkan visibility produk dengan margin tertinggi di konten minggu ini.",
                "Review biaya utama: ${draft?.mainCostDriver?.label ?: "bahan baku"} sebelum membuat promo.",
                "Gunakan fokus ${draft?.mainFocus?.label ?: activeCategory.recommendedMonthlyFocus.label} sebagai tema aksi mingguan."
            ),
            contentIdeas = contentIdeas
        )
    }

    fun getWeeklyPlanPreview(): WeeklyPlan = WeeklyPlan(
        title = "Week 1 - Build Business Foundation",
        target = "Membuat baseline penjualan, margin produk utama, dan kebiasaan tracking harian.",
        priorityActions = listOf(
            "Record all daily sales.",
            "Calculate margin for 5 key products.",
            "Identify top 3 customer questions.",
            "Create 3 educational content posts.",
            "Separate business cash from personal cash."
        ),
        dailyTasks = listOf(
            BusinessTask("Senin: catat semua transaksi", "Mulai dari pemasukan, diskon, dan biaya kecil.", true),
            BusinessTask("Selasa: cek bahan baku utama", "Tandai bahan yang paling memengaruhi margin.", true),
            BusinessTask("Rabu: hitung margin produk", "Ambil 5 produk paling sering dibeli.", false),
            BusinessTask("Kamis: tulis ide konten edukasi", "Gunakan pertanyaan pelanggan sebagai angle.", false),
            BusinessTask("Jumat: review kas usaha", "Pisahkan kebutuhan pribadi dan operasional.", false)
        ),
        challenge = "Complete 7 days of consistent sales tracking.",
        milestoneProgress = 0.58f
    )

    fun getContentIdeaPreview() = getDashboardPreview().contentIdeas

    private fun draftTrendOrSample(revenue: Long?, expenses: Long?): FinancialTrend {
        if (revenue == null || expenses == null) {
            return FinancialTrend(
                revenuePoints = listOf(10f, 12f, 11.5f, 14f, 15f, 17.5f, 18.4f),
                expensePoints = listOf(8f, 8.4f, 9.2f, 10.5f, 11.3f, 12.8f, 13.6f)
            )
        }

        val revenueBase = (revenue / 1_000_000f).coerceAtLeast(1f)
        val expenseBase = (expenses / 1_000_000f).coerceAtLeast(1f)
        return FinancialTrend(
            revenuePoints = listOf(0.62f, 0.68f, 0.73f, 0.81f, 0.88f, 0.94f, 1f).map { revenueBase * it },
            expensePoints = listOf(0.7f, 0.74f, 0.8f, 0.84f, 0.9f, 0.96f, 1f).map { expenseBase * it }
        )
    }

    private fun draftTasksOrSample(draft: BusinessSetupDraft?): List<BusinessTask> {
        if (draft == null) {
            return listOf(
                BusinessTask("Catat semua transaksi hari ini", "Pisahkan pemasukan tunai dan transfer.", true),
                BusinessTask("Hitung margin 2 menu terlaris", "Bandingkan harga jual dengan bahan baku.", false),
                BusinessTask("Tulis 3 pertanyaan pelanggan", "Pakai sebagai bahan konten edukasi.", false),
                BusinessTask("Pisahkan kas usaha dan pribadi", "Buat catatan sederhana di akhir hari.", false)
            )
        }

        return buildList {
            add(BusinessTask("Catat semua transaksi hari ini", "Mulai dari revenue, expense, dan profit harian.", false))
            if (BusinessChallenge.PoorFinancialRecords in draft.challenges) {
                add(BusinessTask("Rapikan catatan finansial", "Pisahkan kas usaha dan kas pribadi minggu ini.", false))
            }
            if (BusinessChallenge.InconsistentContent in draft.challenges) {
                add(BusinessTask("Buat 3 konten konsisten", "Gunakan pertanyaan pelanggan sebagai angle edukasi.", false))
            }
            if (BusinessChallenge.StockProblems in draft.challenges) {
                add(BusinessTask("Review stok bermasalah", "Tandai item slow-moving dan item yang sering habis.", false))
            }
            if (BusinessChallenge.LowSales in draft.challenges) {
                add(BusinessTask("Susun promo ringan", "Pilih satu produk unggulan untuk campaign minggu ini.", false))
            }
            if (size == 1) {
                add(BusinessTask("Review fokus bulanan", "Turunkan target menjadi 3 aksi kecil yang bisa dilakukan.", false))
            }
        }.take(5)
    }
}
