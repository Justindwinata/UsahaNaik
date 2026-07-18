package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.data.ai.LocalContentIdeaProvider
import com.justindwinata.usahanaik.domain.model.BusinessDashboard
import com.justindwinata.usahanaik.domain.model.BusinessHealthScore
import com.justindwinata.usahanaik.domain.model.BusinessSummary
import com.justindwinata.usahanaik.domain.model.BusinessTask
import com.justindwinata.usahanaik.domain.model.ExpenseItem
import com.justindwinata.usahanaik.domain.model.FinancialSummary
import com.justindwinata.usahanaik.domain.model.FinancialTrend
import com.justindwinata.usahanaik.domain.model.Milestone
import com.justindwinata.usahanaik.domain.model.ProductPerformance
import com.justindwinata.usahanaik.domain.model.WeeklyPlan

class SampleGrowthRepository(
    private val categoryRepository: BusinessCategoryRepository = SampleBusinessCategoryRepository(),
    private val contentIdeaProvider: LocalContentIdeaProvider = LocalContentIdeaProvider()
) {
    private val sampleBusinessName = "Toko Rasa Naik"
    private val sampleCategory = categoryRepository.getCategories().first { it.id == "food_beverage" }

    fun getDashboardPreview(): BusinessDashboard {
        val contentIdeas = contentIdeaProvider.generateIdeas(sampleCategory, sampleBusinessName)
        return BusinessDashboard(
            summary = BusinessSummary(
                businessName = sampleBusinessName,
                categoryName = sampleCategory.displayName,
                weekLabel = "Minggu 1 - Juli 2026"
            ),
            healthScore = BusinessHealthScore(
                score = 72,
                explanation = "Fondasi bisnis mulai rapi. Fokus minggu ini: catatan penjualan, margin menu utama, dan konten edukasi."
            ),
            financialSummary = FinancialSummary(
                monthlyRevenue = "Rp18,4jt",
                monthlyExpenses = "Rp13,6jt",
                estimatedProfit = "Rp4,8jt",
                profitMargin = "26%",
                expenseBreakdown = listOf(
                    ExpenseItem("Bahan baku", 46),
                    ExpenseItem("Operasional", 28),
                    ExpenseItem("Marketing", 14),
                    ExpenseItem("Lainnya", 12)
                ),
                reportSummary = "Arus masuk terlihat stabil, tetapi biaya bahan baku perlu dipantau agar margin menu utama tidak turun."
            ),
            trend = FinancialTrend(
                revenuePoints = listOf(10f, 12f, 11.5f, 14f, 15f, 17.5f, 18.4f),
                expensePoints = listOf(8f, 8.4f, 9.2f, 10.5f, 11.3f, 12.8f, 13.6f)
            ),
            milestones = listOf(
                Milestone("Catat penjualan 7 hari", "On track", 0.71f),
                Milestone("Hitung margin 5 produk", "Mulai", 0.4f),
                Milestone("Buat 3 konten edukasi", "Belum selesai", 0.33f)
            ),
            tasks = listOf(
                BusinessTask("Catat semua transaksi hari ini", "Pisahkan pemasukan tunai dan transfer.", true),
                BusinessTask("Hitung margin 2 menu terlaris", "Bandingkan harga jual dengan bahan baku.", false),
                BusinessTask("Tulis 3 pertanyaan pelanggan", "Pakai sebagai bahan konten edukasi.", false),
                BusinessTask("Pisahkan kas usaha dan pribadi", "Buat catatan sederhana di akhir hari.", false)
            ),
            productPerformance = listOf(
                ProductPerformance("Paket Ayam Geprek", "Terlaris", "Margin 31%"),
                ProductPerformance("Es Kopi Susu", "Repeat tinggi", "Margin 42%"),
                ProductPerformance("Snack Box", "Perlu promo", "Margin 24%")
            ),
            recommendations = listOf(
                "Naikkan visibility produk dengan margin tertinggi di konten minggu ini.",
                "Review bahan baku yang paling sering naik harga sebelum membuat promo.",
                "Gunakan pertanyaan pelanggan sebagai ide edukasi di Instagram atau TikTok."
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
}
