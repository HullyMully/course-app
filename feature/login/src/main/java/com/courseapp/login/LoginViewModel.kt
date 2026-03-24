package com.courseapp.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import com.courseapp.core.util.ValidationUtils

data class LoginUiState(
    val emailErrorRes: Int? = null,
    val passwordErrorRes: Int? = null
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _navigateToHome = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val navigateToHome: SharedFlow<Unit> = _navigateToHome.asSharedFlow()

    private var currentEmail = ""
    private var currentPassword = ""

    fun setCredentials(email: String, password: String) {
        currentEmail = email
        currentPassword = password

        val emailError = when {
            email.isBlank() -> null
            !ValidationUtils.isValidEmail(email) -> R.string.email_error
            else -> null
        }
        val passwordError = if (currentPassword.isNotBlank()) null else _uiState.value.passwordErrorRes
        _uiState.value = LoginUiState(emailErrorRes = emailError, passwordErrorRes = passwordError)
    }

    fun onLoginClick(): Boolean {
        val emailError = when {
            currentEmail.isBlank() -> R.string.empty_email_error
            !ValidationUtils.isValidEmail(currentEmail) -> R.string.email_error
            else -> null
        }
        val passwordError = if (currentPassword.isBlank()) R.string.empty_password_error else null

        _uiState.value = LoginUiState(emailErrorRes = emailError, passwordErrorRes = passwordError)

        val hasErrors = emailError != null || passwordError != null
        if (!hasErrors) {
            _navigateToHome.tryEmit(Unit)
        }
        return !hasErrors
    }
}
