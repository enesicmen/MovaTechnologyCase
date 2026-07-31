package com.movatechnologycase.domain.model

import java.math.BigDecimal

data class Transaction(
    val id: String,
    val type: TransactionType,
    val category: String,
    val description: String,
    val amount: BigDecimal,
    val currency: String,
    val date: String,
    val childId: String?,
    val status: String
)