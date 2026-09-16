# FuckGram

An LSPosed / Xposed module that removes annoyances from Telegram clients.

This repository is a maintained fork of [FuckAPK/FuckGram](https://github.com/FuckAPK/FuckGram).

## Features

| Toggle | What it does |
| --- | --- |
| Enable force forward | Removes the "restrict saving content" / no-forwards limit |
| Remove sponsored ads | Drops sponsored messages from channels |
| Disable reaction popup | Suppresses the reaction popup |
| Disable double-tap quick reaction | Disables the double-tap reaction shortcut |
| Lock premium features | Forces `premiumLocked` so premium UI stays locked |
| Remove emoji set from sticker panel | Hides the emoji tab in the sticker panel |
| Speed up download | Raises download chunk size / request counts |
| Force highest video quality | Selects the highest available video quality |
| Disable tracking | Blocks sponsored-message click tracking |
| Disable chat swipe action | Disables swipe actions in the chat list |
| Disable channel bottom button | Disables the channel mute/unmute bottom button |
| Disable stories | Hides stories |
| Prohibit spoilers | Renders spoiler media without the blur |
| Enable message filter | Drops messages whose text/caption matches a regex |

The message filter is off by default and matches against the configurable pattern in the settings screen.

## Requirements

- Android 8.0+ (API 26)
- LSPosed or another Xposed framework exposing the classic `de.robv.android.xposed` API
- A Telegram client built from the Telegram Android sources (classes under `org.telegram.*`)

## Supported clients

| Client | Package |
| --- | --- |
| Telegram | `org.telegram.messenger` |
| Telegram (web build) | `org.telegram.messenger.web` |
| Forkgram | `org.forkclient.messenger.beta` |
| MercuryGram | `it.belloworld.mercurygram` |
| Nekogram | `tw.nekomimi.nekogram` |

## Build

Requires JDK 21 and an Android SDK with API 36.

```sh
./gradlew assembleDebug
```

Release builds are minified, resource-shrunk and signed when a `signing.properties` file is
present at the repository root (or in `app/`) with the following keys:

```properties
storeFilePath=/absolute/path/to/keystore.jks
storePassword=...
keyPassword=...
keyAlias=...
```

Without that file the release variant is built unsigned, which keeps local builds working.

## CI

`.github/workflows/build.yml` is self-contained (it does not reuse an external workflow):

- pull requests and pushes to `main` run an unsigned `assembleDebug` (no secrets)
- tag pushes build the signed release APK and attach it to the GitHub release

Release signing secrets: `KEYSTORE_FILE` (base64-encoded keystore), `KEYSTORE_PASSWORD`,
`KEY_PASSWORD`, `KEY_ALIAS`.

## Notes

Hooks are tied to internal Telegram class and method names and are therefore version
sensitive. Each module is isolated: if one hook fails to apply, it is logged to the Xposed log
and the remaining modules still run. Enable logging in LSPosed when debugging.

## License

GPL-3.0. See [LICENSE](LICENSE).
