package com.movatechnologycase.di

import com.movatechnologycase.data.repository.MockWalletRepository
import com.movatechnologycase.domain.repository.WalletRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        repository: MockWalletRepository
    ): WalletRepository
}