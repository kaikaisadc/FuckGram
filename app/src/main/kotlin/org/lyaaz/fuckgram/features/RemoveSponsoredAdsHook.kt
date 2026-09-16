package org.lyaaz.fuckgram.features

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

    override fun hook(): Boolean {
        return hookMethods(messagesControllerClass, "getSponsoredMessages") { null } and
            hookMethods(chatActivityClass, "addSponsoredMessages") { null }
    }
}
