package com.justindwinata.usahanaik.data.ai

import com.justindwinata.usahanaik.data.repository.SampleBusinessCategoryRepository
import com.justindwinata.usahanaik.domain.ai.ContentIdeaProvider
import com.justindwinata.usahanaik.domain.model.BusinessCategory
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.ContentGenerationSource
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaRequest
import com.justindwinata.usahanaik.domain.model.ContentIdeaResult
import com.justindwinata.usahanaik.domain.model.ContentIdeaType
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import com.justindwinata.usahanaik.domain.model.PromotionCampaign

class LocalContentIdeaProvider : ContentIdeaProvider {
    override fun generateIdeas(
        category: BusinessCategory,
        businessName: String
    ): List<ContentIdea> {
        val request = ContentIdeaRequest(
            businessProfile = null,
            goal = ContentGoal.ProductEducation,
            platform = ContentPlatform.InstagramReels,
            ideaCount = 6
        )
        return generateFor(
            categoryId = category.id,
            businessName = businessName,
            challenges = emptySet(),
            request = request
        ).ideas
    }

    override fun generateIdeas(request: ContentIdeaRequest): ContentIdeaResult {
        val draft = request.businessProfile?.draft
        val categoryId = draft?.categoryId ?: "other_business"
        val businessName = draft?.businessName?.takeIf { it.isNotBlank() } ?: "Bisnis Anda"
        return generateFor(
            categoryId = categoryId,
            businessName = businessName,
            challenges = draft?.challenges.orEmpty(),
            request = request
        )
    }

    private fun generateFor(
        categoryId: String,
        businessName: String,
        challenges: Set<BusinessChallenge>,
        request: ContentIdeaRequest
    ): ContentIdeaResult {
        val ideas = (categoryIdeas(categoryId, businessName, request) +
            challengeIdeas(challenges, businessName, request) +
            universalIdeas(businessName, request))
            .distinctBy { it.title }
            .take(request.ideaCount.coerceIn(5, 10))

        return ContentIdeaResult(
            ideas = ideas,
            promotionCampaigns = promotionCampaigns(categoryId, businessName),
            source = ContentGenerationSource.Local,
            usedFallback = false
        )
    }

