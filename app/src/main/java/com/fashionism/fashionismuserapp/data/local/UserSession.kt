package com.fashionism.fashionismuserapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

// File name and keys are unchanged from the old UserSession so existing users stay logged in
val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserSession(private val dataStore: DataStore<Preferences>) {

    suspend fun isLoggedIn(): Boolean = dataStore.data.first()[LOGIN_SESSION] ?: false

    suspend fun token(): String = dataStore.data.first()[TOKEN].orEmpty()

    suspend fun userId(): Int = dataStore.data.first()[ID_USER] ?: 0

    suspend fun save(userId: Int, token: String) {
        dataStore.edit {
            it[LOGIN_SESSION] = true
            it[ID_USER] = userId
            it[TOKEN] = token
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    private companion object {
        val LOGIN_SESSION = booleanPreferencesKey("login_session")
        val TOKEN = stringPreferencesKey("token")
        val ID_USER = intPreferencesKey("id_user")
    }
}
