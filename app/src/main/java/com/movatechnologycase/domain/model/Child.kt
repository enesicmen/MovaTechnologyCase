package com.movatechnologycase.domain.model

import java.math.BigDecimal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Child(
    val id: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    val age: Int,
    val grade: String,
    @SerialName("wallet_balance")
    @Serializable(with = BigDecimalSerializer::class)
    val walletBalance: BigDecimal,
    @SerialName("school_name")
    val schoolName: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}