    private fun categoryIdeas(
        categoryId: String,
        businessName: String,
        request: ContentIdeaRequest
    ): List<ContentIdea> {
        return when (categoryId) {
            "skincare_beauty" -> listOf(
                idea("3 Kesalahan Memilih Skincare untuk Pemula", ContentIdeaType.Educational, request, "Bahas kesalahan umum tanpa klaim medis, lalu arahkan pelanggan untuk memahami kebutuhan kulitnya.", "Jangan klaim menyembuhkan jerawat atau menjamin hasil."),
                idea("Basic Routine untuk Pemula", ContentIdeaType.BundleCampaign, request, "Kenalkan urutan produk sederhana dengan bahasa aman dan mudah dipahami.", "Gunakan kata seperti 'dapat membantu' dan hindari janji hasil."),
                idea("Cara Membaca Ingredient Secara Sederhana", ContentIdeaType.Educational, request, "Edukasi ingredient sebagai informasi umum, bukan saran medis.", "Sarankan konsultasi profesional untuk masalah kulit serius."),
                idea("Format Testimoni Pelanggan", ContentIdeaType.Testimonial, request, "Tampilkan pengalaman pelanggan dengan wording jujur dan tidak berlebihan.", "Hindari before/after yang menjamin hasil sama untuk semua orang.")
            )
            "food_beverage", "coffee_beverage_shop" -> listOf(
                idea("Menu Favorit Pelanggan Minggu Ini", ContentIdeaType.Promotional, request, "Sorot menu yang sering dibeli dan alasan pelanggan menyukainya.", "Hindari klaim kesehatan kecuali punya bukti."),
                idea("Behind the Scenes Persiapan Menu", ContentIdeaType.BehindTheScenes, request, "Tunjukkan proses persiapan yang rapi dan menggugah selera.", "Pastikan visual makanan terlihat nyata dan tidak menyesatkan."),
                idea("Bundle Menu Hemat", ContentIdeaType.BundleCampaign, request, "Tawarkan kombinasi menu dengan manfaat praktis untuk pelanggan.", "Gunakan promo yang benar-benar tersedia."),
                idea("Daily Menu Story", ContentIdeaType.DailyStoryUpdate, request, "Bagikan menu hari ini dengan CTA pemesanan yang jelas.", "Hindari fake scarcity seperti 'hanya hari ini' jika tidak benar.")
            )
            "warung_kelontong" -> listOf(
                idea("Paket Kebutuhan Harian", ContentIdeaType.BundleCampaign, request, "Gabungkan produk fast-moving untuk kebutuhan rumah tangga.", "Pastikan harga dan stok sesuai kondisi toko."),
                idea("Restock Produk yang Sering Dicari", ContentIdeaType.DailyStoryUpdate, request, "Kabarkan produk yang baru tersedia untuk warga sekitar.", "Jangan menyebut stok terbatas jika tidak direncanakan."),
                idea("Tips Belanja Mingguan Lebih Rapi", ContentIdeaType.Educational, request, "Bantu pelanggan membuat daftar belanja rumah tangga.", "Hindari klaim penghematan yang tidak bisa dibuktikan."),
                idea("Produk Cepat Laku Minggu Ini", ContentIdeaType.ProductComparison, request, "Bandingkan pilihan produk harian secara informatif.", "Gunakan informasi produk yang akurat.")
            )
            "online_shop_reseller", "fashion_thrift" -> listOf(
                idea("Perbandingan Produk untuk Pemula", ContentIdeaType.ProductComparison, request, "Bantu pelanggan memilih produk berdasarkan kebutuhan dan budget.", "Jangan menjelekkan merek lain."),
                idea("Packing Process", ContentIdeaType.BehindTheScenes, request, "Tunjukkan proses packing untuk membangun trust.", "Pastikan proses yang ditampilkan sesuai praktik asli."),
                idea("FAQ Sebelum Membeli", ContentIdeaType.CustomerFaq, request, "Jawab pertanyaan ukuran, bahan, stok, atau pengiriman.", "Jangan menjanjikan pengiriman yang tidak bisa dipenuhi."),
                idea("Highlight Produk Margin Sehat", ContentIdeaType.Promotional, request, "Promosikan produk unggulan dengan angle manfaat yang jujur.", "Hindari klaim hasil berlebihan.")
            )
            "laundry" -> listOf(
                idea("Paket Laundry Mingguan", ContentIdeaType.BundleCampaign, request, "Tawarkan paket praktis untuk pelanggan repeat.", "Pastikan syarat paket jelas."),
                idea("Tips Merawat Bahan Pakaian", ContentIdeaType.Educational, request, "Berikan tips sederhana agar pakaian lebih terawat.", "Hindari klaim teknis yang tidak pasti."),
                idea("Before/After Cleaning Story", ContentIdeaType.BehindTheScenes, request, "Tunjukkan proses layanan dengan hasil yang realistis.", "Gunakan foto asli dan hindari janji semua noda pasti hilang."),
                idea("Repeat Customer Offer", ContentIdeaType.Promotional, request, "Ajak pelanggan lama kembali menggunakan layanan.", "Tulis periode promo dengan jujur.")
            )
            else -> listOf(
                idea("Kenapa pelanggan memilih $businessName", ContentIdeaType.Testimonial, request, "Gunakan bukti sosial ringan untuk membangun kepercayaan.", "Gunakan testimoni jujur dan tidak dibuat-buat."),
                idea("Cerita di balik $businessName", ContentIdeaType.BehindTheScenes, request, "Tampilkan proses harian agar pelanggan merasa lebih dekat.", "Hindari klaim yang tidak bisa dibuktikan."),
                idea("FAQ pelanggan minggu ini", ContentIdeaType.CustomerFaq, request, "Jawab pertanyaan yang paling sering muncul dari pelanggan.", "Pastikan informasi mudah diverifikasi.")
            )
        }
    }

    private fun challengeIdeas(
        challenges: Set<BusinessChallenge>,
        businessName: String,
        request: ContentIdeaRequest
    ): List<ContentIdea> {
        val ideas = mutableListOf<ContentIdea>()
        if (BusinessChallenge.InconsistentContent in challenges) {
            ideas += idea("3 Konten Konsisten untuk $businessName", ContentIdeaType.DailyStoryUpdate, request, "Buat pola konten edukasi, bukti sosial, dan promo agar posting lebih teratur.", "Review semua caption sebelum posting.")
        }
        if (BusinessChallenge.LowRepeatOrders in challenges) {
            ideas += idea("Pesan Follow-Up Pelanggan Lama", ContentIdeaType.ProblemSolution, request, "Ajak pelanggan lama kembali dengan pesan ramah dan relevan.", "Jangan membuat klaim hasil atau diskon palsu.")
        }
        if (BusinessChallenge.StockProblems in challenges) {
            ideas += idea("Highlight Stok yang Perlu Bergerak", ContentIdeaType.Promotional, request, "Buat konten untuk produk yang perlu diprioritaskan tanpa fake scarcity.", "Gunakan alasan promo yang jujur.")
        }
        return ideas
    }

