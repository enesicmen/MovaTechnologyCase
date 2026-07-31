package com.movatechnologycase.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChildDto(
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
    val walletBalance: Double,
    @SerialName("school_name")
    val schoolName: String
)
