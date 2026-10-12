package com.fashionism.fashionismuserapp.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.view.View
import androidx.annotation.AnimRes
import androidx.core.app.ActivityOptionsCompat
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.roundToInt

object Helper {
    /**
     * Shrinks a photo in place before upload: scales the long side down to [maxDimension] px,
     * then lowers JPEG quality until it fits in [maxBytes] (stopping at [MIN_JPEG_QUALITY]).
     * Pixels are turned upright from the EXIF orientation, because re-encoding drops EXIF:
     * without this, camera photos upload sideways. A file that is not a decodable image is returned untouched.
     */
    fun reduceFileImage(file: File, maxBytes: Int = 1_000_000, maxDimension: Int = 1920): File {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.path, bounds)
        val options = BitmapFactory.Options().apply {
            // Decode already shrunk: a full-size 50 MP photo does not fit in memory
            inSampleSize = inSampleSizeFor(bounds.outWidth, bounds.outHeight, maxDimension)
        }
        val decoded = BitmapFactory.decodeFile(file.path, options) ?: return file
        val scaled = decoded.scaledToFit(maxDimension)
        val bitmap = scaled.upright(runCatching { ExifInterface(file) }.getOrNull())
        try {
            var quality = 95
            var bytes = bitmap.toJpeg(quality)
            while (bytes.size > maxBytes && quality > MIN_JPEG_QUALITY) {
                quality = maxOf(quality - 10, MIN_JPEG_QUALITY)
                bytes = bitmap.toJpeg(quality)
            }
            file.writeBytes(bytes)
        } finally {
            bitmap.recycle()
            scaled.recycle()
            decoded.recycle()
        }
        return file
    }

    /** Largest power of two that still decodes the long side at [maxDimension] px or more. */
    internal fun inSampleSizeFor(width: Int, height: Int, maxDimension: Int): Int {
        val longSide = maxOf(width, height)
        var sampleSize = 1
        while (longSide / (sampleSize * 2) >= maxDimension) sampleSize *= 2
        return sampleSize
    }

    private fun Bitmap.scaledToFit(maxDimension: Int): Bitmap {
        val longSide = maxOf(width, height)
        if (longSide <= maxDimension) return this
        val scale = maxDimension.toFloat() / longSide
        return Bitmap.createScaledBitmap(this, (width * scale).roundToInt(), (height * scale).roundToInt(), true)
    }

    /** [exif] is null when the metadata is unreadable: keep the pixels as they are. */
    private fun Bitmap.upright(exif: ExifInterface?): Bitmap {
        if (exif == null) return this
        val matrix = Matrix().apply {
            setRotate(exif.rotationDegrees.toFloat())
            if (exif.isFlipped) postScale(-1f, 1f)
        }
        if (matrix.isIdentity) return this
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private fun Bitmap.toJpeg(quality: Int): ByteArray =
        ByteArrayOutputStream().also { compress(Bitmap.CompressFormat.JPEG, quality, it) }.toByteArray()

    private const val MIN_JPEG_QUALITY = 40

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
