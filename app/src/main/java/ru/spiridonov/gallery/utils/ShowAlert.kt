package ru.spiridonov.gallery.utils

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog

class ShowAlert(activity: Activity, message:String) {
    init {
        val builder = AlertDialog.Builder(activity as Context)
        builder.setMessage(message)
        builder.setPositiveButton("ok", null)
        val dialog = builder.create()
        dialog.show()
    }
}