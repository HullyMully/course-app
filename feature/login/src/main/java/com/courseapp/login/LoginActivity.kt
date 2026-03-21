package com.courseapp.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.courseapp.core.util.ValidationUtils
import com.courseapp.login.databinding.ActivityLoginBinding

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

        viewModel.emailError.observe(this) { binding.emailLayout.error = it }
        viewModel.passwordError.observe(this) { binding.passwordLayout.error = it }
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
            binding.emailEdit.text?.toString().orEmpty(),
            binding.passwordEdit.text?.toString().orEmpty()
        )
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
