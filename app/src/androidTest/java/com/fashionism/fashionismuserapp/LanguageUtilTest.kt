package com.fashionism.fashionismuserapp

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fashionism.fashionismuserapp.tools.LanguageUtil
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LanguageUtilTest {
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun clearSavedLanguage() {
        context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)
            .edit().clear().commit()
    }

    @Test
    fun defaultsToEnglish() {
        assertEquals("en", LanguageUtil.getLanguage(context))
    }

    @Test
    fun savedLanguageIsReadBack() {
        LanguageUtil.setLanguage(context, "in")
        assertEquals("in", LanguageUtil.getLanguage(context))
    }

    @Test
    fun appIdMatchesFlavor() {
        assertEquals(BuildConfig.APPLICATION_ID, context.packageName)
    }
}
