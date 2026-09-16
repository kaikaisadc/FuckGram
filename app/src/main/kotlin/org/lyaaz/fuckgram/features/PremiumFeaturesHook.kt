package org.lyaaz.fuckgram.features

import org.lyaaz.fuckgram.HookModule
import org.lyaaz.fuckgram.HookModule.Companion.messagesControllerClass
import org.lyaaz.fuckgram.HookModule.Companion.settings
import org.lyaaz.fuckgram.HookUtils.hookConstructors
import org.lyaaz.fuckgram.HookUtils.hookMethods
import org.lyaaz.fuckgram.Reflect
import org.lyaaz.fuckgram.Toggle

object PremiumFeaturesHook : HookModule {
    override fun enabled(): Boolean {
        return settings.isEnabled(Toggle.PREMIUM_FEATURES)
    }

    override fun hook(): Boolean {
        return hookConstructors(messagesControllerClass) { chain ->
            val result = chain.proceed()
            Reflect.setBooleanField(chain.thisObject, "premiumLocked", true)
            result
        } and hookMethods(messagesControllerClass, "applyAppConfig") { chain ->
            val result = chain.proceed()
            Reflect.setBooleanField(chain.thisObject, "premiumLocked", true)
            result
        }
    }
}
