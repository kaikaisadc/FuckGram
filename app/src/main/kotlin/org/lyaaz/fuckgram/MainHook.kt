package org.lyaaz.fuckgram

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.lyaaz.fuckgram.features.*

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        HookModule.lpparam = lpparam
        modules.forEach { module ->
            runCatching {
                if (module.enabled()) module.hook(lpparam)
            }.onFailure {
                XposedBridge.log("FuckGram: failed to apply ${module.javaClass.simpleName}")
                XposedBridge.log(it)
            }
        }
    }

    companion object {
        private val modules = listOf<HookModule>(
            ChannelBottomButtonHook,
            ChatSwipeHook,
            ForceForwardHook,
            MessageFilterHook,
            PremiumFeaturesHook,
            QuickReactionHook,
            ReactionPopupHook,
            RemoveEmojiSetHook,
            RemoveSponsoredAdsHook,
            SpeedUpDownloadHook,
            VideoQualityHook,
            SpoilersHook,
            StoriesHook,
            TrackingHook
        )
    }
}
