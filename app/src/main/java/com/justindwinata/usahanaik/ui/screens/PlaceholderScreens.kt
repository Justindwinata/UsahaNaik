package com.justindwinata.usahanaik.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.justindwinata.usahanaik.data.repository.SampleBusinessCategoryRepository
import com.justindwinata.usahanaik.data.repository.SampleGrowthRepository
import com.justindwinata.usahanaik.domain.content.ContentPlannerDashboardSummary
import com.justindwinata.usahanaik.domain.content.ContentPlannerDashboardSummaryMapper
import com.justindwinata.usahanaik.domain.finance.FinancialDashboardMetrics
import com.justindwinata.usahanaik.domain.finance.FinancialDashboardMetricsMapper
import com.justindwinata.usahanaik.domain.model.ContentCalendarSummaryCalculator
import com.justindwinata.usahanaik.domain.progress.DashboardContinuitySummary
import com.justindwinata.usahanaik.domain.progress.DashboardContinuitySummaryMapper
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessDiagnosis
import com.justindwinata.usahanaik.domain.model.BusinessInsight
import com.justindwinata.usahanaik.domain.model.BusinessMilestone
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessRiskSignal
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessStage
import com.justindwinata.usahanaik.domain.model.BusinessTask
import com.justindwinata.usahanaik.domain.model.ContentGenerationSource
import com.justindwinata.usahanaik.domain.model.ContentCalendarSchedule
import com.justindwinata.usahanaik.domain.model.ContentCalendarStatus
import com.justindwinata.usahanaik.domain.model.ContentGoal
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.ContentIdeaFilter
import com.justindwinata.usahanaik.domain.model.ContentIdeaStatus
import com.justindwinata.usahanaik.domain.model.ContentIdeaType
import com.justindwinata.usahanaik.domain.model.ContentPlatform
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.Milestone
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.PromotionCampaign
import com.justindwinata.usahanaik.domain.model.SellingChannel
import com.justindwinata.usahanaik.domain.model.StockIssue
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.model.InsightSeverity
import com.justindwinata.usahanaik.domain.model.WeeklyGrowthPlan
import com.justindwinata.usahanaik.domain.model.WeeklyProgressHistorySummary
import com.justindwinata.usahanaik.domain.model.WeeklyProgressSnapshot
import com.justindwinata.usahanaik.domain.model.WeeklyRetrospective
import com.justindwinata.usahanaik.domain.model.WeeklyTask
import com.justindwinata.usahanaik.domain.model.WeeklyTaskStatus
import com.justindwinata.usahanaik.domain.setup.BusinessCategorySetupHints
import com.justindwinata.usahanaik.domain.setup.BusinessSetupCalculator
import com.justindwinata.usahanaik.domain.setup.BusinessSetupField
import com.justindwinata.usahanaik.domain.weekly.WeeklyPlanDashboardSummary
import com.justindwinata.usahanaik.domain.weekly.WeeklyPlanDashboardSummaryMapper
import com.justindwinata.usahanaik.ui.components.MetricCard
import com.justindwinata.usahanaik.ui.components.PillBadge
import com.justindwinata.usahanaik.ui.components.PrimaryActionButton
import com.justindwinata.usahanaik.ui.components.ProgressScoreCard
import com.justindwinata.usahanaik.ui.components.SectionHeader
import com.justindwinata.usahanaik.ui.components.TrendLineChart
import com.justindwinata.usahanaik.ui.components.UsahaNaikCard
import com.justindwinata.usahanaik.ui.content.ContentPlannerUiState
import com.justindwinata.usahanaik.ui.content.ContentPlannerViewModel
import com.justindwinata.usahanaik.ui.dashboard.DashboardInsightsUiState
import com.justindwinata.usahanaik.ui.dashboard.DashboardInsightsViewModel
import com.justindwinata.usahanaik.ui.finance.FinancialEntryUiState
import com.justindwinata.usahanaik.ui.finance.FinancialEntryViewModel
import com.justindwinata.usahanaik.ui.setup.BusinessSetupUiState
import com.justindwinata.usahanaik.ui.setup.BusinessSetupViewModel
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
import com.justindwinata.usahanaik.ui.weekly.WeeklyPlanUiState
import com.justindwinata.usahanaik.ui.weekly.WeeklyPlanViewModel
import com.justindwinata.usahanaik.ui.content.ContentCalendarUiState
import com.justindwinata.usahanaik.ui.content.ContentCalendarViewModel
import com.justindwinata.usahanaik.ui.progress.WeeklyRetrospectiveViewModel

