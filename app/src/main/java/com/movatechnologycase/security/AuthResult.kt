package com.movatechnologycase.security

sealed interface AuthResult {

    data class Success(val credentials: AuthCredentials) : AuthResult

    data object Cancelled : AuthResult

    data object TemporaryLockout : AuthResult

    data object PermanentLockout : AuthResult

    data object BiometricNotAvailable : AuthResult

    data object BiometricNotEnrolled : AuthResult

    data object KeyInvalidated : AuthResult

    data object NoStoredCredentials : AuthResult

    data object StorageCorrupted : AuthResult

    data class Error(val message: String) : AuthResult
}