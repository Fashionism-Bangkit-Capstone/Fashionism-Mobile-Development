package com.fashionism.fashionismuserapp.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fashionism.fashionismuserapp.data.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private var runningRequests = 0
    private val _isLoading = MutableLiveData(false)

    /** True while any request started with showLoading is running (several can overlap). */
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errors = Channel<Result.Error>(Channel.BUFFERED)

    /** Each error is delivered once; collect with collectWhenStarted. */
    val errors: Flow<Result.Error> = _errors.receiveAsFlow()

    protected fun <T> request(
        call: suspend () -> Result<T>,
        showLoading: Boolean = true,
        onError: (Result.Error) -> Unit = ::emitError,
        onSuccess: (T) -> Unit,
    ): Job = viewModelScope.launch {
        if (showLoading) updateLoading(+1)
        try {
            when (val result = call()) {
                is Result.Success -> onSuccess(result.data)
                is Result.Error -> onError(result)
            }
        } finally {
            if (showLoading) updateLoading(-1)
        }
    }

    protected fun emitError(error: Result.Error) {
        _errors.trySend(error)
    }

    private fun updateLoading(delta: Int) {
        runningRequests += delta
        _isLoading.value = runningRequests > 0
    }
}