@Composable
fun WelcomeScreen(
    savedProfile: BusinessProfile? = null,
    onStartClick: () -> Unit,
    onResumeSavedProfileClick: () -> Unit = {},
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
        if (savedProfile != null) {
            UsahaNaikCard(containerColor = BlueSoft) {
                PillBadge(text = "Saved locally", containerColor = CreamBackground, contentColor = CoralPrimary)
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(
                    text = savedProfile.draft.businessName,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Your business profile is saved locally on this device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                PrimaryActionButton(
                    text = "Resume Saved Profile",
                    onClick = onResumeSavedProfileClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(AppSpacing.lg))
        }
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
fun CategorySelectionScreen(onContinueClick: (String) -> Unit) {
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
                    .clip(RoundedCornerShape(22.dp))
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) CoralPrimary else BorderSubtle,
                        shape = RoundedCornerShape(22.dp)
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
            onClick = { onContinueClick(selectedCategoryId) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BusinessSetupScreen(
    viewModel: BusinessSetupViewModel,
    onContinueClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val categories = remember { SampleBusinessCategoryRepository().getCategories() }
    val selectedCategory = categories.firstOrNull { it.id == uiState.draft.categoryId } ?: categories.first()
    val guidance = BusinessCategorySetupHints.guidanceFor(selectedCategory)

    ScreenContainer {
        SectionHeader(title = "Setup Bisnis", actionLabel = "${uiState.completedSectionCount}/${uiState.totalSectionCount}")
        Text(
            text = "Lengkapi draft setup agar dashboard preview bisa terasa lebih personal. Data UN-0002 masih disimpan di memory ViewModel.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        UsahaNaikCard(containerColor = LavenderSoft) {
            PillBadge(text = selectedCategory.displayName, containerColor = CreamBackground, contentColor = CoralPrimary)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(text = "Category setup hints", style = MaterialTheme.typography.titleMedium)
            Text(text = guidance.focusArea, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            guidance.setupHints.forEach { hint ->
                Text(text = "- $hint", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(
                text = "Recommended focus: ${guidance.recommendedMonthlyFocus.label}",
                style = MaterialTheme.typography.labelLarge,
                color = CoralPrimary
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        UsahaNaikCard(containerColor = GreenSoft) {
            Text(text = "Setup progress", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            LinearProgressIndicator(
                progress = { uiState.setupProgress },
                modifier = Modifier.fillMaxWidth(),
                color = GreenPositive,
                trackColor = CreamBackground
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            SectionCompletionRow("Identity", uiState.isIdentityComplete)
            SectionCompletionRow("Financial", uiState.isFinancialComplete)
            SectionCompletionRow("Product", uiState.isProductComplete)
            SectionCompletionRow("Challenges", uiState.isChallengesComplete)
            SectionCompletionRow("Goals", uiState.isGoalsComplete)
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        BusinessIdentityForm(
            uiState = uiState,
            selectedCategoryName = selectedCategory.displayName,
            onBusinessNameChange = viewModel::updateBusinessName,
            onOwnerNameChange = viewModel::updateOwnerName,
            onSellingChannelChange = viewModel::updateSellingChannel,
            onBusinessLocationChange = viewModel::updateBusinessLocation,
            onBusinessStageChange = viewModel::updateBusinessStage
        )
        FinancialBaselineForm(
            uiState = uiState,
            onStartingCapitalChange = viewModel::updateStartingCapital,
            onMonthlyRevenueChange = viewModel::updateMonthlyRevenue,
            onMonthlyExpensesChange = viewModel::updateMonthlyExpenses,
            onEstimatedProfitChange = viewModel::updateEstimatedMonthlyProfit,
            onDailyTransactionsChange = viewModel::updateAverageDailyTransactions,
            onAverageTransactionValueChange = viewModel::updateAverageTransactionValue
        )
        ProductDataForm(
            uiState = uiState,
            onProductCountChange = viewModel::updateProductCount,
            onBestSellingProductChange = viewModel::updateBestSellingProduct,
            onHighestMarginProductChange = viewModel::updateHighestMarginProduct,
            onCostDriverChange = viewModel::updateMainCostDriver,
            onStockIssueChange = viewModel::updateStockIssue
        )
        ChallengesForm(
            uiState = uiState,
            onChallengeToggle = viewModel::toggleChallenge
        )
        MonthlyGoalsForm(
            uiState = uiState,
            onTargetRevenueChange = viewModel::updateTargetMonthlyRevenue,
            onTargetProfitChange = viewModel::updateTargetMonthlyProfit,
            onMainFocusChange = viewModel::updateMainFocus,
            onAvailableTimeChange = viewModel::updateAvailableTime
        )
        Button(
            onClick = { viewModel.requestReview() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (uiState.isValid) "Review Setup" else "Check Setup")
        }
        if (uiState.hasAttemptedReview && !uiState.isValid) {
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(
                text = "Beberapa data penting masih perlu dilengkapi sebelum review.",
                style = MaterialTheme.typography.bodyMedium,
                color = CoralPrimary
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        OutlinedButton(
            onClick = viewModel::resetDraft,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reset Draft")
        }
        if (uiState.isReviewVisible) {
            Spacer(modifier = Modifier.height(AppSpacing.lg))
            SetupReviewSection(
                uiState = uiState,
                draft = uiState.draft,
                categoryName = selectedCategory.displayName,
                onSaveProfile = { viewModel.saveCompletedProfile(onContinueClick) }
            )
        }
    }
}

@Composable
private fun BusinessIdentityForm(
    uiState: BusinessSetupUiState,
    selectedCategoryName: String,
    onBusinessNameChange: (String) -> Unit,
    onOwnerNameChange: (String) -> Unit,
    onSellingChannelChange: (SellingChannel) -> Unit,
    onBusinessLocationChange: (String) -> Unit,
    onBusinessStageChange: (BusinessStage) -> Unit
) {
    SetupSectionCard(title = "1. Business Identity", isComplete = uiState.isIdentityComplete, containerColor = BlueSoft) {
        SetupTextField(
            label = "Business name",
            value = uiState.draft.businessName,
            onValueChange = onBusinessNameChange,
            error = uiState.visibleError(BusinessSetupField.BusinessName)
        )
        SetupTextField(
            label = "Owner name (optional)",
            value = uiState.draft.ownerName,
            onValueChange = onOwnerNameChange
        )
        SetupReadOnlyValue(label = "Business category", value = selectedCategoryName)
        ChoiceGroup(
            title = "Selling platform/channel",
            options = SellingChannel.entries,
            selected = uiState.draft.sellingChannel,
            label = { it.label },
            onSelected = onSellingChannelChange
        )
        SetupTextField(
            label = "Business location (optional)",
            value = uiState.draft.businessLocation,
            onValueChange = onBusinessLocationChange
        )
        ChoiceGroup(
            title = "Business stage",
            options = BusinessStage.entries,
            selected = uiState.draft.businessStage,
            label = { it.label },
            onSelected = onBusinessStageChange
        )
    }
}

@Composable
private fun FinancialBaselineForm(
    uiState: BusinessSetupUiState,
    onStartingCapitalChange: (String) -> Unit,
    onMonthlyRevenueChange: (String) -> Unit,
    onMonthlyExpensesChange: (String) -> Unit,
    onEstimatedProfitChange: (String) -> Unit,
    onDailyTransactionsChange: (String) -> Unit,
    onAverageTransactionValueChange: (String) -> Unit
) {
    SetupSectionCard(title = "2. Financial Baseline", isComplete = uiState.isFinancialComplete, containerColor = GreenSoft) {
        MoneyTextField("Starting capital", uiState.draft.startingCapital, onStartingCapitalChange)
        MoneyTextField(
            label = "Monthly revenue",
            value = uiState.draft.monthlyRevenue,
            onValueChange = onMonthlyRevenueChange,
            error = uiState.visibleError(BusinessSetupField.MonthlyRevenue)
        )
        MoneyTextField(
            label = "Monthly expenses",
            value = uiState.draft.monthlyExpenses,
            onValueChange = onMonthlyExpensesChange,
            error = uiState.visibleError(BusinessSetupField.MonthlyExpenses)
        )
        MoneyTextField(
            label = "Estimated monthly profit",
            value = uiState.draft.estimatedMonthlyProfit,
            onValueChange = onEstimatedProfitChange,
            error = uiState.visibleError(BusinessSetupField.EstimatedMonthlyProfit)
        )
        NumberTextField(
            label = "Average daily transactions",
            value = uiState.draft.averageDailyTransactions,
            onValueChange = onDailyTransactionsChange
        )
        MoneyTextField(
            label = "Average transaction value (optional)",
            value = uiState.draft.averageTransactionValue,
            onValueChange = onAverageTransactionValueChange
        )
    }
}

@Composable
private fun ProductDataForm(
    uiState: BusinessSetupUiState,
    onProductCountChange: (String) -> Unit,
    onBestSellingProductChange: (String) -> Unit,
    onHighestMarginProductChange: (String) -> Unit,
    onCostDriverChange: (CostDriver) -> Unit,
    onStockIssueChange: (StockIssue) -> Unit
) {
    SetupSectionCard(title = "3. Product / Service Data", isComplete = uiState.isProductComplete, containerColor = LavenderSoft) {
        NumberTextField(
            label = "Number of products/services",
            value = uiState.draft.productCount,
            onValueChange = onProductCountChange,
            error = uiState.visibleError(BusinessSetupField.ProductCount)
        )
        SetupTextField(
            label = "Best-selling product/service",
            value = uiState.draft.bestSellingProduct,
            onValueChange = onBestSellingProductChange
        )
        SetupTextField(
            label = "Highest-margin product/service (optional)",
            value = uiState.draft.highestMarginProduct,
            onValueChange = onHighestMarginProductChange
        )
        ChoiceGroup(
            title = "Main cost driver",
            options = CostDriver.entries,
            selected = uiState.draft.mainCostDriver,
            label = { it.label },
            onSelected = onCostDriverChange
        )
        ChoiceGroup(
            title = "Stock issue indicator",
            options = StockIssue.entries,
            selected = uiState.draft.stockIssue,
            label = { it.label },
            onSelected = onStockIssueChange
        )
    }
}

@Composable
private fun ChallengesForm(
    uiState: BusinessSetupUiState,
    onChallengeToggle: (BusinessChallenge) -> Unit
) {
    SetupSectionCard(title = "4. Business Challenges", isComplete = uiState.isChallengesComplete, containerColor = RoseSoft) {
        Text(
            text = "Pilih satu atau lebih tantangan yang paling terasa saat ini.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        ChipFlow {
            BusinessChallenge.entries.forEach { challenge ->
                FilterChip(
                    selected = challenge in uiState.draft.challenges,
                    onClick = { onChallengeToggle(challenge) },
                    label = { Text(challenge.label) }
                )
            }
        }
        FieldError(uiState.visibleError(BusinessSetupField.Challenges))
    }
}

@Composable
private fun MonthlyGoalsForm(
    uiState: BusinessSetupUiState,
    onTargetRevenueChange: (String) -> Unit,
    onTargetProfitChange: (String) -> Unit,
    onMainFocusChange: (MonthlyFocus) -> Unit,
    onAvailableTimeChange: (AvailableTime) -> Unit
) {
    SetupSectionCard(title = "5. Monthly Goals", isComplete = uiState.isGoalsComplete, containerColor = YellowSoft) {
        MoneyTextField(
            label = "Target monthly revenue",
            value = uiState.draft.targetMonthlyRevenue,
            onValueChange = onTargetRevenueChange,
            error = uiState.visibleError(BusinessSetupField.TargetMonthlyRevenue)
        )
        MoneyTextField(
            label = "Target monthly profit",
            value = uiState.draft.targetMonthlyProfit,
            onValueChange = onTargetProfitChange,
            error = uiState.visibleError(BusinessSetupField.TargetMonthlyProfit)
        )
        ChoiceGroup(
            title = "Main focus this month",
            options = MonthlyFocus.entries,
            selected = uiState.draft.mainFocus,
            label = { it.label },
            onSelected = onMainFocusChange
        )
        FieldError(uiState.visibleError(BusinessSetupField.MainFocus))
        ChoiceGroup(
            title = "Available time per week",
            options = AvailableTime.entries,
            selected = uiState.draft.availableTime,
            label = { it.label },
            onSelected = onAvailableTimeChange
        )
        FieldError(uiState.visibleError(BusinessSetupField.AvailableTime))
    }
}

@Composable
private fun SetupSectionCard(
    title: String,
    isComplete: Boolean,
    containerColor: androidx.compose.ui.graphics.Color,
    content: @Composable ColumnScope.() -> Unit
) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = containerColor) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            PillBadge(
                text = if (isComplete) "Complete" else "Draft",
                containerColor = CreamBackground,
                contentColor = if (isComplete) GreenPositive else InkMuted
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        content()
    }
    Spacer(modifier = Modifier.height(AppSpacing.sm))
}

@Composable
private fun SectionCompletionRow(label: String, completed: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        PillBadge(
            text = if (completed) "Complete" else "Draft",
            containerColor = CreamBackground,
            contentColor = if (completed) GreenPositive else InkMuted
        )
    }
    Spacer(modifier = Modifier.height(AppSpacing.xs))
}

@Composable
private fun SetupTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        isError = error != null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
    FieldError(error)
    Spacer(modifier = Modifier.height(AppSpacing.sm))
}

@Composable
private fun MoneyTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null
) {
    SetupTextField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        error = error,
        keyboardType = KeyboardType.Number
    )
}

@Composable
private fun NumberTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null
) {
    SetupTextField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        error = error,
        keyboardType = KeyboardType.Number
    )
}

@Composable
private fun SetupReadOnlyValue(label: String, value: String) {
    Text(text = label, style = MaterialTheme.typography.labelMedium, color = InkMuted)
    Text(text = value, style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(AppSpacing.sm))
}

@Composable
private fun <T> ChoiceGroup(
    title: String,
    options: List<T>,
    selected: T?,
    label: (T) -> String,
    onSelected: (T) -> Unit
) {
    Text(text = title, style = MaterialTheme.typography.labelLarge)
    Spacer(modifier = Modifier.height(AppSpacing.xs))
    ChipFlow {
        options.forEach { option ->
            FilterChip(
                selected = option == selected,
                onClick = { onSelected(option) },
                label = { Text(label(option)) }
            )
        }
    }
    Spacer(modifier = Modifier.height(AppSpacing.sm))
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ChipFlow(content: @Composable () -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
    ) {
        content()
    }
}

@Composable
private fun FieldError(error: String?) {
    if (error != null) {
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = CoralPrimary
        )
    }
}

@Composable
private fun SetupReviewSection(
    uiState: BusinessSetupUiState,
    draft: BusinessSetupDraft,
    categoryName: String,
    onSaveProfile: () -> Unit
) {
    val profitMargin = BusinessSetupCalculator.profitMarginPercent(draft)?.let { "$it%" } ?: "-"
    val revenueGap = BusinessSetupCalculator.formatRupiah(BusinessSetupCalculator.revenueTargetGap(draft))
    val profitGap = BusinessSetupCalculator.formatRupiah(BusinessSetupCalculator.profitTargetGap(draft))

    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = GreenSoft) {
        PillBadge(text = "Review ready", containerColor = CreamBackground, contentColor = GreenPositive)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "Setup Review", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Dashboard akan dibuat dari draft ini. Angka adalah preview perencanaan, bukan nasihat finansial profesional.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        ReviewRow("Business", draft.businessName)
        ReviewRow("Category", categoryName)
        ReviewRow("Monthly revenue", draft.monthlyRevenue)
        ReviewRow("Monthly expenses", draft.monthlyExpenses)
        ReviewRow("Estimated profit", draft.estimatedMonthlyProfit)
        ReviewRow("Target revenue", draft.targetMonthlyRevenue)
        ReviewRow("Target profit", draft.targetMonthlyProfit)
        ReviewRow("Profit margin", profitMargin)
        ReviewRow("Revenue gap", revenueGap)
        ReviewRow("Profit gap", profitGap)
        ReviewRow("Main focus", draft.mainFocus?.label ?: "-")
        ReviewRow("Weekly time", draft.availableTime?.label ?: "-")
        ReviewRow("Challenges", draft.challenges.joinToString { it.label })
        Spacer(modifier = Modifier.height(AppSpacing.md))
        uiState.saveSuccessMessage?.let { message ->
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = GreenPositive)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        uiState.saveErrorMessage?.let { message ->
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = CoralPrimary)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        PrimaryActionButton(
            text = if (uiState.isSavingProfile) "Saving Profile..." else "Save Business Profile",
            onClick = {
                if (!uiState.isSavingProfile) {
                    onSaveProfile()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ReviewRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted,
            modifier = Modifier.weight(0.42f)
        )
        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.58f)
        )
    }
    Spacer(modifier = Modifier.height(AppSpacing.xs))
}

@Composable
fun DashboardScreen(
    setupDraft: BusinessSetupDraft? = null,
    financialEntryViewModel: FinancialEntryViewModel,
    dashboardInsightsViewModel: DashboardInsightsViewModel,
    weeklyPlanViewModel: WeeklyPlanViewModel,
    contentPlannerViewModel: ContentPlannerViewModel,
    contentCalendarViewModel: ContentCalendarViewModel,
    weeklyRetrospectiveViewModel: WeeklyRetrospectiveViewModel,
    onOpenWeeklyPlan: () -> Unit,
    onOpenContentPlanner: () -> Unit,
    onOpenRetrospective: () -> Unit
) {
    val dashboard = remember(setupDraft) { SampleGrowthRepository().getDashboardPreview(setupDraft) }
    val financialState by financialEntryViewModel.uiState.collectAsState()
    val insightsState by dashboardInsightsViewModel.uiState.collectAsState()
    val weeklyPlanState by weeklyPlanViewModel.uiState.collectAsState()
    val contentPlannerState by contentPlannerViewModel.uiState.collectAsState()
    val contentCalendarState by contentCalendarViewModel.uiState.collectAsState()
    val retrospectiveState by weeklyRetrospectiveViewModel.uiState.collectAsState()
    val financialMetrics = remember(financialState.summary, dashboard) {
        FinancialDashboardMetricsMapper.from(
            summary = financialState.summary,
            fallbackSummary = dashboard.financialSummary,
            fallbackTrend = dashboard.trend
        )
    }
    val weeklySummary = remember(weeklyPlanState.activePlan) {
        WeeklyPlanDashboardSummaryMapper.from(weeklyPlanState.activePlan)
    }
    val contentSummary = remember(contentPlannerState.savedIdeas) {
        ContentPlannerDashboardSummaryMapper.from(contentPlannerState.savedIdeas)
    }
    val calendarSummary = remember(contentCalendarState.schedules) {
        ContentCalendarSummaryCalculator.summarize(contentCalendarState.schedules)
    }
    val continuitySummary = remember(
        weeklySummary,
        calendarSummary,
        retrospectiveState.latestRetrospective,
        retrospectiveState.progressHistorySummary
    ) {
        DashboardContinuitySummaryMapper.from(
            weeklyPlanSummary = weeklySummary,
            contentCalendarSummary = calendarSummary,
            latestRetrospective = retrospectiveState.latestRetrospective,
            progressHistorySummary = retrospectiveState.progressHistorySummary
        )
    }

    LaunchedEffect(setupDraft?.targetMonthlyRevenue, setupDraft?.targetMonthlyProfit) {
        financialEntryViewModel.refresh(
            targetMonthlyRevenue = setupDraft?.targetMonthlyRevenue,
            targetMonthlyProfit = setupDraft?.targetMonthlyProfit
        )
    }
    LaunchedEffect(
        financialState.entries.size,
        financialState.summary.totalIncome,
        financialState.summary.totalExpenses,
        financialState.summary.estimatedProfit
    ) {
        dashboardInsightsViewModel.refresh()
    }

    ScreenContainer {
        DashboardHeader(
            businessName = dashboard.summary.businessName,
            categoryName = dashboard.summary.categoryName,
            weekLabel = dashboard.summary.weekLabel
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        ProgressScoreCard(
            title = "Business Health Score",
            score = insightsState.diagnosis?.healthScore?.score ?: dashboard.healthScore.score,
            helper = insightsState.diagnosis?.healthScore?.explanation ?: dashboard.healthScore.explanation,
            containerColor = LavenderSoft
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        DashboardInsightPanel(uiState = insightsState)
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        WeeklyPlanDashboardSection(
            summary = weeklySummary,
            onOpenWeeklyPlan = onOpenWeeklyPlan
        )
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        ContentPlannerDashboardSection(
            summary = contentSummary,
            onOpenContentPlanner = onOpenContentPlanner
        )
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        DashboardContinuitySection(
            summary = continuitySummary,
            onOpenWeeklyPlan = onOpenWeeklyPlan,
            onOpenContentPlanner = onOpenContentPlanner,
            onOpenRetrospective = onOpenRetrospective
        )
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            MetricCard(
                title = "Revenue",
                value = financialMetrics.monthlyRevenue,
                helper = "Bulan ini",
                modifier = Modifier.weight(1f),
                containerColor = GreenSoft,
                accentColor = GreenPositive
            )
            MetricCard(
                title = "Expenses",
                value = financialMetrics.monthlyExpenses,
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
                value = financialMetrics.estimatedProfit,
                helper = "Estimasi",
                modifier = Modifier.weight(1f),
                containerColor = CoralSoft,
                accentColor = CoralPrimary
            )
            MetricCard(
                title = "Margin",
                value = financialMetrics.profitMargin,
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
                revenuePoints = financialMetrics.revenueTrendPoints,
                expensePoints = financialMetrics.expenseTrendPoints
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                PillBadge("Revenue", containerColor = GreenSoft, contentColor = GreenPositive)
                PillBadge("Expense", containerColor = CoralSoft, contentColor = CoralPrimary)
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(
                text = financialMetrics.reportSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        FinancialDashboardMetricsSection(metrics = financialMetrics)
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
        FinancialTrackingSection(
            uiState = financialState,
            onTypeChange = financialEntryViewModel::updateType,
            onTitleChange = financialEntryViewModel::updateTitle,
            onAmountChange = financialEntryViewModel::updateAmount,
            onCategoryChange = financialEntryViewModel::updateCategory,
            onDateChange = financialEntryViewModel::updateDate,
            onNoteChange = financialEntryViewModel::updateNote,
            onSave = financialEntryViewModel::saveEntry,
            onRequestDelete = financialEntryViewModel::requestDeleteEntry,
            onConfirmDelete = financialEntryViewModel::confirmDeleteEntry,
            onCancelDelete = financialEntryViewModel::cancelDelete
        )
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
private fun DashboardInsightPanel(uiState: DashboardInsightsUiState) {
    val diagnosis = uiState.diagnosis
    SectionHeader(title = "Business Diagnosis", actionLabel = "Rule-based")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    when {
        uiState.isLoading -> {
            UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = BlueSoft) {
                Text(text = "Generating insights...", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "UsahaNaik is reviewing your saved profile and local financial records.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
            }
        }
        uiState.errorMessage != null -> {
            UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = RoseSoft) {
                Text(text = "Insight generation needs attention", style = MaterialTheme.typography.titleMedium)
                Text(text = uiState.errorMessage, style = MaterialTheme.typography.bodyMedium, color = CoralPrimary)
            }
        }
        diagnosis != null -> {
            DiagnosisScoreCard(diagnosis = diagnosis)
            Spacer(modifier = Modifier.height(AppSpacing.md))
            InsightSummaryCards(diagnosis = diagnosis)
            Spacer(modifier = Modifier.height(AppSpacing.md))
            uiState.emptyStateMessage?.let { message ->
                UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = YellowSoft) {
                    Text(text = "Improve insight accuracy", style = MaterialTheme.typography.titleMedium)
                    Text(text = message, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
                }
                Spacer(modifier = Modifier.height(AppSpacing.md))
            }
            BusinessInsightsSection(insights = diagnosis.insights)
            Spacer(modifier = Modifier.height(AppSpacing.md))
            PriorityActionsSection(actions = diagnosis.priorityActions)
            if (diagnosis.riskSignals.isNotEmpty()) {
                Spacer(modifier = Modifier.height(AppSpacing.md))
                RiskSignalsSection(risks = diagnosis.riskSignals)
            }
        }
    }
}

@Composable
private fun DiagnosisScoreCard(diagnosis: BusinessDiagnosis) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = LavenderSoft) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                PillBadge(
                    text = diagnosis.healthScore.statusLabel,
                    containerColor = CreamBackground,
                    contentColor = CoralPrimary
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(text = "Rule-based business health", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Generated from saved profile and local financial records. Suggestions are planning guidance, not guaranteed outcomes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
            }
            Text(
                text = "${diagnosis.healthScore.score}/100",
                style = MaterialTheme.typography.headlineMedium,
                color = CoralPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        diagnosis.breakdown.forEach { item ->
            Text(text = item.component.label, style = MaterialTheme.typography.labelLarge)
            LinearProgressIndicator(
                progress = { item.progress },
                modifier = Modifier.fillMaxWidth(),
                color = GreenPositive,
                trackColor = CreamBackground
            )
            Text(
                text = "${item.score}/${item.maxScore} - ${item.explanation}",
                style = MaterialTheme.typography.bodySmall,
                color = InkMuted
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
    }
}

@Composable
private fun InsightSummaryCards(diagnosis: BusinessDiagnosis) {
    Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
        MetricCard(
            title = "Finance",
            value = diagnosis.summary.financeInsightCount.toString(),
            helper = "Insights",
            modifier = Modifier.weight(1f),
            containerColor = GreenSoft,
            accentColor = GreenPositive
        )
        MetricCard(
            title = "Warnings",
            value = diagnosis.summary.warningCount.toString(),
            helper = "Attention",
            modifier = Modifier.weight(1f),
            containerColor = RoseSoft,
            accentColor = CoralPrimary
        )
    }
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
        MetricCard(
            title = "Actions",
            value = diagnosis.summary.priorityActionCount.toString(),
            helper = "Next steps",
            modifier = Modifier.weight(1f),
            containerColor = BlueSoft,
            accentColor = CoralPrimary
        )
        MetricCard(
            title = "Goal",
            value = diagnosis.summary.goalProgressStatus,
            helper = "Progress",
            modifier = Modifier.weight(1f),
            containerColor = YellowSoft,
            accentColor = GreenPositive
        )
    }
}

@Composable
private fun BusinessInsightsSection(insights: List<BusinessInsight>) {
    SectionHeader(title = "Business Insights")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    insights.take(5).forEach { insight ->
        UsahaNaikCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = insightContainerColor(insight.severity)
        ) {
            InsightSeverityBadge(severity = insight.severity)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(text = insight.title, style = MaterialTheme.typography.titleMedium)
            Text(text = insight.category.label, style = MaterialTheme.typography.labelMedium, color = InkMuted)
            Text(text = insight.message, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
    }
}

@Composable
private fun PriorityActionsSection(actions: List<com.justindwinata.usahanaik.domain.model.PriorityAction>) {
    SectionHeader(title = "Priority Actions")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    actions.forEach { action ->
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = BlueSoft) {
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                PillBadge(text = action.difficulty.label, containerColor = CreamBackground, contentColor = CoralPrimary)
                PillBadge(text = action.estimatedTime.label, containerColor = CreamBackground, contentColor = GreenPositive)
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(text = action.title, style = MaterialTheme.typography.titleMedium)
            Text(text = action.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(AppSpacing.xs))
            Text(text = "Reason: ${action.reason}", style = MaterialTheme.typography.bodySmall, color = InkMuted)
            Text(
                text = "Expected outcome: ${action.expectedOutcome}",
                style = MaterialTheme.typography.bodySmall,
                color = InkMuted
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
    }
}

@Composable
private fun RiskSignalsSection(risks: List<BusinessRiskSignal>) {
    SectionHeader(title = "Risk / Attention")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    risks.forEach { risk ->
        UsahaNaikCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = insightContainerColor(risk.severity)
        ) {
            InsightSeverityBadge(severity = risk.severity)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(text = risk.title, style = MaterialTheme.typography.titleMedium)
            Text(text = risk.category.label, style = MaterialTheme.typography.labelMedium, color = InkMuted)
            Text(text = risk.message, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
    }
}

@Composable
private fun InsightSeverityBadge(severity: InsightSeverity) {
    PillBadge(
        text = "Severity: ${severity.label}",
        containerColor = CreamBackground,
        contentColor = when (severity) {
            InsightSeverity.Positive -> GreenPositive
            InsightSeverity.Info -> CoralPrimary
            InsightSeverity.Warning -> CoralPrimary
            InsightSeverity.Critical -> CoralPrimary
        }
    )
}

private fun insightContainerColor(severity: InsightSeverity) = when (severity) {
    InsightSeverity.Positive -> GreenSoft
    InsightSeverity.Info -> BlueSoft
    InsightSeverity.Warning -> YellowSoft
    InsightSeverity.Critical -> RoseSoft
}

@Composable
private fun WeeklyPlanDashboardSection(
    summary: WeeklyPlanDashboardSummary,
    onOpenWeeklyPlan: () -> Unit
) {
    SectionHeader(title = "Weekly Growth Plan", actionLabel = if (summary.hasPlan) "Active" else "Empty")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    UsahaNaikCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = if (summary.hasPlan) GreenSoft else YellowSoft
    ) {
        Text(text = summary.focusTitle, style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Next task: ${summary.nextTaskTitle}",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Text(text = summary.taskProgressLabel, style = MaterialTheme.typography.labelLarge)
        LinearProgressIndicator(
            progress = { summary.taskProgress },
            modifier = Modifier.fillMaxWidth(),
            color = GreenPositive,
            trackColor = CreamBackground
        )
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = summary.milestoneProgressLabel, style = MaterialTheme.typography.labelLarge)
        LinearProgressIndicator(
            progress = { summary.milestoneProgress },
            modifier = Modifier.fillMaxWidth(),
            color = CoralPrimary,
            trackColor = CreamBackground
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Button(onClick = onOpenWeeklyPlan, modifier = Modifier.fillMaxWidth()) {
            Text(summary.ctaLabel)
        }
    }
}

@Composable
private fun ContentPlannerDashboardSection(
    summary: ContentPlannerDashboardSummary,
    onOpenContentPlanner: () -> Unit
) {
    SectionHeader(title = "Content Planner", actionLabel = if (summary.hasIdeas) "Saved" else "Empty")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    UsahaNaikCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = if (summary.hasIdeas) LavenderSoft else YellowSoft
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            MetricCard(
                title = "Saved",
                value = summary.savedCount.toString(),
                helper = "Ideas",
                modifier = Modifier.weight(1f),
                containerColor = CreamBackground,
                accentColor = CoralPrimary
            )
            MetricCard(
                title = "Planned",
                value = summary.plannedCount.toString(),
                helper = "Ready",
                modifier = Modifier.weight(1f),
                containerColor = CreamBackground,
                accentColor = GreenPositive
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            MetricCard(
                title = "Done",
                value = summary.doneCount.toString(),
                helper = "Posted",
                modifier = Modifier.weight(1f),
                containerColor = CreamBackground,
                accentColor = GreenPositive
            )
            MetricCard(
                title = "Favorites",
                value = summary.favoriteCount.toString(),
                helper = "Pinned",
                modifier = Modifier.weight(1f),
                containerColor = CreamBackground,
                accentColor = CoralPrimary
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Text(text = "Next idea", style = MaterialTheme.typography.labelLarge, color = InkMuted)
        Text(text = summary.nextIdeaTitle, style = MaterialTheme.typography.titleMedium)
        Text(text = summary.nextIdeaPlatform, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Button(onClick = onOpenContentPlanner, modifier = Modifier.fillMaxWidth()) {
            Text(summary.ctaLabel)
        }
    }
}

@Composable
private fun DashboardContinuitySection(
    summary: DashboardContinuitySummary,
    onOpenWeeklyPlan: () -> Unit,
    onOpenContentPlanner: () -> Unit,
    onOpenRetrospective: () -> Unit
) {
    SectionHeader(title = "Progress Continuity", actionLabel = if (summary.hasProgressHistory) "Tracking" else "New")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = GreenSoft) {
        Text(text = "Weekly completion", style = MaterialTheme.typography.titleMedium)
        Text(text = summary.taskCompletionLabel, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        LinearProgressIndicator(
            progress = { summary.taskCompletionProgress },
            modifier = Modifier.fillMaxWidth(),
            color = GreenPositive,
            trackColor = CreamBackground
        )
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = summary.milestoneProgressLabel, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        LinearProgressIndicator(
            progress = { summary.milestoneProgress },
            modifier = Modifier.fillMaxWidth(),
            color = CoralPrimary,
            trackColor = CreamBackground
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Button(onClick = onOpenWeeklyPlan, modifier = Modifier.fillMaxWidth()) {
            Text("Open Weekly Plan")
        }
    }
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
        MetricCard(
            title = "Planned",
            value = summary.plannedContentCount.toString(),
            helper = "Content",
            modifier = Modifier.weight(1f),
            containerColor = BlueSoft,
            accentColor = CoralPrimary
        )
        MetricCard(
            title = "Posted",
            value = summary.postedOrDoneContentCount.toString(),
            helper = "Done",
            modifier = Modifier.weight(1f),
            containerColor = LavenderSoft,
            accentColor = GreenPositive
        )
    }
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = BlueSoft) {
        Text(text = "Next scheduled content", style = MaterialTheme.typography.titleMedium)
        Text(text = summary.nextScheduledContent, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Button(onClick = onOpenContentPlanner, modifier = Modifier.fillMaxWidth()) {
            Text("Open Content Planner")
        }
    }
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = CoralSoft) {
        Text(text = summary.latestRetrospectiveLabel, style = MaterialTheme.typography.titleMedium)
        Text(text = summary.latestRetrospectiveTakeaway, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Button(onClick = onOpenRetrospective, modifier = Modifier.fillMaxWidth()) {
            Text("Generate / View Retrospective")
        }
    }
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = YellowSoft) {
        Text(text = "Progress trend", style = MaterialTheme.typography.titleMedium)
        if (summary.trendPoints.isEmpty()) {
            Text(
                text = "Save weekly snapshots to see trend history here.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        } else {
            summary.trendPoints.forEach { point ->
                Text(text = point.label, style = MaterialTheme.typography.labelLarge)
                LinearProgressIndicator(
                    progress = { point.taskCompletionRate },
                    modifier = Modifier.fillMaxWidth(),
                    color = GreenPositive,
                    trackColor = CreamBackground
                )
                Text(
                    text = "Health ${point.businessHealthScore}/100",
                    style = MaterialTheme.typography.bodySmall,
                    color = InkMuted
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))
            }
        }
    }
}

@Composable
private fun FinancialDashboardMetricsSection(metrics: FinancialDashboardMetrics) {
    SectionHeader(title = "Financial Metrics")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    UsahaNaikCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = if (metrics.hasEntries) BlueSoft else YellowSoft
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            TargetProgressBlock(
                title = "Revenue Target",
                progress = metrics.targetRevenueProgress,
                modifier = Modifier.weight(1f)
            )
            TargetProgressBlock(
                title = "Profit Target",
                progress = metrics.targetProfitProgress,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        ReviewRow(
            label = "Largest expense",
            value = if (metrics.hasEntries) metrics.largestExpenseCategory else "-"
        )
        Text(
            text = if (metrics.hasEntries) {
                "Dashboard cards use financial entries saved in Room for this month."
            } else {
                "Start recording income and expenses to make your dashboard more accurate."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
    }
}

@Composable
private fun TargetProgressBlock(
    title: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = title, style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
            color = GreenPositive,
            trackColor = CreamBackground
        )
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        Text(
            text = "${(progress.coerceIn(0f, 1f) * 100).toInt()}%",
            style = MaterialTheme.typography.titleMedium,
            color = CoralPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FinancialTrackingSection(
    uiState: FinancialEntryUiState,
    onTypeChange: (FinancialEntryType) -> Unit,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit,
    onRequestDelete: (Long) -> Unit,
    onConfirmDelete: () -> Unit,
    onCancelDelete: () -> Unit
) {
    SectionHeader(title = "Financial Tracking", actionLabel = "Local")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = GreenSoft) {
        Text(text = "Record income or expenses", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Saved locally on this device. These entries help make dashboard metrics more accurate.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            FinancialTypeChip(
                label = "Income",
                selected = uiState.form.type == FinancialEntryType.Income,
                onClick = { onTypeChange(FinancialEntryType.Income) },
                modifier = Modifier.weight(1f)
            )
            FinancialTypeChip(
                label = "Expense",
                selected = uiState.form.type == FinancialEntryType.Expense,
                onClick = { onTypeChange(FinancialEntryType.Expense) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        OutlinedTextField(
            value = uiState.form.title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            isError = uiState.visibleTitleError() != null,
            modifier = Modifier.fillMaxWidth()
        )
        FieldError(uiState.visibleTitleError())
        OutlinedTextField(
            value = uiState.form.amount,
            onValueChange = onAmountChange,
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.visibleAmountError() != null,
            modifier = Modifier.fillMaxWidth()
        )
        FieldError(uiState.visibleAmountError())
        OutlinedTextField(
            value = uiState.form.date,
            onValueChange = onDateChange,
            label = { Text("Date") },
            isError = uiState.visibleDateError() != null,
            modifier = Modifier.fillMaxWidth()
        )
        FieldError(uiState.visibleDateError())
        Text(
            text = "Category",
            style = MaterialTheme.typography.labelLarge,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        FinancialCategoryChips(
            categories = uiState.categoriesForSelectedType,
            selectedCategory = uiState.form.category,
            onCategoryChange = onCategoryChange
        )
        FieldError(uiState.visibleCategoryError())
        OutlinedTextField(
            value = uiState.form.note,
            onValueChange = onNoteChange,
            label = { Text("Note optional") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Button(
            onClick = onSave,
            enabled = !uiState.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isSaving) "Saving..." else "Save Financial Entry")
        }
        uiState.successMessage?.let { message ->
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = GreenPositive)
        }
        uiState.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = CoralPrimary)
        }
    }
    Spacer(modifier = Modifier.height(AppSpacing.md))
    SectionHeader(title = "Recent Financial Activity")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    if (uiState.entries.isEmpty()) {
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = YellowSoft) {
            Text(text = "No financial entries yet", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Start recording income and expenses to make your dashboard more accurate.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
    } else {
        uiState.entries.take(5).forEach { entry ->
            RecentFinancialEntryRow(
                entry = entry,
                pendingDeleteEntryId = uiState.pendingDeleteEntryId,
                isDeleting = uiState.isDeleting,
                onRequestDelete = onRequestDelete,
                onConfirmDelete = onConfirmDelete,
                onCancelDelete = onCancelDelete
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
    }
}

@Composable
private fun FinancialTypeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FinancialCategoryChips(
    categories: List<String>,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
    ) {
        categories.forEach { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategoryChange(category) },
                label = { Text(category) }
            )
        }
    }
}

@Composable
private fun RecentFinancialEntryRow(
    entry: FinancialEntry,
    pendingDeleteEntryId: Long?,
    isDeleting: Boolean,
    onRequestDelete: (Long) -> Unit,
    onConfirmDelete: () -> Unit,
    onCancelDelete: () -> Unit
) {
    val isIncome = entry.type == FinancialEntryType.Income
    val amountLabel = if (isIncome) {
        "+${BusinessSetupCalculator.formatRupiah(entry.amount)}"
    } else {
        "-${BusinessSetupCalculator.formatRupiah(entry.amount)}"
    }
    UsahaNaikCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = if (isIncome) GreenSoft else CoralSoft
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                PillBadge(
                    text = entry.type.label,
                    containerColor = CreamBackground,
                    contentColor = if (isIncome) GreenPositive else CoralPrimary
                )
                Spacer(modifier = Modifier.height(AppSpacing.xs))
                Text(text = entry.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${entry.category} - ${entry.date}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
                if (entry.note.isNotBlank()) {
                    Text(
                        text = entry.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = InkMuted
                    )
                }
            }
            Text(
                text = amountLabel,
                style = MaterialTheme.typography.titleMedium,
                color = if (isIncome) GreenPositive else CoralPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        if (pendingDeleteEntryId == entry.id) {
            Text(
                text = "Delete this local financial entry?",
                style = MaterialTheme.typography.bodyMedium,
                color = CoralPrimary
            )
            Spacer(modifier = Modifier.height(AppSpacing.xs))
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                Button(
                    onClick = onConfirmDelete,
                    enabled = !isDeleting,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isDeleting) "Deleting..." else "Delete")
                }
                OutlinedButton(
                    onClick = onCancelDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            }
        } else {
            OutlinedButton(
                onClick = { onRequestDelete(entry.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Entry")
            }
        }
    }
}

@Composable
fun WeeklyPlanScreen(
    viewModel: WeeklyPlanViewModel,
    onOpenRetrospective: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ScreenContainer {
        SectionHeader(title = "Weekly Plan", actionLabel = "Local")
        Spacer(modifier = Modifier.height(AppSpacing.md))
        when {
            uiState.isLoading -> WeeklyPlanLoadingCard()
            uiState.activePlan == null -> WeeklyPlanEmptyState(
                uiState = uiState,
                onGenerate = viewModel::generatePlan
            )
            else -> WeeklyPlanContent(
                plan = checkNotNull(uiState.activePlan),
                uiState = uiState,
                onToggleTask = viewModel::toggleTaskCompletion,
                onRequestRegenerate = viewModel::requestRegeneratePlan,
                onConfirmRegenerate = viewModel::confirmRegeneratePlan,
                onCancelRegenerate = viewModel::cancelRegeneratePlan
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = LavenderSoft) {
            Text(text = "Weekly Retrospective", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Review this week's task, finance, milestone, and content progress. Evaluation is deterministic and saved locally.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
            Spacer(modifier = Modifier.height(AppSpacing.md))
            Button(onClick = onOpenRetrospective, modifier = Modifier.fillMaxWidth()) {
                Text("Open Retrospective")
            }
        }
        uiState.successMessage?.let { message ->
            Spacer(modifier = Modifier.height(AppSpacing.md))
            UsahaNaikCard(containerColor = GreenSoft) {
                Text(text = message, style = MaterialTheme.typography.bodyMedium, color = GreenPositive)
            }
        }
        uiState.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(AppSpacing.md))
            UsahaNaikCard(containerColor = RoseSoft) {
                Text(text = message, style = MaterialTheme.typography.bodyMedium, color = CoralPrimary)
            }
        }
    }
}

@Composable
private fun WeeklyPlanLoadingCard() {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = BlueSoft) {
        Text(text = "Loading weekly plan...", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "UsahaNaik is checking your saved local plan.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
    }
}

@Composable
private fun WeeklyPlanEmptyState(
    uiState: WeeklyPlanUiState,
    onGenerate: () -> Unit
) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = YellowSoft) {
        Text(
            text = uiState.emptyStateMessage ?: "Generate your first weekly growth plan.",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Weekly plans are generated with rule-based suggestions from your saved profile, financial data, and diagnosis insights.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Button(
            onClick = onGenerate,
            enabled = !uiState.isGenerating && uiState.profile != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isGenerating) "Generating..." else "Generate Weekly Growth Plan")
        }
    }
}

@Composable
private fun WeeklyPlanContent(
    plan: WeeklyGrowthPlan,
    uiState: WeeklyPlanUiState,
    onToggleTask: (String) -> Unit,
    onRequestRegenerate: () -> Unit,
    onConfirmRegenerate: () -> Unit,
    onCancelRegenerate: () -> Unit
) {
    WeeklyPlanHeader(plan = plan)
    Spacer(modifier = Modifier.height(AppSpacing.md))
    WeeklyFocusCard(plan = plan)
    Spacer(modifier = Modifier.height(AppSpacing.md))
    WeeklyProgressCard(plan = plan)
    Spacer(modifier = Modifier.height(AppSpacing.lg))
    SectionHeader(title = "Task List", actionLabel = "${plan.progressSummary.completedTasks}/${plan.progressSummary.totalTasks}")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    plan.tasks.forEach { task ->
        WeeklyTaskCard(task = task, onToggleTask = onToggleTask)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
    }
    SectionHeader(title = "Weekly Challenge")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    WeeklyChallengeCard(plan = plan)
    Spacer(modifier = Modifier.height(AppSpacing.lg))
    SectionHeader(title = "Milestones")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    plan.milestones.forEach { milestone ->
        WeeklyMilestoneCard(milestone = milestone)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
    }
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = LavenderSoft) {
        Text(text = "Plan note", style = MaterialTheme.typography.titleMedium)
        Text(text = plan.limitationsNote, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.md))
        if (uiState.showRegenerateConfirmation) {
            Text(
                text = "Replace the active weekly plan with a new plan from the latest data?",
                style = MaterialTheme.typography.bodyMedium,
                color = CoralPrimary
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                Button(
                    onClick = onConfirmRegenerate,
                    enabled = !uiState.isGenerating,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (uiState.isGenerating) "Generating..." else "Replace")
                }
                OutlinedButton(onClick = onCancelRegenerate, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
            }
        } else {
            OutlinedButton(onClick = onRequestRegenerate, modifier = Modifier.fillMaxWidth()) {
                Text("Regenerate Plan")
            }
        }
    }
}

@Composable
private fun WeeklyPlanHeader(plan: WeeklyGrowthPlan) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = CoralSoft) {
        PillBadge(text = plan.status.label, containerColor = CreamBackground, contentColor = CoralPrimary)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = plan.businessName, style = MaterialTheme.typography.titleLarge)
        Text(text = "${plan.businessCategoryName} - Week of ${plan.generatedDate}", style = MaterialTheme.typography.bodyMedium, color = InkMuted)
    }
}

@Composable
private fun WeeklyFocusCard(plan: WeeklyGrowthPlan) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = YellowSoft) {
        PillBadge(text = plan.focus.category.label, containerColor = CreamBackground, contentColor = CoralPrimary)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = plan.focus.title, style = MaterialTheme.typography.titleLarge)
        Text(text = plan.priorityReason, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "Target: ${plan.target}", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun WeeklyProgressCard(plan: WeeklyGrowthPlan) {
    val summary = plan.progressSummary
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = GreenSoft) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            MetricCard(
                title = "Tasks",
                value = "${summary.completedTasks}/${summary.totalTasks}",
                helper = "Completed",
                modifier = Modifier.weight(1f),
                containerColor = CreamBackground,
                accentColor = GreenPositive
            )
            MetricCard(
                title = "Milestones",
                value = "${summary.completedMilestones}/${summary.totalMilestones}",
                helper = "Reached",
                modifier = Modifier.weight(1f),
                containerColor = CreamBackground,
                accentColor = CoralPrimary
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        LinearProgressIndicator(
            progress = { summary.taskProgress },
            modifier = Modifier.fillMaxWidth(),
            color = GreenPositive,
            trackColor = CreamBackground
        )
        Text(
            text = "Next task: ${summary.nextTask?.title ?: "All tasks completed"}",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
    }
}

@Composable
private fun WeeklyTaskCard(
    task: WeeklyTask,
    onToggleTask: (String) -> Unit
) {
    val completed = task.status == WeeklyTaskStatus.Completed
    UsahaNaikCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = if (completed) GreenSoft else BlueSoft
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Checkbox(
                checked = completed,
                onCheckedChange = { onToggleTask(task.id) }
            )
            Column(modifier = Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                    PillBadge(text = task.difficulty.label, containerColor = CreamBackground, contentColor = CoralPrimary)
                    PillBadge(text = task.estimatedTime.label, containerColor = CreamBackground, contentColor = GreenPositive)
                }
                Spacer(modifier = Modifier.height(AppSpacing.xs))
                Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(AppSpacing.xs))
                Text(text = "Reason: ${task.reason}", style = MaterialTheme.typography.bodySmall, color = InkMuted)
                Text(text = "Expected outcome: ${task.expectedOutcome}", style = MaterialTheme.typography.bodySmall, color = InkMuted)
            }
        }
    }
}

@Composable
private fun WeeklyChallengeCard(plan: WeeklyGrowthPlan) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = CoralSoft) {
        PillBadge(text = "Challenge", containerColor = CreamBackground, contentColor = CoralPrimary)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = plan.challenge.title, style = MaterialTheme.typography.titleLarge)
        Text(text = plan.challenge.description, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        plan.challenge.checklistItems.forEach { item ->
            Text(text = "- $item", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "Target: ${plan.challenge.completionTarget}", style = MaterialTheme.typography.bodyMedium)
        Text(text = plan.challenge.motivationalCopy, style = MaterialTheme.typography.bodySmall, color = InkMuted)
    }
}

@Composable
private fun WeeklyMilestoneCard(milestone: BusinessMilestone) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = LavenderSoft) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                PillBadge(text = milestone.status.label, containerColor = CreamBackground, contentColor = CoralPrimary)
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(text = milestone.title, style = MaterialTheme.typography.titleMedium)
                Text(text = milestone.description, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
            }
            Text(
                text = "${milestone.progressPercentage}%",
                style = MaterialTheme.typography.titleMedium,
                color = CoralPrimary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        LinearProgressIndicator(
            progress = { milestone.progressPercentage / 100f },
            modifier = Modifier.fillMaxWidth(),
            color = GreenPositive,
            trackColor = CreamBackground
        )
    }
}

