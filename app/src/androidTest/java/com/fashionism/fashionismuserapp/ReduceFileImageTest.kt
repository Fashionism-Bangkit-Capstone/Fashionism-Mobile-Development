package com.fashionism.fashionismuserapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fashionism.fashionismuserapp.tools.Helper.reduceFileImage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class ReduceFileImageTest {
    private val cacheDir = InstrumentationRegistry.getInstrumentation().targetContext.cacheDir

    @Test
    fun noisyHighResolutionPhoto_fitsUnderOneMegabyte() {
        // Random pixels barely compress: the old loop pushed JPEG quality below zero and crashed on this
        val file = File(cacheDir, "noise.jpg")
        val noise = Bitmap.createBitmap(4000, 3000, Bitmap.Config.ARGB_8888)
        val random = Random(42)
        val row = IntArray(noise.width)
        for (y in 0 until noise.height) {
            for (x in row.indices) row[x] = random.nextInt() or 0xFF000000.toInt()
            noise.setPixels(row, 0, noise.width, 0, y, noise.width, 1)
        }
        file.outputStream().use { noise.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        noise.recycle()
        assertTrue(file.length() > 1_000_000)

        reduceFileImage(file)

        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.path, bounds)
        assertEquals(1920, maxOf(bounds.outWidth, bounds.outHeight))
        assertTrue("size was ${file.length()}", file.length() <= 1_000_000)
    }

    @Test
    fun fileThatIsNotAnImage_isLeftUntouched() {
        val file = File(cacheDir, "not-an-image.jpg").apply { writeText("hello") }

        reduceFileImage(file)

        assertEquals("hello", file.readText())
    }
}
