package org.lyaaz.fuckgram

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import org.lyaaz.ui.SwitchPreferenceItem
import org.lyaaz.ui.TextFieldPreference
import org.lyaaz.ui.theme.AppTheme as Theme

class SettingsActivity : ComponentActivity() {

    private var currentUiMode: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        RemotePrefs.start(this)
        currentUiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        setContent {
            Theme {
                SettingsScreen()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newUiMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (newUiMode != currentUiMode) {
            recreate()
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    Theme {
        SettingsScreen()
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settings = remember { Settings(Utils.getPrefs(context), RemotePrefs::sync) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .imePadding()
    ) {
        items(Toggle.entries) { toggle ->
            var checked by remember(toggle) {
                mutableStateOf(settings.isEnabled(toggle))
            }
            SwitchPreferenceItem(
                title = toggle.titleRes,
                checked = checked,
                onCheckedChange = {
                    checked = it
                    settings.setEnabled(toggle, it)
                }
            )
        }

        item {
            var pattern by remember {
                mutableStateOf(settings.messageFilterPattern())
            }
            TextFieldPreference(
                title = R.string.title_message_filter_pattern,
                value = pattern,
                onValueChange = {
                    pattern = it
                    settings.setMessageFilterPattern(it)
                },
                keyboardType = KeyboardType.Text
            )
        }

        item {
            Spacer(
                modifier = Modifier.windowInsetsBottomHeight(
                    WindowInsets.systemBars
                )
            )
        }
    }
}
