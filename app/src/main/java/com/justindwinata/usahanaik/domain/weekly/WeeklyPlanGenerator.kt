package com.justindwinata.usahanaik.domain.weekly

import com.justindwinata.usahanaik.domain.model.ActionDifficulty
import com.justindwinata.usahanaik.domain.model.ActionEstimatedTime
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessDiagnosis
import com.justindwinata.usahanaik.domain.model.BusinessMilestone
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.FinancialTrackingSummary
import com.justindwinata.usahanaik.domain.model.InsightCategory
import com.justindwinata.usahanaik.domain.model.MilestoneStatus
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.StockIssue
import com.justindwinata.usahanaik.domain.model.WeeklyChallenge
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyPlanFocus
import com.justindwinata.usahanaik.domain.model.WeeklyTask
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus

class WeeklyPlanGenerator {
    fun generate(
        profile: BusinessProfile,
        financialSummary: FinancialTrackingSummary,
        diagnosis: BusinessDiagnosis,
        generatedDate: String = "2026-07-19"
    ): WeeklyGrowthPlan {
        val draft = profile.draft
        val focus = selectFocus(draft, financialSummary, diagnosis)
        val tasks = generateTasks(draft, financialSummary, diagnosis, focus)
        val challenge = generateChallenge(focus)
        val milestones = generateMilestones(focus, tasks)

        return WeeklyGrowthPlan(
            title = "Weekly Growth Plan - ${focus.title}",
            generatedDate = generatedDate,
            businessName = draft.businessName.ifBlank { "UsahaNaik Business" },
            businessCategoryId = draft.categoryId,
            businessCategoryName = categoryName(draft.categoryId),
            focus = focus,
            target = targetFor(focus, tasks),
            priorityReason = focus.reason,
            tasks = tasks,
            challenge = challenge,
            milestones = milestones,
            limitationsNote = "This weekly plan is generated with deterministic rules from local profile and financial data. It is planning guidance, not a guaranteed outcome."
        )
    }

    private fun selectFocus(
        draft: BusinessSetupDraft,
        financialSummary: FinancialTrackingSummary,
        diagnosis: BusinessDiagnosis
    ): WeeklyPlanFocus {
        return when {
            BusinessChallenge.PoorFinancialRecords in draft.challenges || !financialSummary.hasEntries -> WeeklyPlanFocus(
                title = "Build financial tracking discipline",
                category = InsightCategory.Finance,
                reason = "Financial records need to be consistent before deeper decisions are reliable."
            )
            financialSummary.hasEntries && financialSummary.totalIncome > 0L &&
                financialSummary.totalExpenses.toFloat() / financialSummary.totalIncome.toFloat() >= 0.70f -> WeeklyPlanFocus(
                title = "Control operating costs",
                category = InsightCategory.Expense,
                reason = "Expenses are taking a large share of recorded income."
            )
            BusinessChallenge.LowSales in draft.challenges || financialSummary.targetRevenueProgress < 0.4f -> WeeklyPlanFocus(
                title = "Improve daily sales activity",
                category = InsightCategory.Sales,
                reason = "Sales progress needs more focused daily execution."
            )
            BusinessChallenge.InconsistentContent in draft.challenges || draft.mainFocus == MonthlyFocus.ImproveContentConsistency -> WeeklyPlanFocus(
                title = "Create consistent promotional content",
                category = InsightCategory.Content,
                reason = "Consistent content helps customers understand the offer more clearly."
            )
            BusinessChallenge.LowRepeatOrders in draft.challenges || draft.mainFocus == MonthlyFocus.IncreaseRepeatOrders -> WeeklyPlanFocus(
                title = "Increase customer follow-up and repeat orders",
                category = InsightCategory.CustomerRetention,
                reason = "Repeat customers can make weekly sales activity more stable."
            )
            BusinessChallenge.StockProblems in draft.challenges || draft.stockIssue in stockAttentionIssues -> WeeklyPlanFocus(
                title = "Improve stock control and fast-moving product tracking",
                category = InsightCategory.Stock,
                reason = "Stock flow affects cash, sales availability, and product focus."
            )
            diagnosis.riskSignals.isNotEmpty() -> WeeklyPlanFocus(
                title = "Resolve the highest attention business signal",
                category = diagnosis.riskSignals.first().category,
                reason = diagnosis.riskSignals.first().message
            )
            else -> WeeklyPlanFocus(
                title = "Strengthen weekly business execution",
                category = InsightCategory.Operations,
                reason = "The business has enough data to focus on consistent weekly execution."
            )
        }
    }

