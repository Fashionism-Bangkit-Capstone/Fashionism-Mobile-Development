package com.fashionism.fashionismuserapp

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.fashionism.fashionismuserapp.data.Result
import com.fashionism.fashionismuserapp.data.local.UserSession
import com.fashionism.fashionismuserapp.data.remote.request.ChangePassword
import com.fashionism.fashionismuserapp.data.remote.request.LoginDataAccount
import com.fashionism.fashionismuserapp.data.remote.response.LoginResponse
import com.fashionism.fashionismuserapp.data.remote.response.LoginResult
import com.fashionism.fashionismuserapp.data.remote.response.MessageResponse
import com.fashionism.fashionismuserapp.data.remote.request.RegisterDataAccount
import com.fashionism.fashionismuserapp.data.remote.response.RegisterResponse
import com.fashionism.fashionismuserapp.data.remote.response.ResponseGetProfile
import com.fashionism.fashionismuserapp.data.remote.UserApi
import com.fashionism.fashionismuserapp.data.repository.AuthRepository
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import retrofit2.HttpException
import retrofit2.Response

class AuthRepositoryTest {
    @get:Rule
    val folder = TemporaryFolder()

    /** Only login matters here; any other call is a test bug. */
    private class FakeUserApi(private val login: () -> LoginResponse) : UserApi {
        override suspend fun login(body: LoginDataAccount) = login()
        override suspend fun register(body: RegisterDataAccount): RegisterResponse = error("unused")
        override suspend fun getProfile(userId: Int): ResponseGetProfile = error("unused")
        override suspend fun updateProfile(
            userId: Int, name: RequestBody, email: RequestBody, phone: RequestBody,
            address: RequestBody, avatar: MultipartBody.Part,
        ): MessageResponse = error("unused")
        override suspend fun changePassword(userId: Int, body: ChangePassword): MessageResponse = error("unused")
    }

    private fun TestScope.session() = UserSession(
        PreferenceDataStoreFactory.create(scope = backgroundScope) { folder.newFile("session.preferences_pb") }
    )

    @Test
    fun successfulLogin_savesTheSession() = runTest {
        val session = session()
        val api = FakeUserApi { LoginResponse("false", "Login success", LoginResult(7, "Ana", "ana@mail.com", "abc123")) }

        val result = AuthRepository(api, session).login("ana@mail.com", "secret")

        assertTrue(result is Result.Success)
        assertTrue(session.isLoggedIn())
        assertEquals(7, session.userId())
        assertEquals("abc123", session.token())
    }

    @Test
    fun failedLogin_leavesTheUserLoggedOut() = runTest {
        val session = session()
        val api = FakeUserApi { throw HttpException(Response.error<Any>(401, "".toResponseBody())) }

        val result = AuthRepository(api, session).login("ana@mail.com", "wrong")

        assertEquals(401, (result as Result.Error).code)
        assertFalse(session.isLoggedIn())
    }

    @Test
    fun logout_clearsTheSession() = runTest {
        val session = session()
        session.save(7, "abc123")

        AuthRepository(FakeUserApi { error("unused") }, session).logout()

        assertFalse(session.isLoggedIn())
        assertEquals("", session.token())
    }
}
