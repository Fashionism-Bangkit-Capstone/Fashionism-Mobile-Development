package com.fashionism.fashionismuserapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fashionism.fashionismuserapp.data.model.ProfileDetail
import com.fashionism.fashionismuserapp.data.repository.AuthRepository
import com.fashionism.fashionismuserapp.data.repository.UserRepository
import com.fashionism.fashionismuserapp.ui.common.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    sealed interface Event {
        data object ProfileUpdated : Event
        data class PasswordChanged(val message: String?) : Event
        data object LoggedOut : Event
    }

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _profile = MutableLiveData<ProfileDetail>()
    val profile: LiveData<ProfileDetail> = _profile

    init {
        loadProfile()
    }

    private fun loadProfile() {
        request(call = { userRepository.getProfile() }) { _profile.value = it }
    }

    fun updateProfile(name: String, email: String, phone: String, address: String, avatar: File?) {
        request(call = { userRepository.updateProfile(name, email, phone, address, avatar) }) {
            _events.trySend(Event.ProfileUpdated)
            loadProfile()
        }
    }

    fun changePassword(old: String, new: String, confirm: String) {
        request(call = { userRepository.changePassword(old, new, confirm) }) {
            _events.trySend(Event.PasswordChanged(it.message))
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _events.send(Event.LoggedOut)
        }
    }
}
