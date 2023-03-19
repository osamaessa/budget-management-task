package com.osama.budgetmanagement.domain.common

sealed class RequestState<out T> {
    object Loading : RequestState<Nothing>()
    data class Error(var exception: Throwable) : RequestState<Nothing>()
    data class Success<T>(var data: T) : RequestState<T>()
}