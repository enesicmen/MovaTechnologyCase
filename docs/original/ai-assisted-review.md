# AI-Assisted Security Review

## AI Response

### 1. Android Keystore is not used

**Location:** Entire class

The implementation does not create or use cryptographic key material from Android
Keystore.

Sensitive credentials are therefore stored without an operating-system-protected
encryption key.

A Keystore-backed AES/GCM key should be created with user authentication required.
The key should be used to encrypt and decrypt sensitive credentials.

---

### 2. BiometricPrompt is not connected to cryptography

**Location:** `authenticateWithBiometrics()`

The application calls:

```kotlin
prompt.authenticate(promptInfo)
```

without providing a `BiometricPrompt.CryptoObject`.

The biometric prompt therefore acts only as a Boolean UI gate. Successful biometric
authentication does not cryptographically unlock the stored credentials.

A Keystore-backed `Cipher` should be wrapped in a `CryptoObject` and passed to
`BiometricPrompt.authenticate()`.

---

### 3. loadCredentials() bypasses biometric authentication

**Location:** `loadCredentials()`

The method reads the token and PIN directly from `SharedPreferences` without first
requiring biometric authentication.

Any component with access to the `AuthManager` instance can call this method and
load credentials into memory.

Credential decryption should only occur after a successful biometric operation using
the authenticated `CryptoObject`.

---

### 4. Weak biometric authentication is accepted

**Location:** `setAllowedAuthenticators()`

The prompt accepts:

```kotlin
BIOMETRIC_WEAK or DEVICE_CREDENTIAL
```

This does not satisfy the requirement for a biometric-bound key protected with
`BIOMETRIC_STRONG`.

Sensitive wallet credentials should require `BIOMETRIC_STRONG`, matching the
authentication requirements configured on the Keystore key.

---

### 5. Biometric availability is not checked

**Location:** Before `authenticateWithBiometrics()`

The implementation does not call `BiometricManager.canAuthenticate()` before
displaying the prompt.

It cannot distinguish between unsupported biometric hardware, temporarily unavailable
hardware, no enrolled biometric, or strong biometrics not being supported.

The application should check `BIOMETRIC_STRONG` availability and return a typed
result before displaying the prompt.

---

### 6. Biometric enrollment changes do not invalidate access

**Location:** Cryptographic key configuration is missing

No biometric-bound Keystore key exists, and there is no configuration that
invalidates the key when a new fingerprint or face is enrolled.

For a fintech wallet, enrollment changes should invalidate the existing key and
require the user to authenticate again using the primary login flow.

The Keystore key should be configured with biometric enrollment invalidation.

---

### 7. The completion callback runs on a background executor

**Location:** `authenticateWithBiometrics()`

`BiometricPrompt` receives an executor created by
`Executors.newSingleThreadExecutor()`.

The completion callback therefore executes on that background thread. A caller that
directly updates UI state could cause threading errors.

The callback should be dispatched using the main executor or clearly exposed as a
background-thread callback.

---

### 8. Singleton initialization is not fully thread-safe

**Location:** `getInstance()`

The instance property is marked `@Volatile`, but creation is not synchronized:

```kotlin
return instance ?: AuthManager(context).also { instance = it }
```

Two threads calling the method simultaneously could construct separate instances.

This may cause inconsistent in-memory credential state. Dependency injection should
own the class lifecycle, or synchronized double-checked locking should be used.

---

### 9. The commit result is ignored

**Location:** `saveUserCredentials()`

`SharedPreferences.commit()` returns whether the write succeeded, but the result is
ignored.

The implementation updates `currentToken` and `walletPin` even when persistent
storage may have failed.

The storage result should be checked before updating in-memory authentication state,
and failures should be returned as a typed error.

---

### 10. Failed credential loading can leave stale values in memory

**Location:** `loadCredentials()`

When either the token or PIN is missing, the method returns `false` but does not clear
existing values from `currentToken` and `walletPin`.

A credential from an earlier session may therefore remain in memory.

A failed or inconsistent read should clear in-memory state and remove corrupted or
partial stored data.
