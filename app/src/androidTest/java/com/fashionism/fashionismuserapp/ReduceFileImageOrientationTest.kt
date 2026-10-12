package com.fashionism.fashionismuserapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.exifinterface.media.ExifInterface
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fashionism.fashionismuserapp.tools.Helper.reduceFileImage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Cameras store the sensor image as is and record how to turn it in EXIF. reduceFileImage re-encodes
 * (dropping EXIF), so it must bake that orientation into the pixels.
 */
@RunWith(AndroidJUnit4::class)
class ReduceFileImageOrientationTest {
    private val cacheDir = InstrumentationRegistry.getInstrumentation().targetContext.cacheDir

    /** 400x200 JPEG: left half red, right half blue, tagged with [orientation]. */
    private fun photo(orientation: Int): File {
        val bitmap = Bitmap.createBitmap(400, 200, Bitmap.Config.ARGB_8888)
        Canvas(bitmap).apply {
            drawRect(0f, 0f, 200f, 200f, Paint().apply { color = Color.RED })
            drawRect(200f, 0f, 400f, 200f, Paint().apply { color = Color.BLUE })
        }
        val file = File(cacheDir, "orientation_$orientation.jpg")
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        ExifInterface(file).apply {
            setAttribute(ExifInterface.TAG_ORIENTATION, orientation.toString())
            saveAttributes()
        }
        return file
    }

    private fun Int.isRed() = Color.red(this) > 200 && Color.blue(this) < 60
    private fun Int.isBlue() = Color.blue(this) > 200 && Color.red(this) < 60

    @Test
    fun rotate90_becomesPortraitWithTheLeftHalfOnTop() {
        val file = photo(ExifInterface.ORIENTATION_ROTATE_90)

        reduceFileImage(file)

        val result = BitmapFactory.decodeFile(file.path)
        assertEquals(200, result.width)
        assertEquals(400, result.height)
        assertTrue("top should be the red (left) half", result.getPixel(100, 50).isRed())
        assertTrue("bottom should be the blue (right) half", result.getPixel(100, 350).isBlue())
        // Pixels are upright now, so no viewer may rotate them again
        assertEquals(ExifInterface.ORIENTATION_UNDEFINED, ExifInterface(file).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED))
    }

    @Test
    fun flipHorizontal_swapsLeftAndRight() {
        val file = photo(ExifInterface.ORIENTATION_FLIP_HORIZONTAL)

        reduceFileImage(file)

        val result = BitmapFactory.decodeFile(file.path)
        assertEquals(400, result.width)
        assertTrue("left should now be blue", result.getPixel(100, 100).isBlue())
        assertTrue("right should now be red", result.getPixel(300, 100).isRed())
    }

    @Test
    fun normalOrientation_isUnchanged() {
        val file = photo(ExifInterface.ORIENTATION_NORMAL)

        reduceFileImage(file)

        val result = BitmapFactory.decodeFile(file.path)
        assertEquals(400, result.width)
        assertTrue(result.getPixel(100, 100).isRed())
        assertTrue(result.getPixel(300, 100).isBlue())
    }
}
