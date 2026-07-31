package com.movatechnologycase.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: String,
    val type: String,
    val category: String,
    val description: String,
    val amount: Double,
    val currency: String,
    val date: String,
    @SerialName("child_id")
    val childId: String?,
    val status: String
)