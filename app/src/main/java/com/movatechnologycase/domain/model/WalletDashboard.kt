package com.movatechnologycase.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalletDashboard(
    val wallet: Wallet,
    val children: List<Child>,
    @SerialName("recent_transactions")
    val recentTransactions: List<Transaction>
)