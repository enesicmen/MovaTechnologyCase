# Mova Technology Android Case Study

A single-screen Digital Wallet Dashboard application built with Kotlin and Jetpack Compose.

The project demonstrates:

- Wallet Dashboard UI
- Loaded, Empty, and Error states
- Retry flow
- MVVM architecture
- StateFlow-based state management
- Hilt / Dagger dependency injection
- Mock JSON data loaded from assets
- Secure credential storage with Android Keystore and DataStore
- BiometricPrompt, BIOMETRIC_STRONG, and CryptoObject usage
- Manual and AI-assisted security review

---

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- StateFlow
- Hilt / Dagger
- Kotlin Serialization
- DataStore
- Android Keystore
- AndroidX Biometric
- Gradle Kotlin DSL
- Gradle Version Catalog

---

## Requirements

- Android Studio Quail 2 or newer
- JDK 17
- Minimum SDK 24
- Compile SDK 37
- Target SDK 37
- Android Gradle Plugin 9.3.0
- Gradle 9.5.0
- Android emulator or physical device

Biometric testing requires a device or emulator that supports strong biometrics and has a fingerprint or face enrolled.

---

## Build and Run

1. Clone the repository:

```bash
git clone https://github.com/enesicmen/MovaTechnologyCase.git
```

2. Open the project in Android Studio.
3. Wait for Gradle Sync to complete.
4. Select an emulator or physical device.
5. Run the `app` configuration.

No external API key or service configuration is required.

---

## Wallet Dashboard

The dashboard contains:

- Current wallet balance
- Top Up button
- Horizontally scrollable linked children list
- Recent transactions list
- Income and expense indicators
- Bottom navigation:
  - Wallet
  - Activity
  - Profile

Only the Wallet tab is functional, as required by the case study.

---

## Dashboard States

The application supports the following states:

- Loading
- Loaded
- Empty
- Error

A demo state selector is available at the top of the screen so reviewers can test each state easily.

### Loaded

Uses:

```text
app/src/main/assets/mock_wallet.json
```

Displays the wallet balance, linked children, and recent transactions.

### Empty

Uses:

```text
app/src/main/assets/mock_wallet_empty.json
```

Displays a new wallet with no linked children and no recent transactions.

### Error

Simulates a failed data request.

The Retry button performs the repository request again. After the simulated temporary failure, the repository returns the loaded wallet data.

---

## Architecture

The project uses MVVM with repository abstraction.

```text
Compose UI
    ↓
ViewModel
    ↓
WalletRepository
    ↓
MockWalletRepository
    ↓
JSON Assets
```

### Presentation Layer

- Compose UI
- UiState
- ViewModel
- StateFlow

### Domain Layer

- Wallet models
- Child model
- Transaction model
- WalletRepository interface

### Data Layer

- MockWalletRepository
- WalletScenarioController
- JSON loading through AssetManager

The ViewModel depends only on the `WalletRepository` interface. The mock implementation can later be replaced without changing the ViewModel or Compose UI.

---

## Dependency Injection

Hilt / Dagger is used for dependency injection.

Hilt provides:

- WalletRepository
- AssetManager
- Kotlin Serialization Json
- DataStore
- SecureCredentialStorage
- Security components

No manual ViewModel factory is used.

---

## State Management

The ViewModel exposes the screen state through:

```kotlin
StateFlow<WalletDashboardContract>
```

The Compose UI observes state with:

```kotlin
collectAsStateWithLifecycle()
```

This approach provides lifecycle-aware and predictable state updates.

---

## Configuration Changes and Process Recreation

`ViewModel` preserves the current UI state during configuration changes such as device rotation.

`SavedStateHandle` stores the selected demo scenario. Therefore, the selected Loaded, Empty, or Error scenario survives Activity recreation.

Wallet data is not stored inside `SavedStateHandle`. It is loaded again from the repository after process recreation to avoid restoring stale financial information.

---

## Light and Dark Mode

The application uses Material 3 and supports system Light and Dark themes.

Income and expense information is not communicated through color alone. Different icons and positive or negative amount prefixes are also used.

---

## Security Review

The original insecure `AuthManager.kt` file is stored at:

```text
docs/original/AuthManager.kt
```

Security review documents:

```text
docs/01-manual-review/manual-code-review.md
docs/02-ai-assisted-review/ai-assisted-review.md
docs/03-comparison/comparison-analysis.md
```

The review covers:

- Plaintext credential storage
- Sensitive data logging
- Incomplete logout behavior
- Weak biometric authentication
- Missing CryptoObject usage
- Missing Android Keystore integration
- Biometric authentication bypass
- Threading and lifecycle issues
- Error handling
- Architectural concerns

---

## Corrected Security Implementation

The production-oriented security implementation is located at:

```text
app/src/main/java/com/movatechnologycase/security
```

It uses:

- Android Keystore
- AES/GCM encryption
- DataStore
- BiometricPrompt
- BIOMETRIC_STRONG
- CryptoObject
- Biometric-bound key
- Key invalidation after biometric enrollment changes
- Typed authentication results
- Secure logout

Only encrypted ciphertext and the initialization vector are stored in DataStore.

The encryption key remains inside Android Keystore.

Credential decryption can only be completed after successful biometric authentication through the authenticated CryptoObject.

Logout clears:

- Encrypted credential data
- Initialization vector
- Android Keystore key

---

## Security Runtime Verification

The security flow was manually tested on a biometric-enabled emulator.

Verified flow:

1. Save credentials
2. Complete biometric authentication
3. Store encrypted credentials
4. Authenticate again
5. Decrypt credentials successfully
6. Logout
7. Attempt to load credentials again
8. Receive `NoStoredCredentials`

The temporary security test screen was removed after verification. The final application opens directly on the Wallet Dashboard.

---

## Trade-offs

### Direct JSON-to-domain parsing

The mock JSON files are parsed directly into domain models.

This keeps the case study small and focused. In a larger production application, the data contract could be separated further as the project grows.

### Demo state selector

The selector allows reviewers to test Loaded, Empty, and Error states quickly.

In a production application, it should be restricted to debug builds or removed.

### Security flow is not connected to application startup

The case study does not include a login screen.

For that reason, biometric authentication is not forced when the application starts. `SecureAuthManager` is provided as an independent and testable component.

---

## Given More Time

- ViewModel unit tests
- Repository tests
- Compose UI tests
- Security integration tests
- Activity and Profile navigation
- Screenshot tests

---

## AI Tool Usage

AI tools were used for boilerplate, implementation ideas, and a secondary security review.

The AI-assisted review document contains:

- The exact prompt
- The AI response
- A comparison with the manual review
- An evaluation of incorrect or potentially misleading feedback
- Final decisions made through human engineering judgment

All architectural and security decisions were reviewed manually before being included.

## Wallet Dashboard Demo

https://github.com/user-attachments/assets/eaecb270-009b-4f81-8d39-bae59a48ad56