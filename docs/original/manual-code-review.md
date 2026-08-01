# Manual Security Review

## 1. Credentials are stored without encryption

**Location:** `saveUserCredentials()`

### Problem

The authentication token and wallet PIN are stored directly in standard
`SharedPreferences` without encryption. The raw wallet PIN is persisted as a
recoverable value.

### Fintech impact

An attacker who gains access to application storage or an insecure device backup
could obtain sensitive wallet credentials. This may lead to unauthorized account
or wallet access.

### Recommendation

Sensitive values should be encrypted using key material protected by Android
Keystore. The raw wallet PIN should not be persisted unless recoverable storage is
an explicit product requirement.

---

## 2. Sensitive credentials are written to logs

**Location:** `saveUserCredentials()`

### Problem

The authentication token and wallet PIN are written directly to Logcat:

```kotlin
Log.d("AuthManager", "Saved credentials token=$token pin=$pin")
```

### Fintech impact

Credentials could be exposed through debugging tools, connected devices, crash
reporting systems, or collected application logs.

### Recommendation

Authentication tokens, PIN values, and decrypted sensitive information must never
be included in application logs.

---

## 3. Logout does not clear persisted credentials

**Location:** `logout()`

### Problem

The method only clears the in-memory properties:

```kotlin
currentToken = null
walletPin = null
```

The values stored inside `SharedPreferences` remain available.

### Fintech impact

Calling `loadCredentials()` after logout can restore the previous session. Logout
therefore does not actually remove the user's credentials from the device.

### Recommendation

Logout should delete every persisted credential and all related encrypted values,
IV values, and session information.

---

## 4. Executor lifecycle is not managed

**Location:** `authenticateWithBiometrics()`

### Problem

A new single-thread executor is created for every biometric authentication attempt:

```kotlin
Executors.newSingleThreadExecutor()
```

The executor is never shut down or reused.

### Fintech impact

Repeated authentication attempts may leak thread resources and create unnecessary
background threads during the application's lifetime.

### Recommendation

Use `ContextCompat.getMainExecutor(activity)` when appropriate, or explicitly manage
the ownership and lifecycle of the executor.

---

## 5. Authentication results are not represented clearly

**Location:** `BiometricPrompt.AuthenticationCallback`

### Problem

The callback converts authentication outcomes into a simple Boolean. Cancellation,
temporary lockout, permanent lockout, missing enrollment, and other errors cannot
be distinguished.

### Fintech impact

The application cannot provide safe recovery behavior or an accurate user message
for different security-related failures.

### Recommendation

Return a typed authentication result instead of a Boolean, for example:

- `Success`
- `Cancelled`
- `Failed`
- `TemporaryLockout`
- `PermanentLockout`
- `NotAvailable`

---

## 6. Sensitive values are publicly mutable

**Location:** `currentToken` and `walletPin`

### Problem

Both properties are publicly readable and writable:

```kotlin
var currentToken: String? = null
var walletPin: String? = null
```

They also remain in application memory after being loaded.

### Fintech impact

Other application components may accidentally read, modify, or retain sensitive
credentials.

### Recommendation

Sensitive state should be private. Decrypted credentials should only be exposed when
required and should remain in memory for the shortest possible time.

---

## 7. SharedPreferences commit blocks the calling thread

**Location:** `saveUserCredentials()`

### Problem

The implementation uses synchronous `commit()` for disk storage.

### Fintech impact

If this method is called from the main thread, disk I/O may block the user interface.
The return value of `commit()` is also not checked, so the application may update its
in-memory state even if persistence fails.

### Recommendation

Use an asynchronous storage API or perform storage operations outside the main
thread. Storage failures should be represented explicitly.
