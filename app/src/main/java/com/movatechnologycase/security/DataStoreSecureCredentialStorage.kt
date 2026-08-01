package com.movatechnologycase.security

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class DataStoreSecureCredentialStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SecureCredentialStorage {

    override suspend fun save(
        credentials: EncryptedCredentials
    ) {
        dataStore.edit { preferences ->
            preferences[CIPHER_TEXT_KEY] =
                credentials.cipherText

            preferences[INITIALIZATION_VECTOR_KEY] =
                credentials.initializationVector
        }
    }

    override suspend fun read(): EncryptedCredentials? {
        val preferences = dataStore.data.first()

        val cipherText =
            preferences[CIPHER_TEXT_KEY]

        val initializationVector =
            preferences[INITIALIZATION_VECTOR_KEY]

        if (
            cipherText.isNullOrBlank() ||
            initializationVector.isNullOrBlank()
        ) {
            return null
        }

        return EncryptedCredentials(
            cipherText = cipherText,
            initializationVector =
                initializationVector
        )
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private companion object {

        val CIPHER_TEXT_KEY =
            stringPreferencesKey(
                name = "credentials_cipher_text"
            )

        val INITIALIZATION_VECTOR_KEY =
            stringPreferencesKey(
                name = "credentials_initialization_vector"
            )
    }
}