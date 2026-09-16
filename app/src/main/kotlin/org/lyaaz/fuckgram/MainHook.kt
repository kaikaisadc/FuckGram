package org.lyaaz.fuckgram

import android.util.Log
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import org.lyaaz.fuckgram.features.*

class MainHook : XposedModule() {
    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        if (param.packageName == BuildConfig.APPLICATION_ID) return
        HookModule.attach(this, param)
        modules.forEach { module ->
            runCatching {
                if (module.enabled()) module.hook()
            }.onFailure {
                log(Log.ERROR, HookModule.TAG, "failed to apply ${module.javaClass.simpleName}", it)
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
