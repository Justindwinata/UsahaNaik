package com.justindwinata.usahanaik.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.justindwinata.usahanaik.data.ai.LocalContentIdeaProvider
import com.justindwinata.usahanaik.data.local.UsahaNaikDatabase
import com.justindwinata.usahanaik.data.repository.LocalBusinessProfileRepository
import com.justindwinata.usahanaik.data.repository.LocalContentCalendarRepository
import com.justindwinata.usahanaik.data.repository.LocalContentIdeaRepository
import com.justindwinata.usahanaik.data.repository.LocalFinancialEntryRepository
import com.justindwinata.usahanaik.data.repository.LocalWeeklyProgressHistoryRepository
import com.justindwinata.usahanaik.data.repository.LocalWeeklyPlanRepository
import com.justindwinata.usahanaik.data.repository.LocalWeeklyRetrospectiveRepository
import com.justindwinata.usahanaik.ui.content.ContentCalendarViewModel
import com.justindwinata.usahanaik.ui.content.ContentCalendarViewModelFactory
import com.justindwinata.usahanaik.ui.content.ContentPlannerViewModel
import com.justindwinata.usahanaik.ui.content.ContentPlannerViewModelFactory
import com.justindwinata.usahanaik.ui.dashboard.DashboardInsightsViewModel
import com.justindwinata.usahanaik.ui.dashboard.DashboardInsightsViewModelFactory
import com.justindwinata.usahanaik.ui.finance.FinancialEntryViewModel
import com.justindwinata.usahanaik.ui.finance.FinancialEntryViewModelFactory
import com.justindwinata.usahanaik.ui.navigation.AppRoute
import com.justindwinata.usahanaik.ui.navigation.bottomTabs
import com.justindwinata.usahanaik.ui.navigation.onboardingRoutes
import com.justindwinata.usahanaik.ui.progress.WeeklyRetrospectiveViewModel
import com.justindwinata.usahanaik.ui.progress.WeeklyRetrospectiveViewModelFactory
import com.justindwinata.usahanaik.ui.screens.BusinessSetupScreen
import com.justindwinata.usahanaik.ui.screens.CategorySelectionScreen
import com.justindwinata.usahanaik.ui.screens.ContentIdeasScreen
import com.justindwinata.usahanaik.ui.screens.DashboardScreen
import com.justindwinata.usahanaik.ui.screens.SettingsScreen
import com.justindwinata.usahanaik.ui.screens.WeeklyRetrospectiveScreen
import com.justindwinata.usahanaik.ui.screens.WeeklyPlanScreen
import com.justindwinata.usahanaik.ui.screens.WelcomeScreen
import com.justindwinata.usahanaik.ui.setup.BusinessSetupViewModel
import com.justindwinata.usahanaik.ui.setup.BusinessSetupViewModelFactory
import com.justindwinata.usahanaik.ui.theme.CoralPrimary
import com.justindwinata.usahanaik.ui.theme.CreamBackground
import com.justindwinata.usahanaik.ui.theme.InkMuted
import com.justindwinata.usahanaik.ui.theme.SurfaceWarm
import com.justindwinata.usahanaik.ui.theme.UsahaNaikTheme
import com.justindwinata.usahanaik.ui.weekly.WeeklyPlanViewModel
import com.justindwinata.usahanaik.ui.weekly.WeeklyPlanViewModelFactory

