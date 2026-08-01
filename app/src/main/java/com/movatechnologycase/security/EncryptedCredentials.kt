package com.movatechnologycase.security

data class AuthCredentials(
    val token: String,
    val walletPin: String
)

data class EncryptedCredentials(
    val cipherText: String,
    val initializationVector: String
)