package org.lyaaz.fuckgram.features

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookModule.Companion.videoPlayerClass
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object VideoQualityHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.VIDEO_QUALITY)
    }

    override fun hook(lpparam: XC_LoadPackage.LoadPackageParam): Boolean {
        return hookMethods(videoPlayerClass, "preparePlayer", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                forceHighestQuality(param)
            }
        })
    }

    private fun forceHighestQuality(param: MethodHookParam) {
        val args = param.args ?: return
        if (args.size < 2) return
        val qualities = args[0] as? List<*> ?: return

        var best: Any? = null
        var bestPixels = -1L
        var bestIsOriginal = false
        for (quality in qualities) {
            if (quality == null) continue
            val width: Int
            val height: Int
            val original: Boolean
            try {
                width = XposedHelpers.getIntField(quality, "width")
                height = XposedHelpers.getIntField(quality, "height")
                original = XposedHelpers.getBooleanField(quality, "original")
            } catch (t: Throwable) {
                continue
            }
            val pixels = width.toLong() * height.toLong()
            if (pixels > bestPixels || (pixels == bestPixels && original && !bestIsOriginal)) {
                best = quality
                bestPixels = pixels
                bestIsOriginal = original
            }
        }
        if (best != null) {
            args[1] = best
        }
    }
}
