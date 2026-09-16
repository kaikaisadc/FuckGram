package org.lyaaz.fuckgram.features

import android.view.View
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.chatActivityClass
import org.lyaaz.fuckgram.HookModule.Companion.localeControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.rStringClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Reflect
import org.lyaaz.fuckgram.Toggle

object ChannelBottomButtonHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.CHANNEL_BOTTOM_BUTTON)
    }

    override fun hook(): Boolean {
        val unMuteStr by lazy { getLocaleString("ChannelUnmute") }
        val muteStr by lazy { getLocaleString("ChannelMute") }

        return hookMethods(chatActivityClass, "updateBottomOverlay") { chain ->
            val result = chain.proceed()
            val bottomOverlayChatText = Reflect.getObjectField(
                chain.thisObject,
                "bottomOverlayChatText"
            ) as? View
            if (bottomOverlayChatText != null) {
                val text = Reflect.getObjectField(
                    bottomOverlayChatText,
                    "lastText"
                ) as? CharSequence
                if (text != null && (text.toString() == unMuteStr || text.toString() == muteStr)) {
                    bottomOverlayChatText.isEnabled = false
                }
            }
            result
        }
    }

    private fun getLocaleString(key: String): String? {
        return Reflect.callStaticMethod(
            localeControllerClass,
            "getString",
            key,
            Reflect.getStaticIntField(rStringClass, key)
        ) as? String
    }
}
