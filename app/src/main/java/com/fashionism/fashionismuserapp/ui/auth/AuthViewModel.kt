package com.fashionism.fashionismuserapp.ui.auth

import com.fashionism.fashionismuserapp.data.repository.AuthRepository
import com.fashionism.fashionismuserapp.ui.common.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class AuthViewModel(private val repository: AuthRepository) : BaseViewModel() {

    sealed interface Event {
        data class LoggedIn(val message: String) : Event
        data object Registered : Event
    }

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun login(email: String, password: String) {
        request(call = { repository.login(email, password) }) {
            _events.trySend(Event.LoggedIn(it.message))
        }
    }

    /** Registers, then logs straight in with the same credentials. */
    fun register(name: String, email: String, password: String) {
        request(call = { repository.register(name, email, password) }) {
            _events.trySend(Event.Registered)
            login(email, password)
        }
    }
}
