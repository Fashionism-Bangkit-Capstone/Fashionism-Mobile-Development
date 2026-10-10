package com.fashionism.fashionismuserapp.tools

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat

object LanguageUtil {
    private const val PREF_LANGUAGE = "pref_language"
    private const val DEFAULT_LANGUAGE = "en"

    // Same file PreferenceManager.getDefaultSharedPreferences used, so the saved language is kept
    private fun prefs(context: Context) =
        context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)

    fun setLanguage(context: Context, languageCode: String) {
        prefs(context).edit { putString(PREF_LANGUAGE, languageCode) }
    }

    fun getLanguage(context: Context): String {
        return prefs(context).getString(PREF_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun applyLanguage(context: Context) {
        val locales = LocaleListCompat.forLanguageTags(getLanguage(context))
        // Skip when unchanged: setApplicationLocales recreates running activities
        if (AppCompatDelegate.getApplicationLocales() != locales) {
            AppCompatDelegate.setApplicationLocales(locales)
        }
    }
}
