package com.fashionism.fashionismuserapp.ui.common

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fashionism.fashionismuserapp.R
import com.fashionism.fashionismuserapp.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/** For one-shot events (errors, navigation). In a Fragment, call it on viewLifecycleOwner. */
fun <T> LifecycleOwner.collectWhenStarted(flow: Flow<T>, action: (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) { flow.collect { action(it) } }
    }
}

fun Context.showToast(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.errorText(error: Result.Error): String = when {
    error.code == null && error.message == null -> getString(R.string.error_no_connection)
    else -> error.message ?: getString(R.string.error_generic)
}

fun Context.showError(error: Result.Error) = showToast(errorText(error))
