package com.osama.budgetmanagement.core.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.NumberFormat
import java.util.*


@BindingAdapter(value = ["amount", "isDollar"], requireAll = true)
fun TextView.formatCurrency(value: Double, isDollar: Boolean){
    val currency = if (isDollar) Currency.getInstance("USD") else Currency.getInstance("JOD")
    val formattedValue = NumberFormat.getCurrencyInstance().apply {
        this.currency = currency
    }.format(value)
    text = formattedValue
}