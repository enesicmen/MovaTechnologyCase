package com.movatechnologycase.core.formatter

import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale
import java.util.TimeZone

object AppFormatter {

    private val turkishLocale = Locale.forLanguageTag("tr-TR")

    fun formatMoney(
        amount: BigDecimal,
        currencyCode: String
    ): String {
        return runCatching {
            NumberFormat
                .getCurrencyInstance(turkishLocale)
                .apply { currency = Currency.getInstance(currencyCode) }
                .format(amount)
        }.getOrElse { "$amount $currencyCode" }
    }

    fun formatDate(value: String): String {
        val supportedPatterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSX",
            "yyyy-MM-dd'T'HH:mm:ssX"
        )

        val parsedDate = supportedPatterns
            .firstNotNullOfOrNull { pattern ->
                runCatching {
                    SimpleDateFormat(
                        pattern,
                        Locale.US
                    ).apply { timeZone = TimeZone.getTimeZone("UTC") }
                        .parse(value)
                }.getOrNull()
            }
            ?: return value

        return runCatching {
            SimpleDateFormat(
                "dd MMM, HH:mm",
                turkishLocale
            ).format(parsedDate)
        }.getOrDefault(value)
    }
}