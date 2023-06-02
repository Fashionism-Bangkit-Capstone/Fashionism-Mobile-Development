package com.alcorp.fashionism_umkm.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.alcorp.fashionism_umkm.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object Helper {
    fun checkEmailFormat(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}