    private fun generateTasks(
        draft: BusinessSetupDraft,
        financialSummary: FinancialTrackingSummary,
        diagnosis: BusinessDiagnosis,
        focus: WeeklyPlanFocus
    ): List<WeeklyTask> {
        val tasks = mutableListOf<WeeklyTask>()
        tasks += baseTaskFor(focus)
        tasks += diagnosis.priorityActions.take(2).mapIndexed { index, action ->
            WeeklyTask(
                id = "diagnosis-${index + 1}",
                title = action.title,
                description = action.description,
                category = action.category,
                estimatedTime = action.estimatedTime,
                difficulty = action.difficulty,
                reason = action.reason,
                expectedOutcome = action.expectedOutcome
            )
        }
        tasks += categoryTasks(draft)
        tasks += challengeTasks(draft)
        if (financialSummary.hasEntries && financialSummary.estimatedProfit < 0) {
            tasks += WeeklyTask(
                id = "profit-review",
                title = "Review expenses before adding new spending",
                description = "List costs that can be reduced, delayed, or replaced this week.",
                category = InsightCategory.ProfitMargin,
                estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                difficulty = ActionDifficulty.Medium,
                reason = "Recorded expenses are higher than income.",
                expectedOutcome = "May help reduce short-term pressure while sales activity improves."
            )
        }
        tasks += fallbackTasks

        val taskLimit = when (draft.availableTime) {
            AvailableTime.UnderThreeHours -> 5
            AvailableTime.ThreeToFiveHours -> 5
            AvailableTime.SixToTenHours -> 6
            AvailableTime.MoreThanTenHours -> 7
            null -> 5
        }

        return tasks.distinctBy { it.title }.take(taskLimit).mapIndexed { index, task ->
            task.copy(id = "task-${index + 1}")
        }
    }

