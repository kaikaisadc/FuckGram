package org.lyaaz.fuckgram.features

import android.view.View
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.chatActivityClass
import org.lyaaz.fuckgram.HookModule.Companion.localeControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.rStringClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object ChannelBottomButtonHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.CHANNEL_BOTTOM_BUTTON)
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        val unMuteStr by lazy { getLocaleString("ChannelUnmute") }
        val muteStr by lazy { getLocaleString("ChannelMute") }

        return hookMethods(chatActivityClass, "updateBottomOverlay", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {
                val bottomOverlayChatText = XposedHelpers.getObjectField(
                    param.thisObject,
                    "bottomOverlayChatText"
                ) as? View ?: return
                val text = XposedHelpers.getObjectField(
                    bottomOverlayChatText,
                    "lastText"
                ) as? CharSequence ?: return

                if (text.toString() == unMuteStr || text.toString() == muteStr) {
                    bottomOverlayChatText.isEnabled = false
                }
            }
        })
    }

    private fun getLocaleString(key: String): String? {
        return XposedHelpers.callStaticMethod(
            localeControllerClass,
            "getString",
            key,
            XposedHelpers.getStaticIntField(rStringClass, key)
        ) as? String
    }
}
