package org.lyaaz.fuckgram

import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.core.content.edit

enum class Toggle(
    val key: String,
    val default: Boolean,
    @StringRes val titleRes: Int
) {
    FORCE_FORWARD("enable_force_forward", true, R.string.title_enable_force_forward),
    SPONSORED_ADS("enable_remove_sponsored_ads", true, R.string.title_enable_remove_sponsored_ads),
    REACTION_POPUP("disable_reaction_popup", true, R.string.title_disable_reaction_popup),
    QUICK_REACTION("disable_quick_reaction", true, R.string.title_disable_quick_reaction),
    PREMIUM_FEATURES("lock_premium_features", true, R.string.title_lock_premium_features),
    REMOVE_EMOJI_SET("enable_remove_emoji_set", true, R.string.title_enable_remove_emoji_set),
    SPEED_UP_DOWNLOAD("enable_speed_up_download", true, R.string.title_enable_speed_up_download),
    TRACKING("disable_tracking", true, R.string.title_disable_tracking),
    CHAT_SWIPE("disable_chat_swipe", true, R.string.title_disable_chat_swipe),
    CHANNEL_BOTTOM_BUTTON("disable_channel_bottom_button", true, R.string.title_disable_channel_bottom_button),
    STORIES("disable_stories", true, R.string.title_disable_stories),
    SPOILERS("prohibit_spoilers", true, R.string.title_prohibit_spoilers),
    MESSAGE_FILTER("enable_message_filter", false, R.string.title_enable_message_filter)
}

class Settings(private val prefs: SharedPreferences) {

    fun isEnabled(toggle: Toggle): Boolean {
        return prefs.getBoolean(toggle.key, toggle.default)
    }

    fun setEnabled(toggle: Toggle, value: Boolean) {
        prefs.edit { putBoolean(toggle.key, value) }
    }

    fun messageFilterPattern(): String {
        return prefs.getString(PREF_MESSAGE_FILTER_PATTERN, DEFAULT_MESSAGE_FILTER_PATTERN).orEmpty()
    }

    fun setMessageFilterPattern(value: String) {
        prefs.edit { putString(PREF_MESSAGE_FILTER_PATTERN, value) }
    }

    companion object {
        const val PREF_MESSAGE_FILTER_PATTERN = "message_filter_pattern"
        const val DEFAULT_MESSAGE_FILTER_PATTERN = "车队|互推|飞机杯|优惠|机场|推广"

        private const val DEFAULT_MAX_FILE_SIZE = 1024L * 1024L * 2000L
        const val DOWNLOAD_CHUNK_SIZE_BIG = 1024 * 1024
        const val MAX_DOWNLOAD_REQUESTS = 8
        const val MAX_DOWNLOAD_REQUESTS_BIG = 8
        const val MAX_CDN_PARTS = (DEFAULT_MAX_FILE_SIZE / DOWNLOAD_CHUNK_SIZE_BIG).toInt()
    }
}
