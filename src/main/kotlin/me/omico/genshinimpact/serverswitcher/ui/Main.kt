package me.omico.genshinimpact.serverswitcher.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import me.omico.genshinimpact.serverswitcher.model.ClientType
import me.omico.genshinimpact.serverswitcher.preference.AppConfig
import me.omico.genshinimpact.serverswitcher.theme.AppTheme
import javax.swing.JFileChooser

@Composable
fun Main(
    viewModel: MainViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(viewModel) {
        viewModel.viewModelScope = coroutineScope
        viewModel.updateCurrentClientType()
    }
    AppTheme {
        val message: String by viewModel.message.collectAsState("")
        val gameRootDir: String by viewModel.gameRootDir.collectAsState()
        val currentClientType: ClientType by viewModel.currentClientType.collectAsState()
        Column {
            Column {
                CurrentFolderState(
                    gameRootDir = gameRootDir,
                    currentClientType = currentClientType
                )
                Text(text = message)
            }
            Row {
                SelectGameRootDirectoryButton { viewModel.updateGameRootDir(it) }
            }
            if (gameRootDir.isBlank() || currentClientType == ClientType.Unknown) return@Column
            Column {
                ClientType.types.filterNot { it == currentClientType }.forEach {
                    Button(
                        onClick = { viewModel.backup(it) }
                    ) {
                        Text(text = "对比与${it}的差异并备份")
                    }
                }
            }
            Column {
                ClientType.types.filterNot { it == currentClientType }.forEach {
                    Button(
                        onClick = { viewModel.switch(currentClientType, it) }
                    ) {
                        Text(text = "切换到${it}")
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentFolderState(
    gameRootDir: String = "",
    currentClientType: ClientType = ClientType.Unknown
) {
    when {
        gameRootDir.isBlank() ->
            Text(text = "请设置游戏根目录。")
        currentClientType == ClientType.Unknown ->
            Text(text = "无法识别此路径：${gameRootDir}\n请重新指定新的路径。")
        else -> {
            Text(text = "当前游戏根目录：${gameRootDir}")
            Text(text = "当前游戏类型：${currentClientType}")
        }
    }
}

@Composable
fun SelectGameRootDirectoryButton(
    onDirectoryChanged: (String) -> Unit
) {
    Button(
        onClick = {
            with(JFileChooser(AppConfig.latestSelectedDir)) {
                fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                when (showOpenDialog(null)) {
                    JFileChooser.APPROVE_OPTION ->
                        with(selectedFile.absolutePath) { onDirectoryChanged(this) }
                }
                AppConfig.latestSelectedDir = currentDirectory.absolutePath
            }
        }
    ) {
        Text(text = "选中游戏根目录")
    }
}
