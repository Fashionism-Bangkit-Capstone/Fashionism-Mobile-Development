package com.fashionism.fashionismuserapp.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.View
import androidx.annotation.AnimRes
import androidx.core.app.ActivityOptionsCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object Helper {
    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    fun showLoading(isLoading: Boolean, view: View) {
        view.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun shortenText(text: String, maxLength: Int = 20): String {
        if (text.length <= maxLength) {
            return text
        }

        val shortenedText = text.substring(0, maxLength)
        return "$shortenedText..."
    }

}
fun Context.startActivityWithAnim(intent: Intent, @AnimRes enter: Int, @AnimRes exit: Int) =
    startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(this, enter, exit).toBundle())

fun Activity.finishWithAnim(@AnimRes enter: Int, @AnimRes exit: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, enter, exit)
        finish()
    } else {
        finish()
        @Suppress("DEPRECATION")
        overridePendingTransition(enter, exit)
    }
}
