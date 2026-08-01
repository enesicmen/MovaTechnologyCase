package com.movatechnologycase.security

interface SecureCredentialStorage {

    suspend fun save(credentials: EncryptedCredentials)

    suspend fun read(): EncryptedCredentials?

    suspend fun clear()
}