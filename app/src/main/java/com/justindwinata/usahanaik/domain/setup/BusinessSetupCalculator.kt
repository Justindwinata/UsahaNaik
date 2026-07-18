package com.justindwinata.usahanaik.domain.setup

import com.justindwinata.usahanaik.domain.model.BusinessSetupDraft
import kotlin.math.abs

object BusinessSetupCalculator {
    fun parseIndonesianNumber(input: String): Long? {
        val normalized = input
            .trim()
            .lowercase()
            .replace("rp", "")
            .replace("idr", "")
            .replace(" ", "")

        if (normalized.isBlank()) return null
        if (normalized.startsWith("-")) return null

        val multiplier = when {
            normalized.endsWith("juta") || normalized.endsWith("jt") -> 1_000_000L
            normalized.endsWith("ribu") || normalized.endsWith("rb") -> 1_000L
            else -> 1L
        }

        val numericPart = normalized
            .removeSuffix("juta")
            .removeSuffix("jt")
            .removeSuffix("ribu")
            .removeSuffix("rb")

        if (numericPart.isBlank()) return null

        val decimalMode = multiplier > 1L && numericPart.contains(",")
        val sanitized = if (decimalMode) {
            numericPart.replace(".", "").replace(",", ".")
        } else {
            numericPart.replace(".", "").replace(",", "")
        }

        val value = sanitized.toDoubleOrNull() ?: return null
        if (value < 0) return null

        return (value * multiplier).toLong()
    }

    fun profitMarginPercent(draft: BusinessSetupDraft): Int? {
        val revenue = parseIndonesianNumber(draft.monthlyRevenue) ?: return null
        val profit = parseIndonesianNumber(draft.estimatedMonthlyProfit) ?: return null
        if (revenue == 0L) return null
        return ((profit.toDouble() / revenue.toDouble()) * 100).toInt()
    }

    fun revenueTargetGap(draft: BusinessSetupDraft): Long? {
        val revenue = parseIndonesianNumber(draft.monthlyRevenue) ?: return null
        val target = parseIndonesianNumber(draft.targetMonthlyRevenue) ?: return null
        return target - revenue
    }

    fun profitTargetGap(draft: BusinessSetupDraft): Long? {
        val profit = parseIndonesianNumber(draft.estimatedMonthlyProfit) ?: return null
        val target = parseIndonesianNumber(draft.targetMonthlyProfit) ?: return null
        return target - profit
    }

    fun formatRupiah(value: Long?): String {
        if (value == null) return "-"
        val prefix = if (value < 0) "-Rp" else "Rp"
        val digits = abs(value).toString().reversed().chunked(3).joinToString(".").reversed()
        return "$prefix$digits"
    }
}
