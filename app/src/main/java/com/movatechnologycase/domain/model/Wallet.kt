package com.movatechnologycase.domain.model

import java.math.BigDecimal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wallet(
    val id: String,
    val currency: String,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal,
    @SerialName("created_at")
    val createdAt: String
)