package com.courseapp.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.ViewModelProvider
import com.courseapp.core.util.ValidationUtils
import com.courseapp.login.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.emailEdit.filters = arrayOf(ValidationUtils.noCyrillicFilter)
        binding.emailEdit.addTextChangedListener { updateValidation() }
        binding.passwordEdit.addTextChangedListener { updateValidation() }

        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.emailLayout.error = state.emailErrorRes?.let(::getString)
                        binding.passwordLayout.error = state.passwordErrorRes?.let(::getString)
                    }
                }
                launch {
                    viewModel.navigateToHome.collect {
                        startActivity(Intent(this@LoginActivity, Class.forName("com.courseapp.MainActivity")))
                        finish()
                    }
                }
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
            binding.emailEdit.text?.toString().orEmpty(),
            binding.passwordEdit.text?.toString().orEmpty()
        )
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
