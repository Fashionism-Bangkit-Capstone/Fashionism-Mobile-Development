package com.fashionism.fashionismuserapp.tools

import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import java.util.*

object LanguageUtil {
    private const val PREF_LANGUAGE = "pref_language"
    private const val DEFAULT_LANGUAGE = "en"

    fun setLanguage(context: Context, languageCode: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putString(PREF_LANGUAGE, languageCode).apply()
    }

    fun getLanguage(context: Context): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(PREF_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun applyLanguage(context: Context) {
        val languageCode = getLanguage(context)
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
