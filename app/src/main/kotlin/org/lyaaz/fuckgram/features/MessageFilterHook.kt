package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.messageObjectClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.logHookError
import org.lyaaz.fuckgram.Toggle

object MessageFilterHook : HookModule {

    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.MESSAGE_FILTER)
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        val blacklistPattern = runCatching {
            settings.messageFilterPattern().toRegex(RegexOption.IGNORE_CASE)
        }.getOrNull() ?: return false

        return runCatching {
            XposedBridge.hookMethod(
                java.util.ArrayList::class.java.getDeclaredMethod(
                    "add",
                    Object::class.java
                ), object : XC_MethodHook() {
                    @Throws(Throwable::class)
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val arg = param.args?.getOrNull(0) ?: return
                        if (!messageObjectClass.isInstance(arg)) return

                        val text: CharSequence?
                        val caption: CharSequence?
                        try {
                            text = XposedHelpers.getObjectField(arg, "messageText") as? CharSequence
                            caption = XposedHelpers.getObjectField(arg, "caption") as? CharSequence
                            if (!XposedHelpers.getBooleanField(arg, "layoutCreated")) return
                        } catch (t: Throwable) {
                            return
                        }
                        if (text == null && caption == null) return

                        if ((text != null && blacklistPattern.containsMatchIn(text)) ||
                            (caption != null && blacklistPattern.containsMatchIn(caption))
                        ) {
                            param.result = false
                        }
                    }
                }
            )
        }.onFailure {
            logHookError(java.util.ArrayList::class.java.name, "add", it)
        }.isSuccess
    }
}