    private fun baseTaskFor(focus: WeeklyPlanFocus): WeeklyTask {
        return when (focus.category) {
            InsightCategory.Finance -> WeeklyTask(
                id = "base-finance",
                title = "Catat semua pemasukan dan pengeluaran selama 7 hari",
                description = "Gunakan form keuangan di UsahaNaik untuk mencatat transaksi harian agar dashboard lebih akurat.",
                category = InsightCategory.Finance,
                estimatedTime = ActionEstimatedTime.UnderFifteenMinutes,
                difficulty = ActionDifficulty.Easy,
                reason = "Catatan keuangan yang rapi membantu melihat posisi bisnis secara lebih jelas.",
                expectedOutcome = "Dapat membantu memahami pola pemasukan dan pengeluaran."
            )
            InsightCategory.Expense, InsightCategory.ProfitMargin -> WeeklyTask(
                id = "base-expense",
                title = "Review pengeluaran terbesar minggu ini",
                description = "Bandingkan biaya terbesar dengan kebutuhan operasional dan cari satu penghematan realistis.",
                category = InsightCategory.Expense,
                estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                difficulty = ActionDifficulty.Medium,
                reason = "Pengeluaran tinggi dapat menekan estimasi profit.",
                expectedOutcome = "Dapat membantu menjaga margin jika biaya yang tidak perlu dikurangi."
            )
            InsightCategory.Sales -> WeeklyTask(
                id = "base-sales",
                title = "Promosikan satu penawaran utama setiap hari",
                description = "Pilih produk atau layanan yang paling mudah dijelaskan dan promosikan konsisten selama 7 hari.",
                category = InsightCategory.Sales,
                estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                difficulty = ActionDifficulty.Medium,
                reason = "Aktivitas penjualan harian perlu fokus agar lebih mudah dievaluasi.",
                expectedOutcome = "Dapat membantu meningkatkan peluang transaksi dari produk yang sudah jelas."
            )
            InsightCategory.Content, InsightCategory.Marketing -> WeeklyTask(
                id = "base-content",
                title = "Buat 3 konten sederhana minggu ini",
                description = "Siapkan satu konten edukasi, satu testimoni, dan satu promo singkat.",
                category = InsightCategory.Content,
                estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                difficulty = ActionDifficulty.Easy,
                reason = "Konten yang konsisten membantu pelanggan memahami penawaran bisnis.",
                expectedOutcome = "Dapat membantu menjaga komunikasi promosi lebih rapi."
            )
            InsightCategory.CustomerRetention -> WeeklyTask(
                id = "base-retention",
                title = "Follow up 5 pelanggan lama",
                description = "Hubungi pelanggan sebelumnya dengan pesan singkat yang ramah dan relevan.",
                category = InsightCategory.CustomerRetention,
                estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                difficulty = ActionDifficulty.Easy,
                reason = "Repeat order membutuhkan follow-up yang konsisten.",
                expectedOutcome = "Dapat membuka peluang pembelian ulang."
            )
            InsightCategory.Stock -> WeeklyTask(
                id = "base-stock",
                title = "Catat 5 produk paling cepat dan lambat bergerak",
                description = "Tandai produk yang cepat habis dan produk yang jarang terjual sebelum restock.",
                category = InsightCategory.Stock,
                estimatedTime = ActionEstimatedTime.ThirtyToSixtyMinutes,
                difficulty = ActionDifficulty.Medium,
                reason = "Kontrol stok membantu mengurangi uang tertahan di barang lambat.",
                expectedOutcome = "Dapat membantu keputusan restock lebih jelas."
            )
            else -> WeeklyTask(
                id = "base-ops",
                title = "Pilih satu fokus operasional minggu ini",
                description = "Tentukan satu area utama: penjualan, biaya, konten, stok, atau repeat order.",
                category = InsightCategory.Operations,
                estimatedTime = ActionEstimatedTime.UnderFifteenMinutes,
                difficulty = ActionDifficulty.Easy,
                reason = "Satu fokus lebih mudah dijalankan oleh pemilik UMKM yang waktunya terbatas.",
                expectedOutcome = "Dapat membantu mengurangi keputusan yang terlalu tersebar."
            )
        }
    }

    private fun categoryTasks(draft: BusinessSetupDraft): List<WeeklyTask> {
        return when (draft.categoryId) {
            "food_beverage" -> listOf(task("category-food-cost", "Hitung margin satu menu terlaris", "Catat harga jual, biaya bahan, dan estimasi margin untuk satu menu utama.", InsightCategory.Expense))
            "warung_kelontong" -> listOf(task("category-warung-stock", "Pisahkan uang usaha dan pribadi minggu ini", "Gunakan catatan atau wadah terpisah agar uang restock lebih mudah dipantau.", InsightCategory.Finance))
            "skincare_beauty" -> listOf(task("category-skincare-repeat", "Kumpulkan satu testimoni pelanggan", "Minta feedback singkat dari pelanggan yang cocok dengan produk.", InsightCategory.CustomerRetention))
            "online_shop_reseller" -> listOf(task("category-online-fee", "Cek biaya marketplace untuk produk utama", "Bandingkan biaya platform dengan margin produk yang sering dijual.", InsightCategory.Expense))
            "laundry" -> listOf(task("category-laundry-package", "Rancang satu paket laundry mingguan", "Buat paket sederhana untuk pelanggan repeat atau keluarga.", InsightCategory.CustomerRetention))
            else -> emptyList()
        }
    }

    private fun challengeTasks(draft: BusinessSetupDraft): List<WeeklyTask> {
        val tasks = mutableListOf<WeeklyTask>()
        if (BusinessChallenge.InconsistentContent in draft.challenges) {
            tasks += task("challenge-content", "Jadwalkan 3 posting promosi", "Tentukan tanggal, topik, dan CTA untuk tiga konten minggu ini.", InsightCategory.Content)
        }
        if (BusinessChallenge.StockProblems in draft.challenges || draft.stockIssue in stockAttentionIssues) {
            tasks += task("challenge-stock", "Review barang stok bermasalah", "Catat barang yang sering habis atau lambat bergerak sebelum membeli stok baru.", InsightCategory.Stock)
        }
        if (BusinessChallenge.LowRepeatOrders in draft.challenges) {
            tasks += task("challenge-repeat", "Buat daftar pelanggan untuk follow-up", "Pilih 5 pelanggan lama yang bisa dihubungi dengan penawaran relevan.", InsightCategory.CustomerRetention)
        }
        return tasks
    }

