package com.fashionism.fashionismuserapp.data

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>

    /**
     * [code] is the HTTP status, or null when the request never got a response (no connection, timeout).
     * [message] is the server's message from the error body, if it sent one.
     */
    data class Error(val code: Int? = null, val message: String? = null) : Result<Nothing>
}
