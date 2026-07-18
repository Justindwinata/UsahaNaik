package com.justindwinata.usahanaik.data.ai

import com.justindwinata.usahanaik.domain.ai.ContentIdeaProvider
import com.justindwinata.usahanaik.domain.model.BusinessCategory
import com.justindwinata.usahanaik.domain.model.ContentIdea

class LocalContentIdeaProvider : ContentIdeaProvider {
    override fun generateIdeas(
        category: BusinessCategory,
        businessName: String
    ): List<ContentIdea> {
        val baseIdeas = when (category.id) {
            "skincare_beauty" -> skincareIdeas()
            "warung_kelontong" -> warungIdeas()
            else -> generalUmkmIdeas(businessName)
        }

        return baseIdeas + ContentIdea(
            title = "Cerita singkat di balik $businessName",
            category = "Behind the scenes",
            platformSuggestion = "WhatsApp Story",
            angle = "Tunjukkan proses harian agar pelanggan merasa lebih dekat dengan bisnis.",
            cta = "Balas story ini kalau mau rekomendasi produk yang cocok."
        )
    }

    private fun skincareIdeas(): List<ContentIdea> = listOf(
        ContentIdea(
            title = "3 Kesalahan Memilih Skincare untuk Pemula",
            category = "Educational content",
            platformSuggestion = "Instagram Reels",
            angle = "Edukasi pelanggan tentang kesalahan dasar sambil memperkenalkan kategori produk.",
            cta = "Save post ini sebelum beli skincare berikutnya."
        ),
        ContentIdea(
            title = "Paket Hemat Basic Routine",
            category = "Bundle campaign",
            platformSuggestion = "TikTok",
            angle = "Tawarkan bundle sederhana untuk pelanggan yang baru mulai rutinitas skincare.",
            cta = "Chat admin untuk cek paket yang cocok."
        )
    )

    private fun warungIdeas(): List<ContentIdea> = listOf(
        ContentIdea(
            title = "Belanja Mingguan Lebih Irit di Warung Dekat Rumah",
            category = "Promotion content",
            platformSuggestion = "WhatsApp Story",
            angle = "Tonjolkan produk fast-moving dan paket kebutuhan rumah tangga.",
            cta = "Kirim list belanja, nanti kami bantu siapkan."
        ),
        ContentIdea(
            title = "Produk Paling Sering Dicari Minggu Ini",
            category = "Product comparison",
            platformSuggestion = "Instagram Carousel",
            angle = "Bantu pelanggan memilih produk harian berdasarkan kebutuhan dan harga.",
            cta = "Simpan daftar ini untuk belanja berikutnya."
        )
    )

    private fun generalUmkmIdeas(businessName: String): List<ContentIdea> = listOf(
        ContentIdea(
            title = "Kenapa pelanggan memilih $businessName",
            category = "Customer testimonial",
            platformSuggestion = "Instagram Carousel",
            angle = "Gunakan testimoni ringan untuk membangun kepercayaan calon pelanggan.",
            cta = "Kirim pesan untuk tanya produk atau layanan yang paling sesuai."
        ),
        ContentIdea(
            title = "Promo akhir pekan untuk pelanggan repeat",
            category = "Repeat order campaign",
            platformSuggestion = "WhatsApp Story",
            angle = "Ajak pelanggan lama kembali membeli dengan penawaran sederhana.",
            cta = "Klaim promo sebelum Minggu malam."
        )
    )
}