    private fun universalIdeas(businessName: String, request: ContentIdeaRequest): List<ContentIdea> = listOf(
        idea("Pertanyaan pelanggan yang paling sering muncul", ContentIdeaType.CustomerFaq, request, "Ubah pertanyaan pelanggan menjadi konten edukasi yang mudah disimpan.", "Pastikan jawaban sesuai kemampuan bisnis."),
        idea("Promo bundle sederhana", ContentIdeaType.BundleCampaign, request, "Gabungkan produk atau layanan yang saling melengkapi.", "Jangan menjanjikan hasil penjualan."),
        idea("Cerita proses harian $businessName", ContentIdeaType.BehindTheScenes, request, "Tampilkan sisi manusiawi dan proses kerja yang rapi.", "Jaga privasi pelanggan dan staf.")
    )

    private fun idea(
        title: String,
        type: ContentIdeaType,
        request: ContentIdeaRequest,
        angle: String,
        safetyNote: String
    ): ContentIdea {
        val platform = request.platform
        val goal = request.goal
        val hook = hookFor(title, platform)
        return ContentIdea(
            title = title,
            category = type.label,
            platformSuggestion = platform.label,
            angle = angle,
            cta = ctaFor(goal, platform),
            type = request.type ?: type,
            platform = platform,
            goal = goal,
            captionDraft = "$hook\n\n$angle\n\n${ctaFor(goal, platform)}",
            hook = hook,
            visualSuggestion = visualFor(type, platform),
            recommendedPostingNote = postingNoteFor(platform),
            relatedBusinessChallenge = null,
            source = ContentGenerationSource.Local,
            safetyNote = safetyNote
        )
    }

    private fun promotionCampaigns(categoryId: String, businessName: String): List<PromotionCampaign> {
        val categoryName = SampleBusinessCategoryRepository.categories.firstOrNull { it.id == categoryId }?.displayName ?: "UMKM"
        return listOf(
            PromotionCampaign(
                title = "Campaign Mingguan $businessName",
                objective = "Membuat pelanggan memahami penawaran utama $categoryName minggu ini.",
                recommendedContentSequence = listOf("Story teaser", "Educational post", "Promotion reminder"),
                expectedOutcome = "May help customers notice and understand the offer."
            ),
            PromotionCampaign(
                title = "Trust Builder Campaign",
                objective = "Membangun kepercayaan melalui proses, FAQ, dan testimoni yang jujur.",
                recommendedContentSequence = listOf("Behind the scenes", "Customer FAQ", "Testimonial format"),
                expectedOutcome = "May help reduce hesitation before customers ask or order."
            )
        )
    }

    private fun hookFor(title: String, platform: ContentPlatform): String = when (platform) {
        ContentPlatform.TikTok, ContentPlatform.InstagramReels -> "Stop scroll: $title"
        ContentPlatform.WhatsAppStory -> "Info singkat hari ini: $title"
        ContentPlatform.MarketplaceDescription -> "Baca sebelum membeli: $title"
        ContentPlatform.OfflinePosterFlyer -> "$title"
        else -> "Pelanggan sering tanya tentang ini: $title"
    }

    private fun ctaFor(goal: ContentGoal, platform: ContentPlatform): String = when (goal) {
        ContentGoal.RepeatOrder -> "Chat kami untuk rekomendasi repeat order yang sesuai."
        ContentGoal.PromotionCampaign -> "Kirim pesan untuk cek detail promo yang tersedia."
        ContentGoal.CustomerTestimonial -> "Tanya admin kalau ingin lihat pengalaman pelanggan lain."
        ContentGoal.ClearSlowMovingStock -> "Cek stok dan pilihan yang masih tersedia."
        ContentGoal.AnnounceBundleOffer -> "Chat untuk cek isi bundle dan harga terbaru."
        else -> if (platform == ContentPlatform.WhatsAppStory) "Balas story ini untuk tanya detail." else "Save konten ini sebelum memilih."
    }

    private fun visualFor(type: ContentIdeaType, platform: ContentPlatform): String = when (type) {
        ContentIdeaType.BehindTheScenes -> "Show real process photos or short clips with clean lighting."
        ContentIdeaType.Testimonial -> "Use a simple quote card or chat screenshot with permission."
        ContentIdeaType.BundleCampaign -> "Place bundled products together with clear labels."
        ContentIdeaType.ProductComparison -> "Use side-by-side product visuals and simple criteria."
        else -> if (platform == ContentPlatform.OfflinePosterFlyer) "Use one clear product image, headline, and contact info." else "Use clear product or service visuals with readable text."
    }

    private fun postingNoteFor(platform: ContentPlatform): String = when (platform) {
        ContentPlatform.WhatsAppBroadcast -> "Send only to customers who expect business updates."
        ContentPlatform.MarketplaceDescription -> "Keep claims factual and match product details."
        ContentPlatform.OfflinePosterFlyer -> "Use large text and one clear CTA."
        else -> "Review caption, price, stock, and claims before posting."
    }
}