@Composable
fun UsahaNaikApp() {
    UsahaNaikTheme {
        val context = LocalContext.current
        val database = remember {
            UsahaNaikDatabase.getDatabase(context)
        }
        val businessProfileRepository = remember(database) {
            LocalBusinessProfileRepository(database.businessProfileDao())
        }
        val financialEntryRepository = remember(database) {
            LocalFinancialEntryRepository(database.financialEntryDao())
        }
        val weeklyPlanRepository = remember(database) {
            LocalWeeklyPlanRepository(database.weeklyPlanDao())
        }
        val contentIdeaRepository = remember(database) {
            LocalContentIdeaRepository(database.contentIdeaDao())
        }
        val contentCalendarRepository = remember(database) {
            LocalContentCalendarRepository(database.contentCalendarDao())
        }
        val progressHistoryRepository = remember(database) {
            LocalWeeklyProgressHistoryRepository(database.weeklyProgressSnapshotDao())
        }
        val retrospectiveRepository = remember(database) {
            LocalWeeklyRetrospectiveRepository(database.weeklyRetrospectiveDao())
        }
        val contentIdeaProvider = remember {
            LocalContentIdeaProvider()
        }
        val navController = rememberNavController()
        val setupViewModel: BusinessSetupViewModel = viewModel(
            factory = BusinessSetupViewModelFactory(businessProfileRepository)
        )
        val financialEntryViewModel: FinancialEntryViewModel = viewModel(
            factory = FinancialEntryViewModelFactory(financialEntryRepository)
        )
        val dashboardInsightsViewModel: DashboardInsightsViewModel = viewModel(
            factory = DashboardInsightsViewModelFactory(
                businessProfileRepository = businessProfileRepository,
                financialEntryRepository = financialEntryRepository
            )
        )
        val weeklyPlanViewModel: WeeklyPlanViewModel = viewModel(
            factory = WeeklyPlanViewModelFactory(
                businessProfileRepository = businessProfileRepository,
                financialEntryRepository = financialEntryRepository,
                weeklyPlanRepository = weeklyPlanRepository
            )
        )
        val contentPlannerViewModel: ContentPlannerViewModel = viewModel(
            factory = ContentPlannerViewModelFactory(
                businessProfileRepository = businessProfileRepository,
                contentIdeaRepository = contentIdeaRepository,
                contentIdeaProvider = contentIdeaProvider
            )
        )
        val contentCalendarViewModel: ContentCalendarViewModel = viewModel(
            factory = ContentCalendarViewModelFactory(contentCalendarRepository)
        )
        val weeklyRetrospectiveViewModel: WeeklyRetrospectiveViewModel = viewModel(
            factory = WeeklyRetrospectiveViewModelFactory(
                businessProfileRepository = businessProfileRepository,
                financialEntryRepository = financialEntryRepository,
                weeklyPlanRepository = weeklyPlanRepository,
                contentIdeaRepository = contentIdeaRepository,
                contentCalendarRepository = contentCalendarRepository,
                progressHistoryRepository = progressHistoryRepository,
                retrospectiveRepository = retrospectiveRepository
            )
        )
        val setupState by setupViewModel.uiState.collectAsState()
        LaunchedEffect(Unit) {
            setupViewModel.loadSavedProfile()
        }
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        val showBottomBar = currentRoute != null && currentRoute !in onboardingRoutes

        Scaffold(
            containerColor = CreamBackground,
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(containerColor = SurfaceWarm) {
                        bottomTabs.forEach { tab ->
                            val selected = backStackEntry?.destination?.hierarchy
                                ?.any { it.route == tab.route.route } == true
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(tab.route.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(tab.icon, contentDescription = tab.label) },
                                label = { Text(tab.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = SurfaceWarm,
                                    selectedTextColor = CoralPrimary,
                                    indicatorColor = CoralPrimary,
                                    unselectedIconColor = InkMuted,
                                    unselectedTextColor = InkMuted
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppRoute.Welcome.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppRoute.Welcome.route) {
                    WelcomeScreen(
                        savedProfile = setupState.savedProfile,
                        onStartClick = { navController.navigate(AppRoute.CategorySelection.route) },
                        onResumeSavedProfileClick = { navController.navigate(AppRoute.Dashboard.route) },
                        onPreviewDashboardClick = { navController.navigate(AppRoute.Dashboard.route) }
                    )
                }
                composable(AppRoute.CategorySelection.route) {
                    CategorySelectionScreen(
                        onContinueClick = { categoryId ->
                            setupViewModel.selectCategory(categoryId)
                            navController.navigate(AppRoute.BusinessSetup.route)
                        }
                    )
                }
                composable(AppRoute.BusinessSetup.route) {
                    BusinessSetupScreen(
                        viewModel = setupViewModel,
                        onContinueClick = { navController.navigate(AppRoute.Dashboard.route) }
                    )
                }
                composable(AppRoute.Dashboard.route) {
                    DashboardScreen(
                        setupDraft = setupState.savedProfile?.draft ?: setupState.draft.takeIf { setupState.isValid },
                        financialEntryViewModel = financialEntryViewModel,
                        dashboardInsightsViewModel = dashboardInsightsViewModel,
                        weeklyPlanViewModel = weeklyPlanViewModel,
                        contentPlannerViewModel = contentPlannerViewModel,
                        onOpenWeeklyPlan = { navController.navigate(AppRoute.WeeklyPlan.route) },
                        onOpenContentPlanner = { navController.navigate(AppRoute.ContentIdeas.route) }
                    )
                }
                composable(AppRoute.WeeklyPlan.route) {
                    WeeklyPlanScreen(
                        viewModel = weeklyPlanViewModel,
                        onOpenRetrospective = { navController.navigate(AppRoute.Retrospective.route) }
                    )
                }
                composable(AppRoute.ContentIdeas.route) {
                    ContentIdeasScreen(
                        viewModel = contentPlannerViewModel,
                        calendarViewModel = contentCalendarViewModel
                    )
                }
                composable(AppRoute.Retrospective.route) {
                    WeeklyRetrospectiveScreen(viewModel = weeklyRetrospectiveViewModel)
                }
                composable(AppRoute.Settings.route) {
                    SettingsScreen(viewModel = setupViewModel)
                }
            }
        }
    }
}
