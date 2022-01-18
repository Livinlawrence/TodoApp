package com.livin.androidmvvmtodoapp.utils

import androidx.core.util.PatternsCompat

object LoginUtil {

    fun isValidForm(
        email: String,
        password: String
    ): Boolean {
        if (email.isBlank() || password.isBlank()) {
            return false
        }
        return true
    }

    fun isValidEmail(
        email: String
    ): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }


}