package com.fashionism.fashionismuserapp.data.repository

import com.fashionism.fashionismuserapp.data.Result
import com.fashionism.fashionismuserapp.data.local.UserSession
import com.fashionism.fashionismuserapp.data.model.ChangePassword
import com.fashionism.fashionismuserapp.data.model.MessageResponse
import com.fashionism.fashionismuserapp.data.model.ProfileDetail
import com.fashionism.fashionismuserapp.data.remote.UserApi
import com.fashionism.fashionismuserapp.data.safeApiCall
import com.fashionism.fashionismuserapp.tools.Helper.reduceFileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserRepository(private val api: UserApi, private val session: UserSession) {

    suspend fun getProfile(): Result<ProfileDetail> =
        safeApiCall { api.getProfile(session.userId()).data }

    suspend fun updateProfile(
        name: String,
        email: String,
        phone: String,
        address: String,
        avatar: File?,
    ): Result<MessageResponse> {
        val avatarPart = if (avatar == null) {
            MultipartBody.Part.createFormData("avatar", "")
        } else {
            val image = withContext(Dispatchers.IO) { reduceFileImage(avatar) }
            MultipartBody.Part.createFormData("avatar", image.name, image.asRequestBody(JPEG))
        }
        return safeApiCall {
            api.updateProfile(
                session.userId(),
                name.toRequestBody(TEXT),
                email.toRequestBody(TEXT),
                phone.toRequestBody(TEXT),
                address.toRequestBody(TEXT),
                avatarPart,
            )
        }
    }

    suspend fun changePassword(old: String, new: String, confirm: String): Result<MessageResponse> =
        safeApiCall { api.changePassword(session.userId(), ChangePassword(old, new, confirm)) }

    private companion object {
        val TEXT = "text/plain".toMediaType()
        val JPEG = "image/jpeg".toMediaType()
    }
}
