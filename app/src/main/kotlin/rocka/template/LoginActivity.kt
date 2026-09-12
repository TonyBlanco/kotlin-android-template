package rocka.template

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import rocka.template.auth.MockAuthRepository
import rocka.template.databinding.ActivityLoginBinding

class LoginActivity : Activity() {
    private lateinit var binding: ActivityLoginBinding
    private var isSigningIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {
        if (isSigningIn) return

        val email = binding.emailInput.text?.toString().orEmpty().trim()
        val password = binding.passwordInput.text?.toString().orEmpty()

        binding.emailInput.error = null
        binding.passwordInput.error = null

        var hasError = false
        if (email.isEmpty()) {
            binding.emailInput.error = getString(R.string.login_error_empty_email)
            hasError = true
        }
        if (password.isEmpty()) {
            binding.passwordInput.error = getString(R.string.login_error_empty_password)
            hasError = true
        }
        if (hasError) return

        setSigningIn(true)
        MockAuthRepository.signIn(email, password) { result ->
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                setSigningIn(false)
                result.onSuccess {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun setSigningIn(signingIn: Boolean) {
        isSigningIn = signingIn
        binding.loginButton.isEnabled = !signingIn
        binding.emailInput.isEnabled = !signingIn
        binding.passwordInput.isEnabled = !signingIn
        binding.loginProgress.visibility = if (signingIn) View.VISIBLE else View.GONE
    }
}
