package com.movatechnologycase.domain.model

import java.math.BigDecimal

data class Child(
    val id: String,
    val fullName: String,
    val avatarUrl: String?,
    val age: Int,
    val grade: String,
    val walletBalance: BigDecimal,
    val schoolName: String
)