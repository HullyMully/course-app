package com.courseapp.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.courseapp.login.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val emailRegex = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")

    private val noCyrillicFilter = InputFilter { source, _, _, _, _, _ ->
        if (source.any { it in 'а'..'я' || it in 'А'..'Я' || it == 'ё' || it == 'Ё' }) "" else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailEdit.filters = arrayOf(noCyrillicFilter)

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

        if (email.isNotBlank()) {
            val hasCyrillic = email.any { it in 'а'..'я' || it in 'А'..'Я' || it == 'ё' || it == 'Ё' }
            val validEmail = emailRegex.matches(email) && !hasCyrillic
            binding.emailLayout.error = if (!validEmail) getString(R.string.email_error) else null
        } else {
            binding.emailLayout.error = null
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
            binding.emailLayout.error = "Введите email"
            hasErrors = true
        } else {
            val hasCyrillic = email.any { it in 'а'..'я' || it in 'А'..'Я' || it == 'ё' || it == 'Ё' }
            val validEmail = emailRegex.matches(email) && !hasCyrillic
            if (!validEmail) {
                binding.emailLayout.error = getString(R.string.email_error)
                hasErrors = true
            }
        }

        if (password.isBlank()) {
            binding.repeatPasswordLayout.error = "Введите пароль"
            hasErrors = true
        } else if (repeat.isBlank()) {
            binding.repeatPasswordLayout.error = "Повторите пароль"
            hasErrors = true
        } else if (password != repeat) {
            binding.repeatPasswordLayout.error = getString(R.string.passwords_mismatch)
            hasErrors = true
        }

        if (!hasErrors) {
            Toast.makeText(this, R.string.feature_unavailable, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
