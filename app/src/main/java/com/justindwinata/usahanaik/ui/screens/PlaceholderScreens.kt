package com.justindwinata.usahanaik.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.justindwinata.usahanaik.data.repository.SampleBusinessCategoryRepository
import com.justindwinata.usahanaik.data.repository.SampleGrowthRepository
import com.justindwinata.usahanaik.domain.model.BusinessTask
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.Milestone
import com.justindwinata.usahanaik.ui.components.MetricCard
import com.justindwinata.usahanaik.ui.components.PillBadge
import com.justindwinata.usahanaik.ui.components.PrimaryActionButton
import com.justindwinata.usahanaik.ui.components.ProgressScoreCard
import com.justindwinata.usahanaik.ui.components.SectionHeader
import com.justindwinata.usahanaik.ui.components.TrendLineChart
import com.justindwinata.usahanaik.ui.components.UsahaNaikCard
import com.justindwinata.usahanaik.ui.theme.AppSpacing
import com.justindwinata.usahanaik.ui.theme.BorderSubtle
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
    val categories = remember { SampleBusinessCategoryRepository().getCategories() }
    var selectedCategoryId by remember { mutableStateOf(categories.first().id) }
    val selectedCategory = categories.first { it.id == selectedCategoryId }

    ScreenContainer {
        SectionHeader(title = "Pilih Kategori Bisnis")
        Text(
            text = "Pilih kategori yang paling dekat dengan usahamu. Metadata ini nanti dipakai untuk goal, dashboard, dan rekomendasi mingguan.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        categories.forEachIndexed { index, category ->
            val isSelected = category.id == selectedCategoryId
            UsahaNaikCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(22.dp))
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) CoralPrimary else BorderSubtle,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp)
                    )
                    .clickable { selectedCategoryId = category.id },
                containerColor = listOf(BlueSoft, GreenSoft, LavenderSoft, YellowSoft)[index % 4]
            ) {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
                Spacer(modifier = Modifier.height(AppSpacing.xs))
                PillBadge(
                    text = if (isSelected) "Dipilih" else "Fokus: ${category.focusArea}",
                    containerColor = if (isSelected) CoralSoft else CreamBackground,
                    contentColor = if (isSelected) CoralPrimary else InkMuted
                )
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        UsahaNaikCard(containerColor = CoralSoft) {
            Text(text = "Sample goal", style = MaterialTheme.typography.titleMedium)
            Text(
                text = selectedCategory.sampleRecommendedGoal,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
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
        Text(
            text = "Preview form ini menunjukkan struktur data yang akan dipersist dengan Room pada kontrak berikutnya.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        SetupPreviewSection(
            title = "Business Identity",
            fields = listOf("Business name", "Business category", "Selling platform"),
            containerColor = BlueSoft
        )
        SetupPreviewSection(
            title = "Financial Baseline",
            fields = listOf("Starting capital", "Monthly revenue", "Monthly expenses", "Estimated profit"),
            containerColor = GreenSoft
        )
        SetupPreviewSection(
            title = "Product / Service Data",
            fields = listOf("Number of products", "Best-selling product", "Highest-margin product"),
            containerColor = LavenderSoft
        )
        SetupPreviewSection(
            title = "Business Challenges",
            fields = listOf(
                "Low sales",
                "Low profit margin",
                "Inconsistent content",
                "Poor financial records",
                "Repeat order problem",
                "Stock problem"
            ),
            containerColor = RoseSoft
        )
        SetupPreviewSection(
            title = "Monthly Goal",
            fields = listOf("Target revenue", "Target profit", "Main focus"),
            containerColor = YellowSoft
        )
        PrimaryActionButton(
            text = "Buka Dashboard",
            onClick = onContinueClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SetupPreviewSection(
    title: String,
    fields: List<String>,
    containerColor: androidx.compose.ui.graphics.Color
) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = containerColor) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        fields.forEach { field ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = field,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                PillBadge(
                    text = "Preview",
                    containerColor = CreamBackground,
                    contentColor = InkMuted
                )
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
    }
    Spacer(modifier = Modifier.height(AppSpacing.sm))
}

@Composable
fun DashboardScreen() {
    val dashboard = remember { SampleGrowthRepository().getDashboardPreview() }

    ScreenContainer {
        DashboardHeader(
            businessName = dashboard.summary.businessName,
            categoryName = dashboard.summary.categoryName,
            weekLabel = dashboard.summary.weekLabel
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        ProgressScoreCard(
            title = "Business Growth Score",
            score = dashboard.healthScore.score,
            helper = dashboard.healthScore.explanation,
            containerColor = LavenderSoft
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            MetricCard(
                title = "Revenue",
                value = dashboard.financialSummary.monthlyRevenue,
                helper = "Bulan ini",
                modifier = Modifier.weight(1f),
                containerColor = GreenSoft,
                accentColor = GreenPositive
            )
            MetricCard(
                title = "Expenses",
                value = dashboard.financialSummary.monthlyExpenses,
                helper = "Bulan ini",
                modifier = Modifier.weight(1f),
                containerColor = BlueSoft,
                accentColor = CoralPrimary
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            MetricCard(
                title = "Profit",
                value = dashboard.financialSummary.estimatedProfit,
                helper = "Estimasi",
                modifier = Modifier.weight(1f),
                containerColor = CoralSoft,
                accentColor = CoralPrimary
            )
            MetricCard(
                title = "Margin",
                value = dashboard.financialSummary.profitMargin,
                helper = "Target sehat",
                modifier = Modifier.weight(1f),
                containerColor = YellowSoft,
                accentColor = GreenPositive
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        SectionHeader(title = "Revenue vs Expense", actionLabel = "7 titik")
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        UsahaNaikCard(modifier = Modifier.fillMaxWidth()) {
            TrendLineChart(
                revenuePoints = dashboard.trend.revenuePoints,
                expensePoints = dashboard.trend.expensePoints
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                PillBadge("Revenue", containerColor = GreenSoft, contentColor = GreenPositive)
                PillBadge("Expense", containerColor = CoralSoft, contentColor = CoralPrimary)
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        SectionHeader(title = "Expense Breakdown")
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        UsahaNaikCard(modifier = Modifier.fillMaxWidth()) {
            dashboard.financialSummary.expenseBreakdown.forEach { item ->
                Text(
                    text = "${item.label} - ${item.percentage}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                LinearProgressIndicator(
                    progress = { item.percentage / 100f },
                    modifier = Modifier.fillMaxWidth(),
                    color = CoralPrimary,
                    trackColor = CreamBackground
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))
            }
            Text(
                text = dashboard.financialSummary.reportSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        SectionHeader(title = "Milestones")
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        dashboard.milestones.forEach { milestone ->
            MilestoneCard(milestone = milestone)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        SectionHeader(title = "Tasks & Challenges")
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        dashboard.tasks.forEach { task ->
            TaskChecklistRow(task = task)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        SectionHeader(title = "Product Performance")
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        dashboard.productPerformance.forEach { product ->
            UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = BlueSoft) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${product.signal} - ${product.marginLabel}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        SectionHeader(title = "Next Actions")
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        dashboard.recommendations.forEach { recommendation ->
            UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = GreenSoft) {
                Text(text = recommendation, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        SectionHeader(title = "Content Ideas Preview")
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        dashboard.contentIdeas.take(2).forEach { idea ->
            ContentIdeaPreviewCard(idea = idea)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
    }
}

@Composable
fun WeeklyPlanScreen() {
    val weeklyPlan = remember { SampleGrowthRepository().getWeeklyPlanPreview() }

    ScreenContainer {
        SectionHeader(title = "Weekly Plan")
        Spacer(modifier = Modifier.height(AppSpacing.md))
        UsahaNaikCard(containerColor = YellowSoft) {
            Text(text = weeklyPlan.title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = weeklyPlan.target,
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            LinearProgressIndicator(
                progress = { weeklyPlan.milestoneProgress },
                modifier = Modifier.fillMaxWidth(),
                color = GreenPositive,
                trackColor = CreamBackground
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        SectionHeader(title = "Priority Actions")
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        weeklyPlan.priorityActions.forEach { action ->
            UsahaNaikCard(modifier = Modifier.fillMaxWidth()) {
                Text(text = action, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        SectionHeader(title = "Daily Tasks")
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        weeklyPlan.dailyTasks.forEach { task ->
            TaskChecklistRow(task = task)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = CoralSoft) {
            PillBadge(text = "Challenge", containerColor = CreamBackground, contentColor = CoralPrimary)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(text = weeklyPlan.challenge, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun ContentIdeasScreen() {
    val ideas = remember { SampleGrowthRepository().getContentIdeaPreview() }

    ScreenContainer {
        SectionHeader(title = "Content Ideas", actionLabel = "Local sample")
        Text(
            text = "AI-assisted ideas preview. UN-0001 memakai provider lokal deterministik, belum memanggil API AI real.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        ideas.forEach { idea ->
            ContentIdeaPreviewCard(idea = idea)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
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

@Composable
private fun DashboardHeader(
    businessName: String,
    categoryName: String,
    weekLabel: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = businessName, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "$categoryName - $weekLabel",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(CoralSoft),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "UN", color = CoralPrimary, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun MilestoneCard(milestone: Milestone) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = LavenderSoft) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = milestone.title, style = MaterialTheme.typography.titleMedium)
                Text(text = milestone.status, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
            }
            Text(
                text = "${(milestone.progress * 100).toInt()}%",
                style = MaterialTheme.typography.titleMedium,
                color = CoralPrimary
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        LinearProgressIndicator(
            progress = { milestone.progress },
            modifier = Modifier.fillMaxWidth(),
            color = CoralPrimary,
            trackColor = CreamBackground
        )
    }
}

@Composable
private fun TaskChecklistRow(task: BusinessTask) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = null
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                Text(text = task.description, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
            }
        }
    }
}

@Composable
private fun ContentIdeaPreviewCard(idea: ContentIdea) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = RoseSoft) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            PillBadge(text = idea.category, containerColor = CreamBackground, contentColor = CoralPrimary)
            PillBadge(text = idea.platformSuggestion, containerColor = CreamBackground, contentColor = InkMuted)
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = idea.title, style = MaterialTheme.typography.titleLarge)
        Text(text = idea.angle, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "CTA: ${idea.cta}", style = MaterialTheme.typography.labelLarge, color = CoralPrimary)
    }
}