@Composable
fun WeeklyRetrospectiveScreen(viewModel: WeeklyRetrospectiveViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    ScreenContainer {
        SectionHeader(title = "Weekly Retrospective", actionLabel = "Local")
        Text(
            text = "Generate a deterministic weekly evaluation from saved tasks, milestones, finance, content calendar, and diagnosis signals.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = CoralSoft) {
            Text(text = "This week's review", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Retrospectives are planning summaries, not professional financial advice or guaranteed outcomes.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
            Spacer(modifier = Modifier.height(AppSpacing.md))
            Button(
                onClick = viewModel::generateAndSaveRetrospective,
                enabled = !uiState.isGenerating,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isGenerating) "Generating..." else "Generate & Save Retrospective")
            }
        }
        uiState.successMessage?.let { message ->
            Spacer(modifier = Modifier.height(AppSpacing.md))
            UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = GreenSoft) {
                Text(text = message, style = MaterialTheme.typography.bodyMedium, color = GreenPositive)
            }
        }
        uiState.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(AppSpacing.md))
            UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = RoseSoft) {
                Text(text = message, style = MaterialTheme.typography.bodyMedium, color = CoralPrimary)
            }
        }
        uiState.currentSnapshot?.let { snapshot ->
            Spacer(modifier = Modifier.height(AppSpacing.lg))
            WeeklyProgressSnapshotCard(snapshot = snapshot)
        }
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        uiState.latestRetrospective?.let { retrospective ->
            WeeklyRetrospectiveCard(retrospective = retrospective)
        } ?: UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = YellowSoft) {
            Text(
                text = uiState.emptyStateMessage ?: "No retrospective yet",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Generate a retrospective after creating a weekly plan, recording finances, and scheduling content.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        ProgressHistorySection(summary = uiState.progressHistorySummary)
        Spacer(modifier = Modifier.height(AppSpacing.lg))
        RetrospectiveHistorySection(history = uiState.history)
    }
}

