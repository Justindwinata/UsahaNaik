package com.justindwinata.usahanaik.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppRoute(val route: String) {
    data object Welcome : AppRoute("welcome")
    data object CategorySelection : AppRoute("category_selection")
    data object BusinessSetup : AppRoute("business_setup")
    data object Dashboard : AppRoute("dashboard")
    data object WeeklyPlan : AppRoute("weekly_plan")
    data object ContentIdeas : AppRoute("content_ideas")
    data object Retrospective : AppRoute("retrospective")
    data object BusinessReport : AppRoute("business_report")
    data object Settings : AppRoute("settings")
}

data class BottomTab(
    val route: AppRoute,
    val label: String,
    val icon: ImageVector
)

val bottomTabs = listOf(
    BottomTab(AppRoute.Dashboard, "Dashboard", Icons.Rounded.Dashboard),
    BottomTab(AppRoute.WeeklyPlan, "Plan", Icons.Rounded.TaskAlt),
    BottomTab(AppRoute.ContentIdeas, "Ideas", Icons.Rounded.AutoAwesome),
    BottomTab(AppRoute.Settings, "Profile", Icons.Rounded.Person)
)

val onboardingRoutes = setOf(
    AppRoute.Welcome.route,
    AppRoute.CategorySelection.route,
    AppRoute.BusinessSetup.route
)
