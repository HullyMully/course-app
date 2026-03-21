package com.courseapp.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.courseapp.login.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private val noCyrillicFilter = InputFilter { source, _, _, _, _, _ ->
        if (source.any { it in 'а'..'я' || it in 'А'..'Я' || it == 'ё' || it == 'Ё' }) "" else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.emailEdit.filters = arrayOf(noCyrillicFilter)
        binding.emailEdit.addTextChangedListener { updateValidation() }
        binding.passwordEdit.addTextChangedListener { updateValidation() }

        viewModel.emailError.observe(this) { error ->
            binding.emailLayout.error = error
        }
        viewModel.passwordError.observe(this) { error ->
            binding.passwordLayout.error = error
        }
        viewModel.navigateToHome.observe(this) { shouldNavigate ->
            if (shouldNavigate == true) {
                startActivity(Intent(this, Class.forName("com.courseapp.MainActivity")))
                finish()
                viewModel.onNavigateHandled()
            }
        }

        binding.loginButton.setOnClickListener { viewModel.onLoginClick() }

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.forgotPasswordLink.setOnClickListener {
            Toast.makeText(this, R.string.feature_unavailable, Toast.LENGTH_SHORT).show()
        }

        binding.vkButton.setOnClickListener { openUrl("https://vk.com/") }
        binding.okButton.setOnClickListener { openUrl("https://ok.ru/") }
    }

    private fun updateValidation() {
        viewModel.setCredentials(
            binding.emailEdit.text?.toString() ?: "",
            binding.passwordEdit.text?.toString() ?: ""
        )
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
