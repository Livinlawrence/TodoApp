package com.livin.androidmvvmtodoapp.utils

import java.text.SimpleDateFormat
import java.util.*


object Converters {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun fromTimestampToDate(timeStamp: Long?): String? {
        return timeStamp?.let { dateFormat.format(timeStamp) }
    }

    fun fromTimestampToTime(timeStamp: Long?): String? {
        return timeStamp?.let { timeFormat.format(timeStamp) }
    }

}