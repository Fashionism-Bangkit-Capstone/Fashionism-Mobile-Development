package com.fashionism.fashionismuserapp.data.db

data class RegisterResponse(
    var error: String,
    var message: String,
)

data class RegisterDataAccount(
    var name: String,
    var email: String,
    var password: String
)

data class LoginResponse(
    var error: String,
    var message: String,
    var data: LoginResult
)

data class LoginDataAccount(
    var email: String,
    var password: String
)

data class LoginResult(
    var id: Int,
    var name: String,
    var email: String,
    var access_token: String
)

data class ResponseGetProfile(
    var error: Boolean,
    var data: ProfileDetail
)

data class ResponseUpdateProfile(
    var error: Boolean,
    var message: String
)

data class ResponseChangePassword(
    var error: Boolean,
    var message: String,
)

data class ChangePassword(
    var old_password: String,
    var new_password: String,
    var confirm_password: String
)

data class ResponseGetAllPreferences(
    var error: Boolean,
    var data: List<PreferenceDetail>
)

data class PreferenceDetail(
    var id: Int,
    var name: String,
)

data class ResponseGetUserPreferences(
    var error: Boolean,
    var data: List<UserPreferences>
)

data class UserPreferences(
    var id: Int,
    var name: String,
    var user_account_preferences: UserAccountPreferences
)

data class UserAccountPreferences(
    var user_account_id: Int,
    var preference_id: Int
)

data class ResponseSetPreferences(
    var error: Boolean,
    var message: String
)

data class ProfileDetail(
    var id: Int,
    var name: String,
    var email: String,
    var phone: String? = null,
    var address: String? = null,
    var avatar: String? = null,
)
