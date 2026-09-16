package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.chatActivityClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object QuickReactionHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.QUICK_REACTION)
    }

    override fun hook(): Boolean {
        return hookMethods(chatActivityClass, "selectReaction") { chain ->
            if (chain.args.getOrNull(6) == true) null else chain.proceed()
        }
    }
}
