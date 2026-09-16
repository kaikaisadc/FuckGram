package org.lyaaz.fuckgram

import android.content.Context
import android.util.Log
import io.github.libxposed.service.XposedService
import io.github.libxposed.service.XposedServiceHelper

/**
 * Mirrors the module's local preferences into the Xposed framework's remote preference store, which
 * is what the hooked app reads. The service binds asynchronously, so every write is applied to the
 * remote store when connected and the whole local set is pushed on bind.
 */
object RemotePrefs {
    private const val TAG = "FuckGram"

    @Volatile
    private var service: XposedService? = null

    @Volatile
    private var appContext: Context? = null

    private var started = false

    fun start(context: Context) {
        appContext = context.applicationContext
        if (started) {
            syncAll()
            return
        }
        started = true
        XposedServiceHelper.registerListener(object : XposedServiceHelper.OnServiceListener {
            override fun onServiceBind(service: XposedService) {
                this@RemotePrefs.service = service
                syncAll()
            }

            override fun onServiceDied(service: XposedService) {
                if (this@RemotePrefs.service === service) {
                    this@RemotePrefs.service = null
                }
            }
        })
    }

    fun sync(key: String, value: Any?) {
        val service = service ?: return
        val editor = service.getRemotePreferences(HookModule.PREFS_GROUP).edit()
        editor.putValue(key, value)
        editor.apply()
    }

    private fun syncAll() {
        val service = service ?: return
        val context = appContext ?: return
        val editor = service.getRemotePreferences(HookModule.PREFS_GROUP).edit()
        Utils.getPrefs(context).all.forEach { (key, value) -> editor.putValue(key, value) }
        editor.apply()
    }

    private fun android.content.SharedPreferences.Editor.putValue(key: String, value: Any?) {
        when (value) {
            null -> remove(key)
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Set<*> -> {
                @Suppress("UNCHECKED_CAST")
                putStringSet(key, value as Set<String>)
            }
            else -> Log.w(TAG, "Unsupported preference type for $key: ${value.javaClass.name}")
        }
    }
}
