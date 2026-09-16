package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.messagesControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookModule.Companion.storiesControllerClass
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object StoriesHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.STORIES)
    }

    override fun hook(): Boolean {
        return hookMethods(storiesControllerClass, "hasStories") { false } and
            hookMethods(messagesControllerClass, "storiesEnabled") { false }
    }
}
