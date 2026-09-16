package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.chatActivityClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object QuickReactionHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.QUICK_REACTION)
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookMethods(chatActivityClass, "selectReaction", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (param.args.getOrNull(6) == true) {
                    param.result = null
                }
            }
        })
    }
}