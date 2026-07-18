package com.justindwinata.usahanaik.data.repository

import com.justindwinata.usahanaik.domain.model.BusinessCategory

class SampleBusinessCategoryRepository : BusinessCategoryRepository {
    override fun getCategories(): List<BusinessCategory> = categories

    companion object {
        val categories = listOf(
            BusinessCategory(
                id = "food_beverage",
                displayName = "Food & Beverage",
                description = "Usaha makanan, snack, katering, atau minuman rumahan.",
                focusArea = "menu terlaris, margin bahan baku, repeat order, dan promo bundling",
                sampleRecommendedGoal = "meningkatkan repeat order dan menjaga margin menu utama"
            ),
            BusinessCategory(
                id = "warung_kelontong",
                displayName = "Warung / Toko Kelontong",
                description = "Warung harian, toko sembako, dan kebutuhan rumah tangga.",
                focusArea = "stock turnover, daily profit, fast-moving products, dan stok lambat",
                sampleRecommendedGoal = "mengidentifikasi produk terlaris dan mengurangi stok lambat"
            ),
            BusinessCategory(
                id = "skincare_beauty",
                displayName = "Skincare & Beauty",
                description = "Produk perawatan wajah, kosmetik, dan beauty reseller.",
                focusArea = "repeat order, edukasi produk, testimoni, dan paket bundling",
                sampleRecommendedGoal = "meningkatkan repeat purchase dan edukasi produk"
            ),
            BusinessCategory(
                id = "fashion_thrift",
                displayName = "Fashion / Thrift",
                description = "Pakaian, thrift, aksesoris, dan produk fashion harian.",
                focusArea = "display katalog, scarcity campaign, styling content, dan margin item",
                sampleRecommendedGoal = "meningkatkan konversi katalog dan rotasi stok"
            ),
            BusinessCategory(
                id = "laundry",
                displayName = "Laundry",
                description = "Laundry kiloan, satuan, sepatu, atau layanan cuci rumahan.",
                focusArea = "kapasitas harian, layanan repeat, paket langganan, dan kualitas layanan",
                sampleRecommendedGoal = "meningkatkan pelanggan repeat dan efisiensi proses harian"
            ),
            BusinessCategory(
                id = "online_shop_reseller",
                displayName = "Online Shop / Reseller",
                description = "Reseller marketplace, social commerce, dan toko online mandiri.",
                focusArea = "konten produk, katalog, conversion rate, dan kampanye promo",
                sampleRecommendedGoal = "meningkatkan konsistensi konten dan konversi penjualan"
            ),
            BusinessCategory(
                id = "coffee_beverage_shop",
                displayName = "Coffee / Beverage Shop",
                description = "Kedai kopi, minuman kekinian, booth, atau pop-up beverage.",
                focusArea = "menu signature, jam ramai, average order value, dan loyalty customer",
                sampleRecommendedGoal = "mengoptimalkan menu unggulan dan penjualan jam ramai"
            ),
            BusinessCategory(
                id = "service_business",
                displayName = "Service Business",
                description = "Jasa lokal seperti servis, kursus, salon, cleaning, atau konsultasi.",
                focusArea = "paket layanan, jadwal, repeat booking, dan testimoni pelanggan",
                sampleRecommendedGoal = "meningkatkan booking ulang dan paket layanan bernilai tinggi"
            ),
            BusinessCategory(
                id = "digital_service",
                displayName = "Digital Service",
                description = "Freelance, desain, admin media sosial, website, atau jasa digital.",
                focusArea = "portfolio proof, lead pipeline, pricing package, dan client retention",
                sampleRecommendedGoal = "meningkatkan lead berkualitas dan retensi klien"
            ),
            BusinessCategory(
                id = "other_business",
                displayName = "Other Business",
                description = "Kategori lain yang tetap bisa memakai planner bisnis dasar.",
                focusArea = "pencatatan finansial, target mingguan, validasi produk, dan promosi",
                sampleRecommendedGoal = "membangun kebiasaan pencatatan dan target bisnis yang rapi"
            )
        )
    }
}
