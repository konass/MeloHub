package com.example.socialapp.utils


import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("tag", Context.MODE_PRIVATE)
    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }
    fun saveString(KEY_NAME: String, value: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(KEY_NAME, value)
        editor.apply()
    }
    fun getValueInt(KEY_NAME: String): Int {
        return preferences.getInt(KEY_NAME, 0)
    }
    fun getValueString(KEY_NAME: String): String? {
        return preferences.getString(KEY_NAME, "")
    }
    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }
}