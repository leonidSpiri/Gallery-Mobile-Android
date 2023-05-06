package ru.spiridonov.gallery.presentation.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import ru.spiridonov.gallery.R
import ru.spiridonov.gallery.utils.AllUtils

@BindingAdapter("errorInput")
fun bindErrorInput(textInputLayout: TextInputLayout, isError: Boolean) {
    val message =
        if (isError) textInputLayout.context.getString(R.string.error_input_name) else null
    textInputLayout.error = message
}

@BindingAdapter("setMediaName")
fun parseMediaName(textView: TextView, name: String?) {
    textView.text = String.format(textView.context.resources.getString(R.string.name), name)
}

@BindingAdapter("setMediaLocation")
fun parseMediaLocation(textView: TextView, location: String?) {
    textView.text = String.format(textView.context.resources.getString(R.string.location), location)
}

@BindingAdapter("setMediaDate")
fun parseMediaDate(textView: TextView, date: Long?) {
    val strDate = AllUtils.dateLongToString(date)
    textView.text = String.format(textView.context.resources.getString(R.string.date), strDate)
}

@BindingAdapter("setCameraInfo")
fun parseCameraInfo(textView: TextView, info: String?) {
    textView.text = String.format(textView.context.resources.getString(R.string.camera_info), info)
}

@BindingAdapter("welcomeUser")
fun textWelcomeUser(textView: TextView, text: String?) {
    textView.text = String.format(textView.context.resources.getString(R.string.welcome), text)
}
