package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.messagesControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object ForceForwardHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.FORCE_FORWARD)
    }

    override fun hook(): Boolean {
        return hookMethods(messagesControllerClass, "isChatNoForwards") { false }
    }
}
