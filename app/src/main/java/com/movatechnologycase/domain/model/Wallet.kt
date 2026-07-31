package com.movatechnologycase.domain.model

import java.math.BigDecimal

data class Wallet(
    val id: String,
    val currency: String,
    val balance: BigDecimal,
    val createdAt: String
)