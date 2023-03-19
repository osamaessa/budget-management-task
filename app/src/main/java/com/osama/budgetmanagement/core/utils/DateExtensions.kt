package com.osama.budgetmanagement.core.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("formatDate")
fun TextView.formatDate(time: Long){
    text = try {
        val date = Date(time)
        SimpleDateFormat("yyyy, MMM dd", Locale.getDefault()).format(date)
    } catch (e: Exception) {
        SimpleDateFormat("yyyy, MMM dd", Locale.getDefault()).format(Date())
    }
}