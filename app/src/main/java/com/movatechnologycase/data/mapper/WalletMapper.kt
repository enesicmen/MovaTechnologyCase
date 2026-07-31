package com.movatechnologycase.data.mapper

import com.movatechnologycase.data.dto.ChildDto
import com.movatechnologycase.data.dto.TransactionDto
import com.movatechnologycase.data.dto.WalletDto
import com.movatechnologycase.data.dto.WalletResponseDto
import com.movatechnologycase.domain.model.Child
import com.movatechnologycase.domain.model.Transaction
import com.movatechnologycase.domain.model.TransactionType
import com.movatechnologycase.domain.model.Wallet
import com.movatechnologycase.domain.model.WalletDashboard
import java.math.BigDecimal

fun WalletResponseDto.toDomain(): WalletDashboard {
    return WalletDashboard(
        wallet = wallet.toDomain(),
        children = children.map(ChildDto::toDomain),
        recentTransactions = recentTransactions.map(TransactionDto::toDomain)
    )
}

private fun WalletDto.toDomain(): Wallet {
    return Wallet(
        id = id,
        currency = currency,
        balance = BigDecimal.valueOf(balance),
        createdAt = createdAt
    )
}

private fun ChildDto.toDomain(): Child {
    return Child(
        id = id,
        fullName = "$firstName $lastName",
        avatarUrl = avatarUrl,
        age = age,
        grade = grade,
        walletBalance = BigDecimal.valueOf(walletBalance),
        schoolName = schoolName
    )
}

private fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        id = id,
        type = when (type.lowercase()) {
            "income" -> TransactionType.INCOME
            "expense" -> TransactionType.EXPENSE
            else -> TransactionType.UNKNOWN
        },
        category = category,
        description = description,
        amount = BigDecimal.valueOf(amount),
        currency = currency,
        date = date,
        childId = childId,
        status = status
    )
}