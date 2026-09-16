package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.fileLoadOperationClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Reflect
import org.lyaaz.fuckgram.Settings
import org.lyaaz.fuckgram.Toggle

object SpeedUpDownloadHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.SPEED_UP_DOWNLOAD)
    }

    override fun hook(): Boolean {
        return hookMethods(fileLoadOperationClass, "updateParams") { chain ->
            Reflect.setIntField(
                chain.thisObject,
                "downloadChunkSizeBig",
                Settings.DOWNLOAD_CHUNK_SIZE_BIG
            )
            Reflect.setIntField(
                chain.thisObject,
                "maxDownloadRequests",
                Settings.MAX_DOWNLOAD_REQUESTS
            )
            Reflect.setIntField(
                chain.thisObject,
                "maxDownloadRequestsBig",
                Settings.MAX_DOWNLOAD_REQUESTS_BIG
            )
            Reflect.setIntField(
                chain.thisObject,
                "maxCdnParts",
                Settings.MAX_CDN_PARTS
            )
            null
        }
    }
}
