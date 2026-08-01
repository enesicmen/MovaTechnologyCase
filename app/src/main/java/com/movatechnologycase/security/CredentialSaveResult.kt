package com.movatechnologycase.security

sealed interface CredentialSaveResult {

    data object Success : CredentialSaveResult

    data object Cancelled : CredentialSaveResult

    data object TemporaryLockout : CredentialSaveResult

    data object PermanentLockout : CredentialSaveResult

    data object BiometricNotAvailable : CredentialSaveResult

    data object BiometricNotEnrolled : CredentialSaveResult

    data object KeyInvalidated : CredentialSaveResult

    data class Error(
        val message: String
    ) : CredentialSaveResult
}