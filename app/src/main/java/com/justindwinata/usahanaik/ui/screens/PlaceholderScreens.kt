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
import com.justindwinata.usahanaik.domain.model.AvailableTime
import com.justindwinata.usahanaik.domain.model.BusinessChallenge
import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import com.justindwinata.usahanaik.domain.model.BusinessStage
import com.justindwinata.usahanaik.domain.model.BusinessTask
import com.justindwinata.usahanaik.domain.model.ContentIdea
import com.justindwinata.usahanaik.domain.model.CostDriver
import com.justindwinata.usahanaik.domain.model.Milestone
import com.justindwinata.usahanaik.domain.model.MonthlyFocus
import com.justindwinata.usahanaik.domain.model.SellingChannel
import com.justindwinata.usahanaik.domain.model.StockIssue
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
fun DashboardScreen(setupDraft: BusinessSetupDraft? = null) {
    val dashboard = remember(setupDraft) { SampleGrowthRepository().getDashboardPreview(setupDraft) }

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
