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
import com.justindwinata.usahanaik.domain.finance.FinancialDashboardMetrics
import com.justindwinata.usahanaik.domain.finance.FinancialDashboardMetricsMapper
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessProfile
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessStage
import com.justindwinata.usahanaik.domain.model.BusinessTask
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.Milestone
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.SellingChannel
import com.justindwinata.usahanaik.domain.model.StockIssue
import com.justindwinata.usahanaik.domain.model.FinancialEntry
import com.justindwinata.usahanaik.domain.model.FinancialEntryType
import com.justindwinata.usahanaik.domain.setup.BusinessCategorySetupHints
import com.justindwinata.usahanaik.domain.setup.BusinessSetupCalculator
import com.justindwinata.usahanaik.domain.setup.BusinessSetupField
import com.justindwinata.usahanaik.ui.components.MetricCard
import com.justindwinata.usahanaik.ui.components.PillBadge
import com.justindwinata.usahanaik.ui.components.PrimaryActionButton
import com.justindwinata.usahanaik.ui.components.ProgressScoreCard
import com.justindwinata.usahanaik.ui.components.SectionHeader
import com.justindwinata.usahanaik.ui.components.TrendLineChart
import com.justindwinata.usahanaik.ui.components.UsahaNaikCard
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
    dashboardInsightsViewModel: DashboardInsightsViewModel
) {
    val dashboard = remember(setupDraft) { SampleGrowthRepository().getDashboardPreview(setupDraft) }
    val financialState by financialEntryViewModel.uiState.collectAsState()
    val financialMetrics = remember(financialState.summary, dashboard) {
        FinancialDashboardMetricsMapper.from(
            summary = financialState.summary,
            fallbackSummary = dashboard.financialSummary,
            fallbackTrend = dashboard.trend
        )
    }

    LaunchedEffect(setupDraft?.targetMonthlyRevenue, setupDraft?.targetMonthlyProfit) {
        financialEntryViewModel.refresh(
            targetMonthlyRevenue = setupDraft?.targetMonthlyRevenue,
            targetMonthlyProfit = setupDraft?.targetMonthlyProfit
        )
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
            title = "Business Growth Score",
            score = dashboard.healthScore.score,
            helper = dashboard.healthScore.explanation,
            containerColor = LavenderSoft
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))
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
