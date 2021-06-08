package me.omico.genshinimpact.serverswitcher.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LauncherConfig(
    val cps: String,
    val channel: Int,
    @SerialName(value = "sub_channel") val subChannel: Int,
    @SerialName(value = "is_first_exit") val isFirstExit: Boolean,
    @SerialName(value = "exit_type") val exitType: Int,
    @SerialName(value = "game_install_path") val gameInstallPath: String,
    @SerialName(value = "game_dynamic_bg_name") val gameDynamicBackgroundName: String,
    @SerialName(value = "game_dynamic_bg_md5") val gameDynamicBackgroundMd5: String,
    @SerialName(value = "game_start_name") val gameStartName: String,
)
