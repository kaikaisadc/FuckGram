package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.dialogCellClass
import org.lyaaz.fuckgram.HookModule.Companion.dialogSwipeControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookModule.Companion.sharedConfigClass
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Toggle

object ChatSwipeHook : HookModule {

    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.CHAT_SWIPE)
    }

    override fun hook(): Boolean {
        return hookMethods(dialogCellClass, "getTranslationX") { 0f } and
            hookMethods(dialogCellClass, "setTranslationX") { null } and
            hookMethods(dialogSwipeControllerClass, "onSwiped") { null } and
            hookMethods(sharedConfigClass, "getChatSwipeAction") { -1 }
    }
}
