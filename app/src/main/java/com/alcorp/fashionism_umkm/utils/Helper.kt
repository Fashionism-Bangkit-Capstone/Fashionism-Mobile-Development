package com.alcorp.fashionism_umkm.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

object Helper {
    fun checkEmailFormat(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}

// Draw behind system bars, then pad root so content stays clear of status/nav bars, cutout and keyboard.
// padTop = false lets a full-bleed header draw under the (hidden) status bar / cutout.
fun ComponentActivity.setupEdgeToEdge(root: View, padTop: Boolean = true) {
    enableEdgeToEdge()
    ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or
                WindowInsetsCompat.Type.displayCutout() or
                WindowInsetsCompat.Type.ime()
        )
        v.setPadding(bars.left, if (padTop) bars.top else 0, bars.right, bars.bottom)
        WindowInsetsCompat.CONSUMED
    }
}

fun ComponentActivity.hideStatusBar() {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        hide(WindowInsetsCompat.Type.statusBars())
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}
