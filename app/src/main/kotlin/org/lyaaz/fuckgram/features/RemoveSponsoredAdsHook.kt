package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.chatActivityClass
import org.lyaaz.fuckgram.HookModule.Companion.messagesControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object RemoveSponsoredAdsHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.SPONSORED_ADS)
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookMethods(
            messagesControllerClass,
            "getSponsoredMessages",
            XC_MethodReplacement.returnConstant(null)
        ) and hookMethods(
            chatActivityClass,
            "addSponsoredMessages",
            XC_MethodReplacement.returnConstant(null)
        )
    }
}