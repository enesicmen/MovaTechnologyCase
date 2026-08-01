package com.movatechnologycase.security

import android.security.keystore.KeyPermanentlyInvalidatedException
import android.util.Base64
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.nio.charset.StandardCharsets
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject

@Singleton
class SecureAuthManager @Inject constructor(
    private val cipherProvider: BiometricCipherProvider,
    private val storage: SecureCredentialStorage
) {

    suspend fun saveCredentials(
        activity: FragmentActivity,
        credentials: AuthCredentials
    ): CredentialSaveResult {
        when (checkBiometricAvailability(activity)) {
            BiometricAvailability.NOT_ENROLLED -> {
                return CredentialSaveResult.BiometricNotEnrolled
            }

            BiometricAvailability.NOT_AVAILABLE -> {
                return CredentialSaveResult.BiometricNotAvailable
            }

            BiometricAvailability.AVAILABLE -> Unit
        }

        return try {
            val cipher =
                cipherProvider.createEncryptionCipher()

            when (
                val result = authenticate(
                    activity = activity,
                    cipher = cipher,
                    title = "Protect wallet credentials",
                    subtitle = "Confirm your identity to continue"
                )
            ) {
                is PromptResult.Success -> {
                    encryptAndSave(
                        cipher = result.cipher,
                        credentials = credentials
                    )

                    CredentialSaveResult.Success
                }

                PromptResult.Cancelled -> {
                    CredentialSaveResult.Cancelled
                }

                PromptResult.TemporaryLockout -> {
                    CredentialSaveResult.TemporaryLockout
                }

                PromptResult.PermanentLockout -> {
                    CredentialSaveResult.PermanentLockout
                }

                is PromptResult.Error -> {
                    CredentialSaveResult.Error(
                        message = result.message
                    )
                }
            }
        } catch (
            exception: KeyPermanentlyInvalidatedException
        ) {
            clearInvalidatedSecurityState()
            CredentialSaveResult.KeyInvalidated
        } catch (exception: Exception) {
            CredentialSaveResult.Error(
                message = exception.message
                    ?: "Credentials could not be saved."
            )
        }
    }

    suspend fun authenticateAndLoad(
        activity: FragmentActivity
    ): AuthResult {
        when (checkBiometricAvailability(activity)) {
            BiometricAvailability.NOT_ENROLLED -> {
                return AuthResult.BiometricNotEnrolled
            }

            BiometricAvailability.NOT_AVAILABLE -> {
                return AuthResult.BiometricNotAvailable
            }

            BiometricAvailability.AVAILABLE -> Unit
        }

        val encryptedCredentials = try {
            storage.read()
        } catch (exception: Exception) {
            return AuthResult.Error(
                message = "Stored credentials could not be read."
            )
        } ?: return AuthResult.NoStoredCredentials

        return try {
            val initializationVector = Base64.decode(
                encryptedCredentials.initializationVector,
                Base64.NO_WRAP
            )

            val cipher =
                cipherProvider.createDecryptionCipher(
                    initializationVector =
                        initializationVector
                )

            when (
                val result = authenticate(
                    activity = activity,
                    cipher = cipher,
                    title = "Unlock wallet",
                    subtitle = "Confirm your identity to continue"
                )
            ) {
                is PromptResult.Success -> {
                    decryptCredentials(
                        cipher = result.cipher,
                        encryptedCredentials =
                            encryptedCredentials
                    )
                }

                PromptResult.Cancelled -> {
                    AuthResult.Cancelled
                }

                PromptResult.TemporaryLockout -> {
                    AuthResult.TemporaryLockout
                }

                PromptResult.PermanentLockout -> {
                    AuthResult.PermanentLockout
                }

                is PromptResult.Error -> {
                    AuthResult.Error(
                        message = result.message
                    )
                }
            }
        } catch (
            exception: KeyPermanentlyInvalidatedException
        ) {
            clearInvalidatedSecurityState()
            AuthResult.KeyInvalidated
        } catch (exception: AEADBadTagException) {
            clearInvalidatedSecurityState()
            AuthResult.StorageCorrupted
        } catch (exception: IllegalArgumentException) {
            clearInvalidatedSecurityState()
            AuthResult.StorageCorrupted
        } catch (exception: Exception) {
            AuthResult.Error(
                message = exception.message
                    ?: "Authentication could not be completed."
            )
        }
    }

    suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            val keyResult = runCatching {
                cipherProvider.deleteKey()
            }

            val storageResult = runCatching {
                storage.clear()
            }

            when {
                keyResult.isFailure -> {
                    Result.failure(
                        keyResult.exceptionOrNull()
                            ?: IllegalStateException(
                                "Keystore key could not be deleted."
                            )
                    )
                }

                storageResult.isFailure -> {
                    Result.failure(
                        storageResult.exceptionOrNull()
                            ?: IllegalStateException(
                                "Stored credentials could not be cleared."
                            )
                    )
                }

                else -> Result.success(Unit)
            }
        }
    }

    private suspend fun encryptAndSave(
        cipher: Cipher,
        credentials: AuthCredentials
    ) {
        val plainText = JSONObject()
            .put(TOKEN_KEY, credentials.token)
            .put(PIN_KEY, credentials.walletPin)
            .toString()
            .toByteArray(StandardCharsets.UTF_8)

        try {
            val encryptedBytes =
                cipher.doFinal(plainText)

            storage.save(
                EncryptedCredentials(
                    cipherText = Base64.encodeToString(
                        encryptedBytes,
                        Base64.NO_WRAP
                    ),
                    initializationVector =
                        Base64.encodeToString(
                            cipher.iv,
                            Base64.NO_WRAP
                        )
                )
            )
        } finally {
            plainText.fill(0)
        }
    }

    private suspend fun decryptCredentials(
        cipher: Cipher,
        encryptedCredentials: EncryptedCredentials
    ): AuthResult {
        val encryptedBytes = Base64.decode(
            encryptedCredentials.cipherText,
            Base64.NO_WRAP
        )

        val plainText =
            cipher.doFinal(encryptedBytes)

        return try {
            val jsonObject = JSONObject(
                String(
                    plainText,
                    StandardCharsets.UTF_8
                )
            )

            val token =
                jsonObject.getString(TOKEN_KEY)

            val walletPin =
                jsonObject.getString(PIN_KEY)

            if (
                token.isBlank() ||
                walletPin.isBlank()
            ) {
                AuthResult.StorageCorrupted
            } else {
                AuthResult.Success(
                    credentials = AuthCredentials(
                        token = token,
                        walletPin = walletPin
                    )
                )
            }
        } finally {
            plainText.fill(0)
            encryptedBytes.fill(0)
        }
    }

    private suspend fun authenticate(
        activity: FragmentActivity,
        cipher: Cipher,
        title: String,
        subtitle: String
    ): PromptResult {
        return withContext(Dispatchers.Main.immediate) {
            suspendCancellableCoroutine { continuation ->
                val executor =
                    ContextCompat.getMainExecutor(activity)

                val biometricPrompt = BiometricPrompt(
                    activity,
                    executor,
                    object :
                        BiometricPrompt.AuthenticationCallback() {

                        override fun onAuthenticationSucceeded(
                            result:
                            BiometricPrompt.AuthenticationResult
                        ) {
                            val authenticatedCipher =
                                result.cryptoObject?.cipher

                            if (!continuation.isActive) {
                                return
                            }

                            if (authenticatedCipher == null) {
                                continuation.resume(
                                    PromptResult.Error(
                                        message =
                                            "CryptoObject was not returned."
                                    )
                                )
                            } else {
                                continuation.resume(
                                    PromptResult.Success(
                                        cipher =
                                            authenticatedCipher
                                    )
                                )
                            }
                        }

                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            if (!continuation.isActive) {
                                return
                            }

                            continuation.resume(
                                mapAuthenticationError(
                                    errorCode = errorCode,
                                    message =
                                        errString.toString()
                                )
                            )
                        }

                        override fun onAuthenticationFailed() {
                            // Prompt remains open so the user
                            // can try again.
                        }
                    }
                )

                continuation.invokeOnCancellation {
                    biometricPrompt.cancelAuthentication()
                }

                val promptInfo =
                    BiometricPrompt.PromptInfo.Builder()
                        .setTitle(title)
                        .setSubtitle(subtitle)
                        .setAllowedAuthenticators(
                            BiometricManager.Authenticators
                                .BIOMETRIC_STRONG
                        )
                        .setNegativeButtonText("Cancel")
                        .build()

                biometricPrompt.authenticate(
                    promptInfo,
                    BiometricPrompt.CryptoObject(cipher)
                )
            }
        }
    }

    private fun checkBiometricAvailability(
        activity: FragmentActivity
    ): BiometricAvailability {
        return when (
            BiometricManager
                .from(activity)
                .canAuthenticate(
                    BiometricManager.Authenticators
                        .BIOMETRIC_STRONG
                )
        ) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                BiometricAvailability.AVAILABLE
            }

            BiometricManager
                .BIOMETRIC_ERROR_NONE_ENROLLED -> {
                BiometricAvailability.NOT_ENROLLED
            }

            else -> {
                BiometricAvailability.NOT_AVAILABLE
            }
        }
    }

    private fun mapAuthenticationError(
        errorCode: Int,
        message: String
    ): PromptResult {
        return when (errorCode) {
            BiometricPrompt.ERROR_NEGATIVE_BUTTON,
            BiometricPrompt.ERROR_USER_CANCELED,
            BiometricPrompt.ERROR_CANCELED -> {
                PromptResult.Cancelled
            }

            BiometricPrompt.ERROR_LOCKOUT -> {
                PromptResult.TemporaryLockout
            }

            BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> {
                PromptResult.PermanentLockout
            }

            else -> {
                PromptResult.Error(message)
            }
        }
    }

    private suspend fun clearInvalidatedSecurityState() {
        withContext(Dispatchers.IO) {
            runCatching {
                cipherProvider.deleteKey()
            }

            runCatching {
                storage.clear()
            }
        }
    }

    private enum class BiometricAvailability {
        AVAILABLE,
        NOT_ENROLLED,
        NOT_AVAILABLE
    }

    private sealed interface PromptResult {

        data class Success(
            val cipher: Cipher
        ) : PromptResult

        data object Cancelled : PromptResult

        data object TemporaryLockout : PromptResult

        data object PermanentLockout : PromptResult

        data class Error(
            val message: String
        ) : PromptResult
    }

    private companion object {
        const val TOKEN_KEY = "token"
        const val PIN_KEY = "wallet_pin"
    }
}