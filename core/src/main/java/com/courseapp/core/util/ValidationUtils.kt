package com.courseapp.core.util

import android.text.InputFilter

object ValidationUtils {

    val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")

    fun containsCyrillic(text: String): Boolean =
        text.any { it in 'а'..'я' || it in 'А'..'Я' || it == 'ё' || it == 'Ё' }

    fun isValidEmail(email: String): Boolean =
        EMAIL_REGEX.matches(email) && !containsCyrillic(email)

    val noCyrillicFilter = InputFilter { source, _, _, _, _, _ ->
        if (source.any { it in 'а'..'я' || it in 'А'..'Я' || it == 'ё' || it == 'Ё' }) "" else null
    }
}
