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
}
