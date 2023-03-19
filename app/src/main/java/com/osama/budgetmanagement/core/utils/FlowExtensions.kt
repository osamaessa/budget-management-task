package com.osama.budgetmanagement.core.utils

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


inline fun <reified T> SharedFlow<T>.observe(
    fragment: Fragment,
    crossinline action: (value: T) -> Unit
): Job {

    return fragment.viewLifecycleOwner.lifecycleScope.launch {
        collectLatest { value ->
            Log.d("AccountLog", "observeEvent: $value")
            action(value)
        }
    }
}

inline fun <reified T> StateFlow<T>.observe(
    fragment: Fragment,
    crossinline action: (value: T) -> Unit
): Job {
    return fragment.viewLifecycleOwner.lifecycleScope.launch {
        collect { value -> action(value) }
    }
}