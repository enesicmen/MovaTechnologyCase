package com.movatechnologycase.domain.model

import java.math.BigDecimal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String,
    val type: TransactionType,
    val category: String,
    val description: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val currency: String,
    val date: String,
    @SerialName("child_id")
    val childId: String?,
    val status: String
)