@Composable
private fun WeeklyProgressSnapshotCard(snapshot: WeeklyProgressSnapshot) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = GreenSoft) {
        SectionHeader(title = "Progress Snapshot", actionLabel = snapshot.weekLabel)
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            MetricCard(
                title = "Tasks",
                value = "${snapshot.completedTasks}/${snapshot.totalTasks}",
                helper = "${(snapshot.taskCompletionRate * 100).toInt()}%",
                modifier = Modifier.weight(1f),
                containerColor = CreamBackground,
                accentColor = GreenPositive
            )
            MetricCard(
                title = "Content",
                value = snapshot.postedOrDoneContentCount.toString(),
                helper = "Posted/done",
                modifier = Modifier.weight(1f),
                containerColor = CreamBackground,
                accentColor = CoralPrimary
            )
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        ReviewRow("Weekly income", BusinessSetupCalculator.formatRupiah(snapshot.weeklyIncome))
        ReviewRow("Weekly expenses", BusinessSetupCalculator.formatRupiah(snapshot.weeklyExpenses))
        ReviewRow("Estimated profit", BusinessSetupCalculator.formatRupiah(snapshot.weeklyEstimatedProfit))
        ReviewRow("Business health score", "${snapshot.businessHealthScore}/100")
    }
}

@Composable
private fun WeeklyRetrospectiveCard(retrospective: WeeklyRetrospective) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = BlueSoft) {
        PillBadge(text = retrospective.weekLabel, containerColor = CreamBackground, contentColor = CoralPrimary)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = retrospective.summaryTitle, style = MaterialTheme.typography.titleLarge)
        Text(text = "Generated ${retrospective.generatedDate}", style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.md))
        retrospective.sections.forEach { section ->
            Text(text = section.title, style = MaterialTheme.typography.titleMedium)
            section.insights.forEach { insight ->
                Text(
                    text = "- ${insight.message}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = when (insight.severity) {
                        InsightSeverity.Positive -> GreenPositive
                        InsightSeverity.Info -> InkMuted
                        InsightSeverity.Warning -> CoralPrimary
                        InsightSeverity.Critical -> CoralPrimary
                    }
                )
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(CreamBackground)
                .padding(AppSpacing.md)
        ) {
            Text(text = "Next week suggestion", style = MaterialTheme.typography.titleMedium)
            Text(text = retrospective.nextWeekSuggestion.focus, style = MaterialTheme.typography.bodyMedium)
            Text(text = retrospective.nextWeekSuggestion.reason, style = MaterialTheme.typography.bodySmall, color = InkMuted)
            Text(text = retrospective.nextWeekSuggestion.recommendedAction, style = MaterialTheme.typography.bodySmall, color = InkMuted)
        }
    }
}

