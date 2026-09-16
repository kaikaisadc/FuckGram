package org.lyaaz.fuckgram

import android.content.SharedPreferences
import android.util.Log
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import java.lang.reflect.Executable

interface HookModule {
    fun hook(): Boolean
    fun enabled(): Boolean

    companion object {
        const val PREFS_GROUP = "fuckgram_settings"
        const val TAG = "FuckGram"

        private lateinit var module: XposedModule
        private lateinit var lpparam: XposedModuleInterface.PackageLoadedParam

        fun attach(entry: XposedModule, param: XposedModuleInterface.PackageLoadedParam) {
            module = entry
            lpparam = param
        }

        fun log(msg: String) {
            module.log(Log.ERROR, TAG, msg)
        }

        fun log(msg: String, t: Throwable) {
            module.log(Log.ERROR, TAG, msg, t)
        }

        fun hook(origin: Executable, hooker: XposedInterface.Hooker) {
            module.hook(origin).intercept(hooker)
        }

        private val prefs: SharedPreferences by lazy { module.getRemotePreferences(PREFS_GROUP) }
        val settings: Settings by lazy { Settings(prefs) }

        private val classCache = HashMap<String, Class<*>>()

        val getClass = { name: String ->
            classCache.getOrPut(name) { lpparam.getDefaultClassLoader().loadClass(name) }
        }

        val messagesControllerClass: Class<*> by lazy { getClass("org.telegram.messenger.MessagesController") }
        val chatActivityClass: Class<*> by lazy { getClass("org.telegram.ui.ChatActivity") }
        val mediaDataControllerClass: Class<*> by lazy { getClass("org.telegram.messenger.MediaDataController") }
        val fileLoadOperationClass: Class<*> by lazy { getClass("org.telegram.messenger.FileLoadOperation") }
        val dialogCellClass: Class<*> by lazy { getClass("org.telegram.ui.Cells.DialogCell") }
        val emojiTabsStripClass: Class<*> by lazy { getClass("org.telegram.ui.Components.EmojiTabsStrip") }
        val sharedConfigClass: Class<*> by lazy { getClass("org.telegram.messenger.SharedConfig") }
        val spoilerEffectClass: Class<*> by lazy { getClass("org.telegram.ui.Components.spoilers.SpoilerEffect") }
        val localeControllerClass: Class<*> by lazy { getClass("org.telegram.messenger.LocaleController") }
        val rStringClass: Class<*> by lazy { getClass("org.telegram.messenger.R\$string") }
        val messageObjectClass: Class<*> by lazy { getClass("org.telegram.messenger.MessageObject") }
        val dialogSwipeControllerClass: Class<*> by lazy { getClass("org.telegram.ui.DialogsActivity\$SwipeController") }
        val storiesControllerClass: Class<*> by lazy { getClass("org.telegram.ui.Stories.StoriesController") }
        val videoPlayerClass: Class<*> by lazy { getClass("org.telegram.ui.Components.VideoPlayer") }
    }
}
