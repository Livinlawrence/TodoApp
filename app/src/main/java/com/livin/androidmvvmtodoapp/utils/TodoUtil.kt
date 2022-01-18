package com.livin.androidmvvmtodoapp.utils

object TodoUtil {

    fun isValidTitle(
        title: String
    ): Boolean {
        if (title.isBlank()) {
            return false
        }
        return true
    }

    fun isValidDate(
        date: String,
    ): Boolean {
        if (date.isBlank()) {
            return false
        }
        return true
    }

    fun isValidTime(
        time: String,
    ): Boolean {
        if (time.isBlank()) {
            return false
        }
        return true
    }

    fun isValidFrequency(
        type: Int,
    ): Boolean {
        if (type == -1) {
            return false
        }
        return true
    }
}