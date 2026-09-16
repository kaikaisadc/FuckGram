package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.mediaDataControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object ReactionPopupHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.REACTION_POPUP)
    }

    override fun hook(): Boolean {
        return hookMethods(mediaDataControllerClass, "getEnabledReactionsList") { emptyList<Any>() }
    }
}
