package com.movatechnologycase.security

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiometricCipherProvider @Inject constructor() {

    private val keyStore: KeyStore
        get() = KeyStore
            .getInstance(ANDROID_KEYSTORE)
            .apply {
                load(null)
            }

    fun createEncryptionCipher(): Cipher {
        return createCipher().apply {
            init(
                Cipher.ENCRYPT_MODE,
                getOrCreateSecretKey()
            )
        }
    }

    fun createDecryptionCipher(
        initializationVector: ByteArray
    ): Cipher {
        return createCipher().apply {
            init(
                Cipher.DECRYPT_MODE,
                getOrCreateSecretKey(),
                GCMParameterSpec(
                    GCM_TAG_LENGTH_BITS,
                    initializationVector
                )
            )
        }
    }

    fun deleteKey() {
        val store = keyStore

        if (store.containsAlias(KEY_ALIAS)) {
            store.deleteEntry(KEY_ALIAS)
        }
    }

    private fun getOrCreateSecretKey(): SecretKey {
        val existingKey = keyStore
            .getKey(KEY_ALIAS, null) as? SecretKey

        return existingKey ?: createSecretKey()
    }

    private fun createSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )

        val builder = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or
                    KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(
                KeyProperties.BLOCK_MODE_GCM
            )
            .setEncryptionPaddings(
                KeyProperties.ENCRYPTION_PADDING_NONE
            )
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            builder.setUserAuthenticationParameters(
                AUTH_PER_USE_TIMEOUT_SECONDS,
                KeyProperties.AUTH_BIOMETRIC_STRONG
            )
        } else {
            @Suppress("DEPRECATION")
            builder.setUserAuthenticationValidityDurationSeconds(
                AUTH_PER_USE_LEGACY_DURATION_SECONDS
            )
        }

        keyGenerator.init(builder.build())

        return keyGenerator.generateKey()
    }

    private fun createCipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION)
    }

    private companion object {

        const val ANDROID_KEYSTORE =
            "AndroidKeyStore"

        const val KEY_ALIAS =
            "wallet_biometric_credentials_key"

        const val TRANSFORMATION =
            "AES/GCM/NoPadding"

        const val GCM_TAG_LENGTH_BITS = 128

        const val AUTH_PER_USE_TIMEOUT_SECONDS = 0

        const val AUTH_PER_USE_LEGACY_DURATION_SECONDS = -1
    }
}