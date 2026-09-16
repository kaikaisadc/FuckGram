package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.messageObjectClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.logHookError
import org.lyaaz.fuckgram.Reflect
import org.lyaaz.fuckgram.Toggle

object MessageFilterHook : HookModule {

    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.MESSAGE_FILTER)
    }

    override fun hook(): Boolean {
        val blacklistPattern = runCatching {
            settings.messageFilterPattern().toRegex(RegexOption.IGNORE_CASE)
        }.getOrNull() ?: return false

        return runCatching {
            HookModule.hook(
                java.util.ArrayList::class.java.getDeclaredMethod(
                    "add",
                    Object::class.java
                )
            ) { chain ->
                val arg = chain.args.getOrNull(0)
                if (arg == null || !messageObjectClass.isInstance(arg)) return@hook chain.proceed()

                val text: CharSequence?
                val caption: CharSequence?
                try {
                    text = Reflect.getObjectField(arg, "messageText") as? CharSequence
                    caption = Reflect.getObjectField(arg, "caption") as? CharSequence
                    if (!Reflect.getBooleanField(arg, "layoutCreated")) return@hook chain.proceed()
                } catch (t: Throwable) {
                    return@hook chain.proceed()
                }
                if (text == null && caption == null) return@hook chain.proceed()

                if ((text != null && blacklistPattern.containsMatchIn(text)) ||
                    (caption != null && blacklistPattern.containsMatchIn(caption))
                ) {
                    false
                } else {
                    chain.proceed()
                }
            }
        }.onFailure {
            logHookError(java.util.ArrayList::class.java.name, "add", it)
        }.isSuccess
    }
}
