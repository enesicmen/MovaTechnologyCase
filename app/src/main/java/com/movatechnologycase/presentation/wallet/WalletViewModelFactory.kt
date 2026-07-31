package com.movatechnologycase.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movatechnologycase.domain.repository.WalletRepository

class WalletViewModelFactory(
    private val repository: WalletRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (
            modelClass.isAssignableFrom(
                WalletDashboardViewModel::class.java
            )
        ) {
            return WalletDashboardViewModel(
                repository = repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}