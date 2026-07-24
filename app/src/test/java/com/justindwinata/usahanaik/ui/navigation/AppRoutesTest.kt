package com.justindwinata.usahanaik.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppRoutesTest {
    @Test
    fun authRoutesAreOnboardingRoutes() {
        assertTrue(AppRoute.Login.route in onboardingRoutes)
        assertTrue(AppRoute.Register.route in onboardingRoutes)
    }

    @Test
    fun bottomNavigationKeepsMainProductWorkflowReachable() {
        val routes = bottomTabs.map { it.route }

        assertEquals(
            listOf(
                AppRoute.Dashboard,
                AppRoute.WeeklyPlan,
                AppRoute.ContentIdeas,
                AppRoute.BusinessReport,
                AppRoute.Settings
            ),
            routes
        )
    }

    @Test
    fun bottomBarIsHiddenOnlyForAuthAndSetupRoutes() {
        assertTrue(!shouldShowBottomBar(AppRoute.Welcome.route))
        assertTrue(!shouldShowBottomBar(AppRoute.Login.route))
        assertTrue(!shouldShowBottomBar(AppRoute.Register.route))
        assertTrue(!shouldShowBottomBar(AppRoute.CategorySelection.route))
        assertTrue(!shouldShowBottomBar(AppRoute.BusinessSetup.route))
        assertTrue(shouldShowBottomBar(AppRoute.Dashboard.route))
        assertTrue(shouldShowBottomBar(AppRoute.Retrospective.route))
        assertTrue(!shouldShowBottomBar(null))
    }

    @Test
    fun localModeRoutesToSetupWithoutProfileAndDashboardWithProfile() {
        assertEquals(AppRoute.CategorySelection.route, localModeDestinationRoute(hasSavedProfile = false))
        assertEquals(AppRoute.Dashboard.route, localModeDestinationRoute(hasSavedProfile = true))
    }
}
