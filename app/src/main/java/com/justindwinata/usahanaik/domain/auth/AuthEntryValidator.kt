package com.justindwinata.usahanaik.domain.auth

data class AuthEntryValidationResult(
    val isValid: Boolean,
    val message: String? = null
)

object AuthEntryValidator {
    fun validateLogin(email: String, password: String): AuthEntryValidationResult {
        return when {
            email.isBlank() -> AuthEntryValidationResult(false, "Email is required.")
            !email.contains("@") -> AuthEntryValidationResult(false, "Enter a valid email address.")
            password.isBlank() -> AuthEntryValidationResult(false, "Password is required.")
            else -> AuthEntryValidationResult(true)
        }
    }

    fun validateRegister(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): AuthEntryValidationResult {
        return when {
            name.isBlank() -> AuthEntryValidationResult(false, "Name is required.")
            email.isBlank() -> AuthEntryValidationResult(false, "Email is required.")
            !email.contains("@") -> AuthEntryValidationResult(false, "Enter a valid email address.")
            password.isBlank() -> AuthEntryValidationResult(false, "Password is required.")
            confirmPassword != password -> AuthEntryValidationResult(false, "Passwords do not match.")
            else -> AuthEntryValidationResult(true)
        }
    }
}
