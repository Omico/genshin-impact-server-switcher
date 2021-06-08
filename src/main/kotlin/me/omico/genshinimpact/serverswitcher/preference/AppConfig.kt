package me.omico.genshinimpact.serverswitcher.preference

import java.util.prefs.Preferences

object AppConfig {

    private const val KEY_GAME_DIR = "gameDir"
    private const val KEY_LATEST_SELECTED_DIR = "latestSelectedDir"

    private val preferences = Preferences.userRoot().node(javaClass.packageName.replace(".", "/"))

    var gameRootDir: String
        get() = preferences.get(KEY_GAME_DIR, "")
        set(value) = preferences.put(KEY_GAME_DIR, value)

    var latestSelectedDir: String
        get() = preferences.get(KEY_LATEST_SELECTED_DIR, "")
        set(value) = preferences.put(KEY_LATEST_SELECTED_DIR, value)
}
