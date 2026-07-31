package com.movatechnologycase.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletResponseDto(
    val wallet: WalletDto,
    val children: List<ChildDto>,
    @SerialName("recent_transactions")
    val recentTransactions: List<TransactionDto>
)