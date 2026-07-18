package com.justindwinata.usahanaik.domain.ai

enum class AiProviderMode(val label: String) {
    LocalOnly("Local only"),
    AiAssistedIfConfigured("AI-assisted if configured")
}

data class AiContentSettings(
    val providerMode: AiProviderMode = AiProviderMode.LocalOnly,
    val providerName: String = "Configurable provider",
    val apiKey: String? = null,
    val timeoutMillis: Long = 15_000L
) {
    val isAiConfigured: Boolean
        get() = providerMode == AiProviderMode.AiAssistedIfConfigured && !apiKey.isNullOrBlank()

    val maskedApiKey: String
        get() = apiKey?.takeIf { it.isNotBlank() }?.let { "****${it.takeLast(4)}" } ?: ""
}