    private fun task(
        id: String,
        title: String,
        description: String,
        category: InsightCategory
    ): WeeklyTask {
        return WeeklyTask(
            id = id,
            title = title,
            description = description,
            category = category,
            estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
            difficulty = ActionDifficulty.Easy,
            status = WeeklyTaskStatus.Pending,
            reason = "Task ini dipilih berdasarkan kategori, tantangan, atau diagnosis bisnis.",
            expectedOutcome = "Dapat membantu pemilik bisnis mengambil langkah mingguan yang lebih terarah."
        )
    }

    private fun generateChallenge(focus: WeeklyPlanFocus): WeeklyChallenge {
        return when (focus.category) {
            InsightCategory.Finance -> WeeklyChallenge(
                title = "7-Day Sales Tracking Challenge",
                description = "Catat pemasukan dan pengeluaran setiap hari selama satu minggu.",
                checklistItems = listOf("Catat pemasukan harian", "Catat pengeluaran harian", "Review total di akhir minggu"),
                completionTarget = "7 hari pencatatan selesai",
                motivationalCopy = "Bangun kebiasaan kecil yang membuat keputusan bisnis lebih jelas."
            )
            InsightCategory.Content, InsightCategory.Marketing -> WeeklyChallenge(
                title = "3 Content Posts Challenge",
                description = "Buat tiga konten sederhana: edukasi, testimoni, dan promo.",
                checklistItems = listOf("Buat konten edukasi", "Buat konten testimoni", "Buat konten promo"),
                completionTarget = "3 konten siap atau terpublikasi",
                motivationalCopy = "Konsistensi kecil lebih baik daripada menunggu ide sempurna."
            )
            InsightCategory.CustomerRetention -> WeeklyChallenge(
                title = "Repeat Customer Follow-Up Challenge",
                description = "Hubungi pelanggan lama dengan pesan yang relevan dan ramah.",
                checklistItems = listOf("Pilih 5 pelanggan", "Kirim follow-up", "Catat respon pelanggan"),
                completionTarget = "5 pelanggan lama dihubungi",
                motivationalCopy = "Pelanggan lama sering lebih mudah diajak bicara daripada calon pelanggan baru."
            )
            InsightCategory.Stock -> WeeklyChallenge(
                title = "Top 5 Product Movement Challenge",
                description = "Identifikasi produk yang cepat bergerak dan yang lambat bergerak.",
                checklistItems = listOf("Catat 5 produk cepat laku", "Catat 5 produk lambat", "Review rencana restock"),
                completionTarget = "Daftar produk cepat dan lambat selesai",
                motivationalCopy = "Stok yang jelas membantu uang usaha bergerak lebih sehat."
            )
            InsightCategory.Expense, InsightCategory.ProfitMargin -> WeeklyChallenge(
                title = "Expense Review Challenge",
                description = "Review kategori biaya terbesar dan pilih satu tindakan penghematan realistis.",
                checklistItems = listOf("Cek biaya terbesar", "Pilih satu biaya untuk dikurangi", "Catat dampaknya minggu depan"),
                completionTarget = "1 tindakan kontrol biaya dipilih",
                motivationalCopy = "Kontrol biaya kecil bisa membuat keputusan margin lebih disiplin."
            )
            else -> WeeklyChallenge(
                title = "Weekly Execution Challenge",
                description = "Selesaikan minimal lima task yang ada di plan minggu ini.",
                checklistItems = listOf("Pilih task prioritas", "Kerjakan bertahap", "Review progres akhir minggu"),
                completionTarget = "5 task selesai",
                motivationalCopy = "Eksekusi terarah membantu bisnis bergerak lebih konsisten."
            )
        }
    }

