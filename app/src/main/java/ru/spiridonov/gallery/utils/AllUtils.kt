package ru.spiridonov.gallery.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

object AllUtils {
    @SuppressLint("SimpleDateFormat")
    fun dateLongToString(milliSeconds: Long?, pattern: String = "dd.MM.yyyy"): String {
        val formatter = SimpleDateFormat(pattern)
        val calendar: Calendar = Calendar.getInstance()
        if (milliSeconds != null) {
            calendar.timeInMillis = milliSeconds
        }
        return formatter.format(calendar.time)
    }
}