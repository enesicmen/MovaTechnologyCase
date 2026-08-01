import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executors

class AuthManager private constructor(context: Context) {

    companion object {
        @Volatile
        private var instance: AuthManager? = null

        fun getInstance(context: Context): AuthManager {
            return instance ?: AuthManager(context).also { instance = it }
        }
    }

    var currentToken: String? = null
    var walletPin: String? = null

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveUserCredentials(token: String, pin: String) {
        prefs.edit()
            .putString("auth_token", token)
            .putString("wallet_pin", pin)
            .commit()
        this.currentToken = token
        this.walletPin = pin
        Log.d("AuthManager", "Saved credentials token=$token pin=$pin")
    }

    fun loadCredentials(): Boolean {
        val token = prefs.getString("auth_token", null)
        val pin = prefs.getString("wallet_pin", null)
        if (token != null && pin != null) {
            this.currentToken = token
            this.walletPin = pin
            return true
        }
        return false
    }

    fun authenticateWithBiometrics(
        activity: FragmentActivity,
        completion: (Boolean) -> Unit
    ) {
        val executor = Executors.newSingleThreadExecutor()
        val prompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    completion(true)
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    completion(false)
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login to your wallet")
            .setAllowedAuthenticators(
                BiometricPrompt.Authenticators.BIOMETRIC_WEAK or
                        BiometricPrompt.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        prompt.authenticate(promptInfo)
    }

    fun logout() {
        currentToken = null
        walletPin = null
    }
}
