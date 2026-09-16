package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.messageObjectClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookModule.Companion.spoilerEffectClass
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object SpoilersHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.SPOILERS)
    }

    override fun hook(): Boolean {
        return hookMethods(spoilerEffectClass, "addSpoilers") { null } and
            hookMethods(messageObjectClass, "hasMediaSpoilers") { false }
    }
}
