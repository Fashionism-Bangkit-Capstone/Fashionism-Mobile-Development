package com.fashionism.fashionismuserapp.data

import com.fashionism.fashionismuserapp.data.model.MessageResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>

    /**
     * [code] is the HTTP status, or null when the request never got a response (no connection, timeout).
     * [message] is the server's message from the error body, if it sent one.
     */
    data class Error(val code: Int? = null, val message: String? = null) : Result<Nothing>
}

suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> = try {
    Result.Success(call())
} catch (e: CancellationException) {
    throw e
} catch (e: HttpException) {
    Result.Error(e.code(), e.serverMessage())
} catch (e: IOException) {
    Result.Error()
} catch (e: Exception) {
    // Malformed body and the like: still an error the UI can show, not a crash
    Result.Error(message = e.message)
}

private fun HttpException.serverMessage(): String? = try {
    response()?.errorBody()?.string()?.let { Gson().fromJson(it, MessageResponse::class.java)?.message }
} catch (e: Exception) {
    null
}
