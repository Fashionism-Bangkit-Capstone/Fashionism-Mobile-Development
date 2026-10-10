package com.fashionism.fashionismuserapp

import com.fashionism.fashionismuserapp.data.model.LoginResponse
import com.fashionism.fashionismuserapp.data.model.ResponseGetProfile
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

// The API models have no @SerializedName, so property names must match the JSON keys exactly.
// Renaming a property (e.g. access_token -> accessToken) silently breaks parsing; these tests catch it.
class ApiModelParsingTest {
    private val gson = Gson()

    @Test
    fun loginResponse_parsesNestedResultAndToken() {
        val json = """
            {"error":"false","message":"success",
             "data":{"id":7,"name":"Ana","email":"ana@mail.com","access_token":"abc123"}}
        """
        val response = gson.fromJson(json, LoginResponse::class.java)

        assertEquals(7, response.data.id)
        assertEquals("ana@mail.com", response.data.email)
        assertEquals("abc123", response.data.access_token)
    }

    @Test
    fun profile_parsesOptionalFieldsAsNullWhenMissing() {
        val json = """{"error":false,"data":{"id":1,"name":"Ana","email":"ana@mail.com"}}"""
        val profile = gson.fromJson(json, ResponseGetProfile::class.java).data

        assertEquals("Ana", profile.name)
        assertNull(profile.phone)
        assertNull(profile.avatar)
    }
}
