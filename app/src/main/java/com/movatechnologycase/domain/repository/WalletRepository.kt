package com.movatechnologycase.domain.repository

import com.movatechnologycase.domain.model.WalletDashboard

interface WalletRepository {
    suspend fun getDashboard(): WalletDashboard
}