package net.dev.weather.utils

import androidx.annotation.StringRes

sealed class Async<out T> {
    object Loading : Async<Nothing>()
    data class Error(@StringRes val message:Int, val throwable: Throwable?) : Async<Nothing>()
    data class Success<out T>(val data: T) : Async<T>()
}