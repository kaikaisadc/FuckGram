package org.lyaaz.fuckgram

import android.content.Context
import android.content.SharedPreferences

object Utils {
    /**
     * Local store backing the settings UI. The hooked app does not read this file: changes are
     * mirrored into the framework's remote preference store by [RemotePrefs].
     */
    fun getPrefs(context: Context): SharedPreferences {
        val prefsName = "${context.packageName}_preferences"
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }
}
