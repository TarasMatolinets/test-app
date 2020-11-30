package com.demo.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.demo.demo.R
import com.demo.mvvm.model.ShapeParams
import com.demo.mvvm.view.fragment.ShapeType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.HashMap

object SharedPreferencesHelper {
    private lateinit var sharedPref: SharedPreferences

    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(
            context.getString(R.string.demo_shared_preferences),
            MODE_PRIVATE
        )
    }

    var passCodeOn: Boolean
        get() = sharedPref.getBoolean(PASSCODE_OFF, true)
        set(value) = sharedPref.edit().putBoolean(PASSCODE_OFF, value).apply()

    var lockApp: Boolean
        get() = sharedPref.getBoolean(LOCK_APP, false)
        set(value) = sharedPref.edit().putBoolean(LOCK_APP, value).apply()

    var passCode: String?
        get() = sharedPref.getString(PASS_CODE, null)
        set(value) = sharedPref.edit().putString(PASS_CODE, value).apply()

    var showPassCodeScreen: Boolean
        get() = sharedPref.getBoolean(SHOW_PASS_CODE, false)
        set(value) = sharedPref.edit().putBoolean(SHOW_PASS_CODE, value).apply()

    var shapeTypeParam: HashMap<ShapeType, ShapeParams>?
        get() {
            return Gson().fromJson(
                sharedPref.getString(CUSTOM_SHAPE, ""),
                object : TypeToken<Map<ShapeType, ShapeParams>>() {}.type
            )
        }
        set(value) {
            sharedPref.edit().putString(CUSTOM_SHAPE, Gson().toJson(value)).apply()
        }

    private const val PASSCODE_OFF = "PASSCODE_OFF_KEY"
    private const val LOCK_APP = "LOCK_APP_KEY"
    private const val PASS_CODE = "PASS_CODE_KEY"
    private const val SHOW_PASS_CODE = "SHOW_PASS_CODE_KEY"
    private const val CUSTOM_SHAPE = "CUSTOM_SHAPE_KEY"
}