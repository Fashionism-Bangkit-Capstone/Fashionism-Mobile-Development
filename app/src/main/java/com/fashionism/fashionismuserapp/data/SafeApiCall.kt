package com.fashionism.fashionismuserapp.data

import com.fashionism.fashionismuserapp.data.remote.response.MessageResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

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
