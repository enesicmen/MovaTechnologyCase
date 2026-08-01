package com.movatechnologycase.di

import com.movatechnologycase.security.DataStoreSecureCredentialStorage
import com.movatechnologycase.security.SecureCredentialStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {

    @Binds
    @Singleton
    abstract fun bindSecureCredentialStorage(
        implementation:
        DataStoreSecureCredentialStorage
    ): SecureCredentialStorage
}