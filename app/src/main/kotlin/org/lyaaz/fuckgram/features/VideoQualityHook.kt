package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookModule.Companion.videoPlayerClass
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Reflect
import org.lyaaz.fuckgram.Toggle

object VideoQualityHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.VIDEO_QUALITY)
    }

    override fun hook(): Boolean {
        return hookMethods(videoPlayerClass, "preparePlayer") { chain ->
            val args = chain.args.toMutableList<Any?>()
            forceHighestQuality(args)
            chain.proceed(args.toTypedArray())
        }
    }

    private fun forceHighestQuality(args: MutableList<Any?>) {
        runCatching {
            val qualities = args.firstOrNull { it is List<*> } as? List<*> ?: return
            val targetIndex = args.indexOf(qualities) + 1
            if (targetIndex >= args.size) return

            var best: Any? = null
            var bestPixels = -1L
            var bestIsOriginal = false
            for (quality in qualities) {
                if (quality == null) continue
                val width: Int
                val height: Int
                val original: Boolean
                try {
                    width = Reflect.getIntField(quality, "width")
                    height = Reflect.getIntField(quality, "height")
                    original = Reflect.getBooleanField(quality, "original")
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
                args[targetIndex] = best
            }
        }
    }
}
