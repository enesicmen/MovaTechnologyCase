package com.movatechnologycase.domain.model

data class WalletDashboard(
    val wallet: Wallet,
    val children: List<Child>,
    val recentTransactions: List<Transaction>
)