package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.chatActivityClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object TrackingHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.TRACKING)
    }

    override fun hook(): Boolean {
        return hookMethods(chatActivityClass, "logSponsoredClicked") { null }
    }
}
