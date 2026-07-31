package com.movatechnologycase.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletDto(
    val id: String,
    val currency: String,
    val balance: Double,
    @SerialName("created_at")
    val createdAt: String
)