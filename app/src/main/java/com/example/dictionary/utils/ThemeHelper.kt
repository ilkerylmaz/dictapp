package com.example.dictionary.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.dictionary.R

object ThemeHelper {
    private const val THEME_PREFS = "theme_prefs"
    private const val KEY_THEME = "theme_mode"

    fun applyTheme(activity: Activity, isDarkMode: Boolean) {
        val theme = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(theme)
        saveThemeMode(activity, isDarkMode)
        
        // Temayı hemen uygula
        activity.recreate()
    }

    fun isDarkMode(context: Context): Boolean {
        val prefs = getPreferences(context)
        return prefs.getBoolean(KEY_THEME, true)
    }

    private fun saveThemeMode(context: Context, isDarkMode: Boolean) {
        val prefs = getPreferences(context)
        prefs.edit().putBoolean(KEY_THEME, isDarkMode).apply()
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
    }
} 