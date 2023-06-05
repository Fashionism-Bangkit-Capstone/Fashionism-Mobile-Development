package com.fashionism.fashionismuserapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fashionism.fashionismuserapp.data.api.APIConfig
import com.fashionism.fashionismuserapp.data.api.APIService
import com.fashionism.fashionismuserapp.data.db.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(private val apiService: APIService) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _userLogin = MutableLiveData<LoginResponse>()
    val userLogin: LiveData<LoginResponse> = _userLogin

    fun login(loginDataAccount: LoginDataAccount) {
        _isLoading.value = true
        val api = APIConfig.getApiService().loginUser(loginDataAccount)
        api.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()

                if (response.isSuccessful) {
                    _userLogin.value = responseBody!!
                    _message.value = responseBody.message
                } else {
                    when (response.code()) {
                        401 -> _message.value =
                            response.message()
                        408 -> _message.value =
                            "Koneksi internet anda lambat, silahkan coba lagi"
                        else -> _message.value = "Pesan error: " + response.message()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Pesan error: " + t.message.toString()
            }

        })
    }

    fun register(registerDataAccount: RegisterDataAccount) {
        _isLoading.value = true
        val api = APIConfig.getApiService().registerUser(registerDataAccount)
        api.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()

                if (response.isSuccessful) {
                    _message.value = responseBody?.message
                } else {
                    when (response.code()) {
                        400 -> _message.value =
                            response.message()
                        408 -> _message.value =
                            "Koneksi internet anda lambat, silahkan coba lagi"
                        else -> _message.value = "Pesan error: " + response.message()
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Pesan error: " + t.message.toString()
            }
        })
    }

    fun getProfileUser(idUser: Int, token: String) {
        _isLoading.value = true
        val api = APIConfig.getApiService().getUser(idUser, token)
        api.enqueue(object : Callback<ResponseGetProfile> {
            override fun onResponse(
                call: Call<ResponseGetProfile>,
                response: Response<ResponseGetProfile>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _message.value = "Berhasil mengubah data"
                } else {
                    when (response.code()) {
                        401 -> _message.value =
                            response.message()
                        408 -> _message.value =
                            "Koneksi internet anda lambat, silahkan coba lagi"
                        else -> _message.value = "Pesan error: " + response.message()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseGetProfile>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Pesan error: " + t.message.toString()
            }

        })
    }

    fun updateProfileUser(idUser: Int, requestUpdate: ProfileDetail, token: String) {
        _isLoading.value = true
        val api = APIConfig.getApiService().updateUser(idUser, requestUpdate, token)
        api.enqueue(object : Callback<ResponseUpdateProfile> {
            override fun onResponse(
                call: Call<ResponseUpdateProfile>,
                response: Response<ResponseUpdateProfile>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _message.value = "Berhasil mengubah data"
                } else {
                    when (response.code()) {
                        401 -> _message.value =
                            response.message()
                        408 -> _message.value =
                            "Koneksi internet anda lambat, silahkan coba lagi"
                        else -> _message.value = "Pesan error: " + response.message()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseUpdateProfile>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Pesan error: " + t.message.toString()
            }

        })
    }

    fun changePasswordUser(idUser: Int, requestUpdate: ChangePassword, token: String) {
        _isLoading.value = true
        val api = APIConfig.getApiService().changePassword(idUser, requestUpdate, token)
        api.enqueue(object : Callback<ResponseChangePassword> {
            override fun onResponse(
                call: Call<ResponseChangePassword>,
                response: Response<ResponseChangePassword>
            ) {
                _isLoading.value = false
                val responseBody = response.body()

                if (response.isSuccessful) {
                    _message.value = responseBody?.message
                } else {
                    when (response.code()) {
                        401 -> _message.value =
                            response.message()
                        408 -> _message.value =
                            "Koneksi internet anda lambat, silahkan coba lagi"
                        else -> _message.value = "Pesan error: " + response.message()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseChangePassword>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Pesan error: " + t.message.toString()
            }

        })
    }

}