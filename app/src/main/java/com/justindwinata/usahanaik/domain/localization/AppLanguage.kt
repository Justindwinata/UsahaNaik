package com.justindwinata.usahanaik.domain.localization

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String
) {
    Indonesian("id", "Indonesian", "Bahasa Indonesia"),
    English("en", "English", "English");

    companion object {
        val Default: AppLanguage = Indonesian

        fun fromCode(code: String?): AppLanguage {
            return entries.firstOrNull { it.code == code } ?: Default
        }
    }
}
