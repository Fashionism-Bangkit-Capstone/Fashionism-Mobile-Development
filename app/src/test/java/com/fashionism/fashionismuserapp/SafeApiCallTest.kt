package com.fashionism.fashionismuserapp

import com.fashionism.fashionismuserapp.data.Result
import com.fashionism.fashionismuserapp.data.safeApiCall
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SafeApiCallTest {
    private fun httpError(code: Int, body: String) =
        HttpException(Response.error<Any>(code, body.toResponseBody()))

    @Test
    fun success_wrapsTheValue() = runTest {
        assertEquals(Result.Success(42), safeApiCall { 42 })
    }

    @Test
    fun httpError_keepsCodeAndServerMessage() = runTest {
        val result = safeApiCall { throw httpError(400, """{"error":true,"message":"Email already exists"}""") }
        assertEquals(Result.Error(400, "Email already exists"), result)
    }

    @Test
    fun httpErrorWithoutJsonBody_stillReportsTheCode() = runTest {
        assertEquals(Result.Error(500, null), safeApiCall { throw httpError(500, "<html>Bad Gateway</html>") })
    }

    @Test
    fun noConnection_isAnErrorWithoutCodeOrMessage() = runTest {
        assertEquals(Result.Error(), safeApiCall { throw IOException("timeout") })
    }
}
