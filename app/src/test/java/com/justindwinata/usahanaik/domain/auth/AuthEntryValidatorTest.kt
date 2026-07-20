package com.justindwinata.usahanaik.domain.auth

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthEntryValidatorTest {
    @Test
    fun loginRequiresEmail() {
        val result = AuthEntryValidator.validateLogin("", "password")

        assertFalse(result.isValid)
    }

    @Test
    fun loginAllowsValidPlaceholderInput() {
        val result = AuthEntryValidator.validateLogin("owner@example.com", "password")

        assertTrue(result.isValid)
    }

    @Test
    fun registerRequiresMatchingPasswords() {
        val result = AuthEntryValidator.validateRegister(
            name = "Owner",
            email = "owner@example.com",
            password = "password",
            confirmPassword = "different"
        )

        assertFalse(result.isValid)
    }

    @Test
    fun registerAllowsValidPlaceholderInput() {
        val result = AuthEntryValidator.validateRegister(
            name = "Owner",
            email = "owner@example.com",
            password = "password",
            confirmPassword = "password"
        )

        assertTrue(result.isValid)
    }
}