@Composable
private fun ProgressHistorySection(summary: WeeklyProgressHistorySummary) {
    SectionHeader(title = "Progress Trend", actionLabel = if (summary.hasHistory) "Saved" else "Empty")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = LavenderSoft) {
        Text(
            text = if (summary.hasHistory) {
                "Average task completion: ${(summary.averageTaskCompletionRate * 100).toInt()}%"
            } else {
                "Save weekly snapshots to build progress history."
            },
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        if (summary.trendPoints.isEmpty()) {
            Text(text = "No trend points yet.", style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        } else {
            summary.trendPoints.forEach { point ->
                Text(text = point.label, style = MaterialTheme.typography.labelLarge)
                LinearProgressIndicator(
                    progress = { point.taskCompletionRate },
                    modifier = Modifier.fillMaxWidth(),
                    color = GreenPositive,
                    trackColor = CreamBackground
                )
                Text(
                    text = "Health score: ${point.businessHealthScore}/100",
                    style = MaterialTheme.typography.bodySmall,
                    color = InkMuted
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))
            }
        }
    }
}

@Composable
private fun RetrospectiveHistorySection(history: List<WeeklyRetrospective>) {
    SectionHeader(title = "Retrospective History", actionLabel = "${history.size} saved")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    if (history.isEmpty()) {
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = YellowSoft) {
            Text(text = "No saved retrospective history yet", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Generate and save a retrospective to review progress over time.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
    } else {
        history.forEach { item ->
            UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = CreamBackground) {
                Text(text = item.weekLabel, style = MaterialTheme.typography.titleMedium)
                Text(text = item.nextWeekSuggestion.focus, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
                Text(text = "Generated ${item.generatedDate}", style = MaterialTheme.typography.bodySmall, color = InkMuted)
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
    }
}

@Composable
fun ContentIdeasScreen(
    viewModel: ContentPlannerViewModel,
    calendarViewModel: ContentCalendarViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val calendarState by calendarViewModel.uiState.collectAsState()

    ScreenContainer {
        SectionHeader(title = "Content Planner", actionLabel = uiState.generationSource.label)
        Text(
            text = "Generate content planning suggestions from your saved business profile, selected goal, and platform. Review and adjust every idea before posting.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        if (!uiState.hasProfile) {
            ContentPlannerEmptyState(message = uiState.emptyStateMessage ?: "Complete business setup first.")
        } else {
            ContentGenerationControls(
                uiState = uiState,
                onPlatformChange = viewModel::updatePlatform,
                onGoalChange = viewModel::updateGoal,
                onTypeChange = viewModel::updateType,
                onIdeaCountChange = viewModel::updateIdeaCount,
                onGenerate = viewModel::generateIdeas
            )
            Spacer(modifier = Modifier.height(AppSpacing.lg))
            GeneratedIdeasSection(
                ideas = uiState.generatedIdeas,
                isGenerating = uiState.isGenerating,
                onSave = viewModel::saveIdea
            )
            if (uiState.promotionCampaigns.isNotEmpty()) {
                Spacer(modifier = Modifier.height(AppSpacing.lg))
                PromotionPlannerSection(campaigns = uiState.promotionCampaigns)
            }
            Spacer(modifier = Modifier.height(AppSpacing.lg))
            SavedIdeasSection(
                uiState = uiState,
                calendarState = calendarState,
                onFilterChange = viewModel::updateFilter,
                onSchedule = calendarViewModel::requestSchedule,
                onScheduleDateChange = calendarViewModel::updateScheduledDate,
                onScheduleTimeChange = calendarViewModel::updateTimeLabel,
                onScheduleNoteChange = calendarViewModel::updatePostingNote,
                onSchedulePlatformChange = calendarViewModel::updatePlatform,
                onSaveSchedule = calendarViewModel::saveSchedule,
                onCancelSchedule = calendarViewModel::cancelSchedule,
                onFavorite = viewModel::toggleFavorite,
                onMarkDraft = viewModel::markDraft,
                onMarkPlanned = viewModel::markPlanned,
                onMarkDone = viewModel::markDone,
                onDelete = viewModel::deleteIdea
            )
            Spacer(modifier = Modifier.height(AppSpacing.lg))
            ContentCalendarSection(
                uiState = calendarState,
                onStatusChange = calendarViewModel::updateStatus,
                onDelete = calendarViewModel::deleteSchedule
            )
        }
        uiState.successMessage?.let { message ->
            Spacer(modifier = Modifier.height(AppSpacing.md))
            UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = GreenSoft) {
                Text(text = message, style = MaterialTheme.typography.bodyMedium, color = GreenPositive)
            }
        }
        uiState.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(AppSpacing.md))
            UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = RoseSoft) {
                Text(text = message, style = MaterialTheme.typography.bodyMedium, color = CoralPrimary)
            }
        }
    }
}

