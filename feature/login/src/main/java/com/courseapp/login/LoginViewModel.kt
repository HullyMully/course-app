package com.courseapp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val emailRegex = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")

    private val _emailError = MutableLiveData<String?>(null)
    val emailError: LiveData<String?> = _emailError

    private val _passwordError = MutableLiveData<String?>(null)
    val passwordError: LiveData<String?> = _passwordError

    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?> = _navigateToHome

    private var currentEmail = ""
    private var currentPassword = ""

    fun setCredentials(email: String, password: String) {
        currentEmail = email
        currentPassword = password

        if (email.isNotBlank()) {
            val hasCyrillic = email.any { it in 'а'..'я' || it in 'А'..'Я' || it == 'ё' || it == 'Ё' }
            val validEmail = emailRegex.matches(email) && !hasCyrillic
            _emailError.value = if (!validEmail) "Неверный формат email (example@mail.com)" else null
        } else {
            _emailError.value = null
        }

        if (currentPassword.isNotBlank()) {
            _passwordError.value = null
        }
    }

    fun onLoginClick(): Boolean {
        var hasErrors = false

        if (currentEmail.isBlank()) {
            _emailError.value = "Введите email"
            hasErrors = true
        } else {
            val hasCyrillic = currentEmail.any { it in 'а'..'я' || it in 'А'..'Я' || it == 'ё' || it == 'Ё' }
            val validEmail = emailRegex.matches(currentEmail) && !hasCyrillic
            if (!validEmail) {
                _emailError.value = "Неверный формат email (example@mail.com)"
                hasErrors = true
            }
        }

        if (currentPassword.isBlank()) {
            _passwordError.value = "Введите пароль"
            hasErrors = true
        }

        if (!hasErrors) {
            _navigateToHome.value = true
        }
        return !hasErrors
    }

    fun onNavigateHandled() {
        _navigateToHome.value = null
    }
}
