package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.emojiTabsStripClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookConstructors
import org.lyaaz.fuckgram.Toggle

object RemoveEmojiSetHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.REMOVE_EMOJI_SET)
    }

    override fun hook(): Boolean {
        return hookConstructors(emojiTabsStripClass) { chain ->
            val args = chain.args.toMutableList<Any?>()
            when (args.size) {
                6 -> args[3] = false
                8 -> args[4] = false
            }
            chain.proceed(args.toTypedArray())
        }
    }
}
