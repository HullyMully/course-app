package com.courseapp.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.courseapp.core.util.ValidationUtils
import com.courseapp.login.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailEdit.filters = arrayOf(ValidationUtils.noCyrillicFilter)
        binding.emailEdit.addTextChangedListener { clearErrorsOnType() }
        binding.passwordEdit.addTextChangedListener { clearErrorsOnType() }
        binding.repeatPasswordEdit.addTextChangedListener { clearErrorsOnType() }

        binding.registerButton.setOnClickListener { onRegisterClick() }
        binding.loginLink.setOnClickListener { finish() }
        binding.vkButton.setOnClickListener { openUrl("https://vk.com/") }
        binding.okButton.setOnClickListener { openUrl("https://ok.ru/") }
    }

    private fun clearErrorsOnType() {
        val email = binding.emailEdit.text?.toString().orEmpty()
        val password = binding.passwordEdit.text?.toString().orEmpty()
        val repeat = binding.repeatPasswordEdit.text?.toString().orEmpty()

        binding.emailLayout.error = when {
            email.isBlank() -> null
            !ValidationUtils.isValidEmail(email) -> getString(R.string.email_error)
            else -> null
        }

        if (password.isNotBlank()) binding.repeatPasswordLayout.error = null
        if (repeat.isNotBlank() && password.isNotBlank() && password != repeat) {
            binding.repeatPasswordLayout.error = getString(R.string.passwords_mismatch)
        }
    }

    private fun onRegisterClick() {
        val email = binding.emailEdit.text?.toString().orEmpty()
        val password = binding.passwordEdit.text?.toString().orEmpty()
        val repeat = binding.repeatPasswordEdit.text?.toString().orEmpty()
        var hasErrors = false

        if (email.isBlank()) {
            binding.emailLayout.error = getString(R.string.empty_email_error)
            hasErrors = true
        } else if (!ValidationUtils.isValidEmail(email)) {
            binding.emailLayout.error = getString(R.string.email_error)
            hasErrors = true
        }

        when {
            password.isBlank() -> {
                binding.repeatPasswordLayout.error = getString(R.string.empty_password_error)
                hasErrors = true
            }
            repeat.isBlank() -> {
                binding.repeatPasswordLayout.error = getString(R.string.empty_repeat_password_error)
                hasErrors = true
            }
            password != repeat -> {
                binding.repeatPasswordLayout.error = getString(R.string.passwords_mismatch)
                hasErrors = true
            }
        }

        if (!hasErrors) {
            Toast.makeText(this, R.string.feature_unavailable, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
