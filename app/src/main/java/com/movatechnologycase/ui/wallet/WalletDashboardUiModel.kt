package com.movatechnologycase.ui.wallet

import com.movatechnologycase.core.formatter.AppFormatter
import com.movatechnologycase.domain.model.TransactionType
import com.movatechnologycase.domain.model.WalletDashboard

data class WalletDashboardUiModel(
    val balanceText: String,
    val currencyCode: String,
    val children: List<ChildWalletUiModel>,
    val transactions: List<WalletTransactionUiModel>
)

data class ChildWalletUiModel(
    val id: String,
    val name: String,
    val grade: String,
    val balanceText: String
)

data class WalletTransactionUiModel(
    val id: String,
    val title: String,
    val dateText: String,
    val amountText: String,
    val isIncome: Boolean
)

internal fun WalletDashboard.toUiModel(): WalletDashboardUiModel {
    return WalletDashboardUiModel(
        balanceText = AppFormatter.formatMoney(
            amount = wallet.balance,
            currencyCode = wallet.currency
        ),
        currencyCode = wallet.currency,
        children = children.map { child ->
            ChildWalletUiModel(
                id = child.id,
                name = child.fullName,
                grade = child.grade,
                balanceText = AppFormatter.formatMoney(
                    amount = child.walletBalance,
                    currencyCode = wallet.currency
                )
            )
        },
        transactions = recentTransactions.map { transaction ->
            val isIncome =
                transaction.type == TransactionType.INCOME

            val formattedAmount =
                AppFormatter.formatMoney(
                    amount = transaction.amount.abs(),
                    currencyCode = transaction.currency
                )

            WalletTransactionUiModel(
                id = transaction.id,
                title = transaction.description,
                dateText = AppFormatter.formatDate(
                    transaction.date
                ),
                amountText = if (isIncome) {
                    "+$formattedAmount"
                } else {
                    "-$formattedAmount"
                },
                isIncome = isIncome
            )
        }
    )
}