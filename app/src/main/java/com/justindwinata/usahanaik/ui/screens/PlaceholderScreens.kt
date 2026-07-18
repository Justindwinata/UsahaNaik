package com.justindwinata.usahanaik.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.justindwinata.usahanaik.ui.components.MetricCard
import com.justindwinata.usahanaik.ui.components.PillBadge
import com.justindwinata.usahanaik.ui.components.PrimaryActionButton
import com.justindwinata.usahanaik.ui.components.ProgressScoreCard
import com.justindwinata.usahanaik.ui.components.SectionHeader
import com.justindwinata.usahanaik.ui.components.UsahaNaikCard
import com.justindwinata.usahanaik.ui.theme.AppSpacing
import com.justindwinata.usahanaik.ui.theme.BlueSoft
import com.justindwinata.usahanaik.ui.theme.CoralPrimary
import com.justindwinata.usahanaik.ui.theme.CoralSoft
import com.justindwinata.usahanaik.ui.theme.CreamBackground
import com.justindwinata.usahanaik.ui.theme.GreenPositive
import com.justindwinata.usahanaik.ui.theme.GreenSoft
import com.justindwinata.usahanaik.ui.theme.InkMuted
import com.justindwinata.usahanaik.ui.theme.LavenderSoft
import com.justindwinata.usahanaik.ui.theme.RoseSoft
import com.justindwinata.usahanaik.ui.theme.YellowSoft

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit,
    onPreviewDashboardClick: () -> Unit
) {
    ScreenContainer {
        Spacer(modifier = Modifier.height(AppSpacing.xl))
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(CoralSoft),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "UN",
                style = MaterialTheme.typography.titleLarge,
                color = CoralPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        Text(
            text = "UsahaNaik",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Planner pertumbuhan UMKM dengan insight finansial, target mingguan, milestone, dan ide konten berbasis arsitektur AI-ready.",
            style = MaterialTheme.typography.bodyLarge,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        PrimaryActionButton(
            text = "Mulai Setup Bisnis",
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        PrimaryActionButton(
            text = "Lihat Dashboard Preview",
            onClick = onPreviewDashboardClick,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        UsahaNaikCard(containerColor = GreenSoft) {
            PillBadge(text = "Catatan aman", containerColor = CreamBackground, contentColor = GreenPositive)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(
                text = "UsahaNaik membantu perencanaan dan pemantauan. Aplikasi ini tidak menjamin kenaikan profit dan bukan nasihat finansial profesional.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun CategorySelectionScreen(onContinueClick: () -> Unit) {
    ScreenContainer {
        SectionHeader(title = "Pilih Kategori Bisnis")
        Spacer(modifier = Modifier.height(AppSpacing.md))
        repeat(4) { index ->
            UsahaNaikCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = listOf(BlueSoft, GreenSoft, LavenderSoft, YellowSoft)[index]
            ) {
                Text(
                    text = listOf(
                        "Food & Beverage",
                        "Warung / Toko Kelontong",
                        "Skincare & Beauty",
                        "Online Shop / Reseller"
                    )[index],
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Metadata kategori dan rekomendasi goal akan dilengkapi pada foundation domain.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        PrimaryActionButton(
            text = "Lanjut ke Setup",
            onClick = onContinueClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BusinessSetupScreen(onContinueClick: () -> Unit) {
    ScreenContainer {
        SectionHeader(title = "Setup Bisnis")
        Spacer(modifier = Modifier.height(AppSpacing.md))
        listOf(
            "Identitas Bisnis",
            "Baseline Finansial",
            "Data Produk / Jasa",
            "Tantangan Bisnis",
            "Target Bulanan"
        ).forEach { section ->
            UsahaNaikCard(modifier = Modifier.fillMaxWidth()) {
                Text(text = section, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Placeholder form untuk kontrak berikutnya.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        PrimaryActionButton(
            text = "Buka Dashboard",
            onClick = onContinueClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DashboardScreen() {
    ScreenContainer {
        Text(
            text = "Toko Rasa Naik",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Food & Beverage - Minggu ini",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        ProgressScoreCard(
            title = "Business Growth Score",
            score = 72,
            helper = "Fondasi bisnis mulai rapi. Fokus minggu ini: catatan penjualan dan margin produk.",
            containerColor = LavenderSoft
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            MetricCard(
                title = "Revenue",
                value = "Rp18,4jt",
                helper = "Bulan ini",
                modifier = Modifier.weight(1f),
                containerColor = GreenSoft,
                accentColor = GreenPositive
            )
            MetricCard(
                title = "Profit",
                value = "Rp4,8jt",
                helper = "Estimasi",
                modifier = Modifier.weight(1f),
                containerColor = CoralSoft,
                accentColor = CoralPrimary
            )
        }
    }
}

@Composable
fun WeeklyPlanScreen() {
    ScreenContainer {
        SectionHeader(title = "Weekly Plan")
        Spacer(modifier = Modifier.height(AppSpacing.md))
        UsahaNaikCard(containerColor = YellowSoft) {
            Text(text = "Week 1 - Build Business Foundation", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Preview rencana mingguan untuk pencatatan penjualan, margin produk, dan konten edukasi.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
    }
}

@Composable
fun ContentIdeasScreen() {
    ScreenContainer {
        SectionHeader(title = "Content Ideas")
        Spacer(modifier = Modifier.height(AppSpacing.md))
        UsahaNaikCard(containerColor = RoseSoft) {
            PillBadge(text = "Local sample")
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(text = "3 Kesalahan Memilih Produk untuk Pemula", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "AI-assisted ideas preview. Belum memakai API AI real.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
    }
}

@Composable
fun SettingsScreen() {
    ScreenContainer {
        SectionHeader(title = "Profile")
        Spacer(modifier = Modifier.height(AppSpacing.md))
        UsahaNaikCard {
            Text(text = "Pengaturan Bisnis", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Placeholder untuk profil, preferensi, dan pengaturan lokal.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
    }
}

@Composable
private fun ScreenContainer(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.md),
        content = content
    )
}