    private fun generateMilestones(
        focus: WeeklyPlanFocus,
        tasks: List<WeeklyTask>
    ): List<BusinessMilestone> {
        val milestones = mutableListOf(
            BusinessMilestone(
                id = "milestone-1",
                title = "Complete core weekly task",
                description = "Finish the first task connected to this week's focus.",
                relatedTaskIds = tasks.take(1).map { it.id }
            ),
            BusinessMilestone(
                id = "milestone-2",
                title = "Complete 3 weekly actions",
                description = "Finish at least three tasks from this plan.",
                relatedTaskIds = tasks.take(3).map { it.id }
            ),
            BusinessMilestone(
                id = "milestone-3",
                title = challengeMilestoneTitle(focus),
                description = "Complete the weekly challenge checklist.",
                relatedTaskIds = tasks.take(5).map { it.id }
            )
        )
        if (tasks.size >= 6) {
            milestones += BusinessMilestone(
                id = "milestone-4",
                title = "Complete 5 weekly actions",
                description = "Finish five tasks for stronger weekly progress.",
                relatedTaskIds = tasks.take(5).map { it.id }
            )
        }
        return milestones.take(5)
    }

    private fun challengeMilestoneTitle(focus: WeeklyPlanFocus): String {
        return when (focus.category) {
            InsightCategory.Finance -> "Complete 7 days of financial tracking"
            InsightCategory.Content, InsightCategory.Marketing -> "Create 3 promotional content posts"
            InsightCategory.CustomerRetention -> "Contact 5 previous customers"
            InsightCategory.Stock -> "Review fast-moving and slow-moving products"
            InsightCategory.Expense, InsightCategory.ProfitMargin -> "Review largest expense category"
            else -> "Complete weekly execution challenge"
        }
    }

    private fun targetFor(focus: WeeklyPlanFocus, tasks: List<WeeklyTask>): String {
        return "Complete at least ${minOf(5, tasks.size)} tasks and finish the ${challengeMilestoneTitle(focus).lowercase()} milestone."
    }

    private fun categoryName(categoryId: String?): String {
        return when (categoryId) {
            "food_beverage" -> "Food & Beverage"
            "warung_kelontong" -> "Warung / Toko Kelontong"
            "skincare_beauty" -> "Skincare & Beauty"
            "fashion_thrift" -> "Fashion / Thrift"
            "laundry" -> "Laundry"
            "online_shop_reseller" -> "Online Shop / Reseller"
            "coffee_beverage_shop" -> "Coffee / Beverage Shop"
            "service_business" -> "Service Business"
            "digital_service" -> "Digital Service"
            else -> "Other Business"
        }
    }

    private companion object {
        val stockAttentionIssues = setOf(
            StockIssue.OftenOutOfStock,
            StockIssue.SlowMovingStock
        )

        val fallbackTasks = listOf(
            WeeklyTask(
                id = "fallback-review",
                title = "Review angka bisnis minggu ini",
                description = "Bandingkan pemasukan, pengeluaran, dan estimasi profit sebelum memilih tindakan berikutnya.",
                category = InsightCategory.Finance,
                estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                difficulty = ActionDifficulty.Easy,
                reason = "Review mingguan menjaga keputusan tetap terkait dengan data.",
                expectedOutcome = "Dapat membantu melihat area yang perlu diprioritaskan."
            ),
            WeeklyTask(
                id = "fallback-focus",
                title = "Tentukan satu fokus bisnis minggu ini",
                description = "Pilih satu fokus utama agar waktu terbatas tidak tersebar terlalu banyak.",
                category = InsightCategory.Operations,
                estimatedTime = ActionEstimatedTime.UnderFifteenMinutes,
                difficulty = ActionDifficulty.Easy,
                reason = "Fokus tunggal lebih realistis untuk UMKM.",
                expectedOutcome = "Dapat membantu menjalankan action dengan lebih konsisten."
            ),
            WeeklyTask(
                id = "fallback-customer",
                title = "Catat 3 pertanyaan pelanggan",
                description = "Gunakan pertanyaan pelanggan sebagai bahan konten edukasi atau perbaikan penawaran.",
                category = InsightCategory.Content,
                estimatedTime = ActionEstimatedTime.FifteenToThirtyMinutes,
                difficulty = ActionDifficulty.Easy,
                reason = "Pertanyaan pelanggan adalah sumber insight praktis.",
                expectedOutcome = "Dapat membantu membuat konten dan penawaran lebih relevan."
            )
        )
    }
}