@Composable
private fun ContentPlannerEmptyState(message: String) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = YellowSoft) {
        Text(text = message, style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Generate your first content ideas after saving a business profile. Local deterministic ideas remain available without paid AI access.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
    }
}

@Composable
private fun ContentGenerationControls(
    uiState: ContentPlannerUiState,
    onPlatformChange: (ContentPlatform) -> Unit,
    onGoalChange: (ContentGoal) -> Unit,
    onTypeChange: (ContentIdeaType?) -> Unit,
    onIdeaCountChange: (Int) -> Unit,
    onGenerate: () -> Unit
) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = LavenderSoft) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            PillBadge(text = "Local deterministic", containerColor = CreamBackground, contentColor = CoralPrimary)
            if (uiState.usedFallback) {
                PillBadge(text = "Fallback used", containerColor = CreamBackground, contentColor = GreenPositive)
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "Generation controls", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Choose a platform and content goal. Remote AI is optional; local generation works offline.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Text(text = "Platform", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        ChipFlow {
            ContentPlatform.entries.forEach { platform ->
                FilterChip(
                    selected = uiState.form.platform == platform,
                    onClick = { onPlatformChange(platform) },
                    label = { Text(platform.label) }
                )
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "Content goal", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        ChipFlow {
            ContentGoal.entries.forEach { goal ->
                FilterChip(
                    selected = uiState.form.goal == goal,
                    onClick = { onGoalChange(goal) },
                    label = { Text(goal.label) }
                )
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "Content type", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(AppSpacing.xs))
        ChipFlow {
            FilterChip(
                selected = uiState.form.type == null,
                onClick = { onTypeChange(null) },
                label = { Text("Any") }
            )
            ContentIdeaType.entries.forEach { type ->
                FilterChip(
                    selected = uiState.form.type == type,
                    onClick = { onTypeChange(type) },
                    label = { Text(type.label) }
                )
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "Idea count", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            listOf(5, 6, 8, 10).forEach { count ->
                FilterChip(
                    selected = uiState.form.ideaCount == count,
                    onClick = { onIdeaCountChange(count) },
                    label = { Text(count.toString()) }
                )
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Button(
            onClick = onGenerate,
            enabled = !uiState.isGenerating,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isGenerating) "Generating Ideas..." else "Generate Content Ideas")
        }
    }
}

@Composable
private fun GeneratedIdeasSection(
    ideas: List<ContentIdea>,
    isGenerating: Boolean,
    onSave: (ContentIdea) -> Unit
) {
    SectionHeader(title = "Generated Ideas", actionLabel = if (ideas.isEmpty()) "Draft" else "${ideas.size} ideas")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    if (ideas.isEmpty()) {
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = BlueSoft) {
            Text(
                text = if (isGenerating) "Generating content ideas..." else "Generate your first content ideas based on your business profile.",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Ideas include hook, caption draft, CTA, visual suggestion, and safety notes.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
    } else {
        ideas.forEach { idea ->
            ContentIdeaPlannerCard(
                idea = idea,
                showPersistenceActions = false,
                onSave = { onSave(idea) },
                onFavorite = {},
                onMarkDraft = {},
                onMarkPlanned = {},
                onMarkDone = {},
                onDelete = {}
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
    }
}

@Composable
private fun SavedIdeasSection(
    uiState: ContentPlannerUiState,
    calendarState: ContentCalendarUiState,
    onFilterChange: (ContentIdeaFilter) -> Unit,
    onSchedule: (ContentIdea) -> Unit,
    onScheduleDateChange: (String) -> Unit,
    onScheduleTimeChange: (String) -> Unit,
    onScheduleNoteChange: (String) -> Unit,
    onSchedulePlatformChange: (ContentPlatform) -> Unit,
    onSaveSchedule: () -> Unit,
    onCancelSchedule: () -> Unit,
    onFavorite: (Long) -> Unit,
    onMarkDraft: (Long) -> Unit,
    onMarkPlanned: (Long) -> Unit,
    onMarkDone: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    SectionHeader(title = "Saved Ideas", actionLabel = uiState.filter.label)
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    ChipFlow {
        ContentIdeaFilter.entries.forEach { filter ->
            FilterChip(
                selected = uiState.filter == filter,
                onClick = { onFilterChange(filter) },
                label = { Text(filter.label) }
            )
        }
    }
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    if (calendarState.form.isActive) {
        ContentScheduleFormCard(
            uiState = calendarState,
            onDateChange = onScheduleDateChange,
            onTimeChange = onScheduleTimeChange,
            onNoteChange = onScheduleNoteChange,
            onPlatformChange = onSchedulePlatformChange,
            onSave = onSaveSchedule,
            onCancel = onCancelSchedule
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
    }
    if (uiState.savedIdeas.isEmpty()) {
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = YellowSoft) {
            Text(text = "No saved ideas yet", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Save generated ideas to plan, favorite, complete, or delete them locally.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
    } else {
        uiState.savedIdeas.forEach { idea ->
            ContentIdeaPlannerCard(
                idea = idea,
                showPersistenceActions = true,
                onSave = {},
                onSchedule = { onSchedule(idea) },
                onFavorite = { onFavorite(idea.id) },
                onMarkDraft = { onMarkDraft(idea.id) },
                onMarkPlanned = { onMarkPlanned(idea.id) },
                onMarkDone = { onMarkDone(idea.id) },
                onDelete = { onDelete(idea.id) }
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
    }
}

@Composable
private fun ContentIdeaPlannerCard(
    idea: ContentIdea,
    showPersistenceActions: Boolean,
    onSave: () -> Unit,
    onSchedule: () -> Unit = {},
    onFavorite: () -> Unit,
    onMarkDraft: () -> Unit,
    onMarkPlanned: () -> Unit,
    onMarkDone: () -> Unit,
    onDelete: () -> Unit
) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = contentIdeaContainerColor(idea.source)) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            PillBadge(text = idea.source.label, containerColor = CreamBackground, contentColor = CoralPrimary)
            PillBadge(text = idea.status.label, containerColor = CreamBackground, contentColor = GreenPositive)
            if (idea.isFavorite) {
                PillBadge(text = "Favorite", containerColor = CreamBackground, contentColor = CoralPrimary)
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = idea.title, style = MaterialTheme.typography.titleLarge)
        Text(
            text = "${idea.platform.label} - ${idea.goal.label} - ${idea.type.label}",
            style = MaterialTheme.typography.labelMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        ContentIdeaDetail(label = "Hook", value = idea.hook)
        ContentIdeaDetail(label = "Angle", value = idea.angle)
        ContentIdeaDetail(label = "Caption draft", value = idea.captionDraft)
        ContentIdeaDetail(label = "CTA", value = idea.cta)
        ContentIdeaDetail(label = "Visual", value = idea.visualSuggestion)
        ContentIdeaDetail(label = "Posting note", value = idea.recommendedPostingNote)
        ContentIdeaDetail(label = "Safety note", value = idea.safetyNote)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        if (showPersistenceActions) {
            ChipFlow {
                FilterChip(selected = idea.isFavorite, onClick = onFavorite, label = { Text(if (idea.isFavorite) "Unfavorite" else "Favorite") })
                FilterChip(selected = idea.status == ContentIdeaStatus.Draft, onClick = onMarkDraft, label = { Text("Draft") })
                FilterChip(selected = idea.status == ContentIdeaStatus.Planned, onClick = onMarkPlanned, label = { Text("Planned") })
                FilterChip(selected = idea.status == ContentIdeaStatus.Done, onClick = onMarkDone, label = { Text("Done") })
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Button(onClick = onSchedule, modifier = Modifier.fillMaxWidth()) {
                Text("Schedule")
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            OutlinedButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                Text("Delete Idea")
            }
        } else {
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) {
                Text("Save Idea")
            }
        }
    }
}

@Composable
private fun ContentIdeaDetail(label: String, value: String) {
    Text(text = label, style = MaterialTheme.typography.labelMedium, color = CoralPrimary)
    Text(text = value, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
    Spacer(modifier = Modifier.height(AppSpacing.xs))
}

@Composable
private fun ContentScheduleFormCard(
    uiState: ContentCalendarUiState,
    onDateChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onPlatformChange: (ContentPlatform) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val form = uiState.form
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = BlueSoft) {
        PillBadge(text = "Local calendar", containerColor = CreamBackground, contentColor = CoralPrimary)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "Schedule content", style = MaterialTheme.typography.titleLarge)
        Text(text = form.title, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
        Spacer(modifier = Modifier.height(AppSpacing.md))
        OutlinedTextField(
            value = form.scheduledDate,
            onValueChange = onDateChange,
            label = { Text("Scheduled date (YYYY-MM-DD)") },
            isError = form.dateError != null,
            modifier = Modifier.fillMaxWidth()
        )
        FieldError(form.dateError)
        OutlinedTextField(
            value = form.timeLabel,
            onValueChange = onTimeChange,
            label = { Text("Time label optional") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "Platform", style = MaterialTheme.typography.labelLarge)
        ChipFlow {
            ContentPlatform.entries.forEach { platform ->
                FilterChip(
                    selected = form.platform == platform,
                    onClick = { onPlatformChange(platform) },
                    label = { Text(platform.label) }
                )
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        OutlinedTextField(
            value = form.postingNote,
            onValueChange = onNoteChange,
            label = { Text("Posting note optional") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            Button(
                onClick = onSave,
                enabled = form.canSave && !uiState.isSaving,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (uiState.isSaving) "Saving..." else "Save")
            }
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Cancel")
            }
        }
    }
}

@Composable
private fun ContentCalendarSection(
    uiState: ContentCalendarUiState,
    onStatusChange: (Long, ContentCalendarStatus) -> Unit,
    onDelete: (Long) -> Unit
) {
    SectionHeader(title = "Content Calendar", actionLabel = "${uiState.schedules.size} scheduled")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    uiState.successMessage?.let { message ->
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = GreenSoft) {
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = GreenPositive)
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
    }
    uiState.errorMessage?.let { message ->
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = RoseSoft) {
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = CoralPrimary)
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
    }
    if (uiState.upcomingSchedules.isEmpty()) {
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = YellowSoft) {
            Text(text = "No scheduled content yet", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Schedule saved ideas into this local calendar. No Android calendar permission or external calendar sync is used.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkMuted
            )
        }
    } else {
        uiState.upcomingSchedules.forEach { item ->
            ContentCalendarItemCard(
                item = item,
                onStatusChange = onStatusChange,
                onDelete = onDelete
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
        }
    }
}

@Composable
private fun ContentCalendarItemCard(
    item: ContentCalendarSchedule,
    onStatusChange: (Long, ContentCalendarStatus) -> Unit,
    onDelete: (Long) -> Unit
) {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = calendarStatusContainerColor(item.status)) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            PillBadge(text = item.status.label, containerColor = CreamBackground, contentColor = CoralPrimary)
            PillBadge(text = item.platform.label, containerColor = CreamBackground, contentColor = GreenPositive)
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = item.title, style = MaterialTheme.typography.titleMedium)
        Text(
            text = listOf(item.scheduledDate, item.timeLabel).filter { it.isNotBlank() }.joinToString(" - "),
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        if (item.postingNote.isNotBlank()) {
            Text(text = item.postingNote, style = MaterialTheme.typography.bodySmall, color = InkMuted)
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        ChipFlow {
            ContentCalendarStatus.entries.forEach { status ->
                FilterChip(
                    selected = item.status == status,
                    onClick = { onStatusChange(item.id, status) },
                    label = { Text(status.label) }
                )
            }
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        OutlinedButton(onClick = { onDelete(item.id) }, modifier = Modifier.fillMaxWidth()) {
            Text("Delete Schedule")
        }
    }
}

@Composable
private fun PromotionPlannerSection(campaigns: List<PromotionCampaign>) {
    SectionHeader(title = "Promotion Planner", actionLabel = "${campaigns.size} campaigns")
    Spacer(modifier = Modifier.height(AppSpacing.sm))
    campaigns.forEach { campaign ->
        UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = CoralSoft) {
            PillBadge(text = "Promotion draft", containerColor = CreamBackground, contentColor = CoralPrimary)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(text = campaign.title, style = MaterialTheme.typography.titleLarge)
            Text(text = campaign.objective, style = MaterialTheme.typography.bodyMedium, color = InkMuted)
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            campaign.recommendedContentSequence.forEachIndexed { index, step ->
                Text(text = "${index + 1}. $step", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(
                text = "Expected outcome: ${campaign.expectedOutcome}",
                style = MaterialTheme.typography.bodySmall,
                color = InkMuted
            )
            Text(text = campaign.safetyNote, style = MaterialTheme.typography.bodySmall, color = InkMuted)
        }
        Spacer(modifier = Modifier.height(AppSpacing.sm))
    }
}

private fun contentIdeaContainerColor(source: ContentGenerationSource) = when (source) {
    ContentGenerationSource.Local -> GreenSoft
    ContentGenerationSource.Ai -> LavenderSoft
    ContentGenerationSource.Fallback -> YellowSoft
}

private fun calendarStatusContainerColor(status: ContentCalendarStatus) = when (status) {
    ContentCalendarStatus.Planned -> BlueSoft
    ContentCalendarStatus.Posted -> GreenSoft
    ContentCalendarStatus.Skipped -> RoseSoft
    ContentCalendarStatus.Done -> LavenderSoft
}

@Composable
fun SettingsScreen(viewModel: BusinessSetupViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    ScreenContainer {
        SectionHeader(title = "Profile")
        Text(
            text = "Your business profile is saved locally on this device. UsahaNaik does not sync this data to cloud in this version.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
        AiProviderSettingsSection()
        Spacer(modifier = Modifier.height(AppSpacing.md))
        if (uiState.savedProfile == null) {
            UsahaNaikCard(containerColor = YellowSoft) {
                Text(text = "No saved profile", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Complete setup and save your business profile to show it here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
            }
        } else {
            val profile = checkNotNull(uiState.savedProfile)
            UsahaNaikCard(containerColor = GreenSoft) {
                PillBadge(text = "Saved locally", containerColor = CreamBackground, contentColor = GreenPositive)
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(text = profile.draft.businessName, style = MaterialTheme.typography.titleLarge)
                ReviewRow("Category ID", profile.draft.categoryId.orEmpty())
                ReviewRow("Monthly revenue", profile.draft.monthlyRevenue)
                ReviewRow("Monthly expenses", profile.draft.monthlyExpenses)
                ReviewRow("Target revenue", profile.draft.targetMonthlyRevenue)
                ReviewRow("Main focus", profile.draft.mainFocus?.label ?: "-")
                ReviewRow("Updated at", profile.updatedAt.toString())
            }
            Spacer(modifier = Modifier.height(AppSpacing.md))
            UsahaNaikCard(containerColor = RoseSoft) {
                Text(text = "Delete local profile", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Deleting local profile removes the saved setup from this device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkMuted
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                if (showDeleteConfirmation) {
                    Text(
                        text = "Confirm delete? This only removes local data on this device.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CoralPrimary
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.sm))
                    Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                        Button(
                            onClick = {
                                viewModel.deleteSavedProfile()
                                showDeleteConfirmation = false
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (uiState.isDeletingProfile) "Deleting..." else "Delete")
                        }
                        OutlinedButton(
                            onClick = { showDeleteConfirmation = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = { showDeleteConfirmation = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Delete Local Profile")
                    }
                }
            }
        }
        uiState.deleteSuccessMessage?.let { message ->
            Spacer(modifier = Modifier.height(AppSpacing.md))
            UsahaNaikCard(containerColor = GreenSoft) {
                Text(text = message, style = MaterialTheme.typography.bodyMedium)
            }
        }
        uiState.deleteErrorMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = CoralPrimary
            )
        }
    }
}

@Composable
private fun AiProviderSettingsSection() {
    UsahaNaikCard(modifier = Modifier.fillMaxWidth(), containerColor = BlueSoft) {
        PillBadge(text = "Local only", containerColor = CreamBackground, contentColor = CoralPrimary)
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(text = "AI content provider", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Content generation currently uses a deterministic local provider. Optional AI provider configuration is planned, and no API key is hardcoded in this build.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkMuted
        )
        Spacer(modifier = Modifier.height(AppSpacing.sm))
        Text(
            text = "If API key settings are added later, keys must be user-provided, masked in UI, stored only on this device, and never logged.",
            style = MaterialTheme.typography.bodySmall,
            color = InkMuted
        )
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
