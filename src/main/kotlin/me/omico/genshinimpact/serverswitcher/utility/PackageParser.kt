package me.omico.genshinimpact.serverswitcher.utility

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.omico.genshinimpact.serverswitcher.model.ClientType
import me.omico.genshinimpact.serverswitcher.model.PackageVersion
import me.omico.genshinimpact.serverswitcher.model.Progress
import me.omico.genshinimpact.serverswitcher.preference.AppConfig
import java.io.File

object PackageParser {

    private const val GAME_DIR = "Genshin Impact Game"

    private const val CONFIG_INI = "config.ini"

    private const val PACKAGE_VERSION = "pkg_version"
    private const val PACKAGE_VERSION_CN = "pkg_version_cn"
    private const val PACKAGE_VERSION_GLOBAL = "pkg_version_global"

    private const val GAME_DATA_DIR_NAME_CN = "YuanShen_Data"
    private const val GAME_DATA_DIR_NAME_GLOBAL = "GenshinImpact_Data"

    private const val CHANNEL_NAME_CN = "CNRELWin"
    private const val CHANNEL_NAME_GLOBAL = "OSRELWin"

    private val userDir = File(System.getProperty("user.dir"))
    private val backupsDir = File(userDir, "backups")

    suspend fun backup(
        targetClientType: ClientType,
    ): Flow<Progress> = flow {
        val diffList = diffList(currentClientType, targetClientType)
        val backupDir = File(backupsDir, currentClientType.javaClass.simpleName)
        if (backupDir.exists()) backupDir.deleteRecursively()
        val backupGameDir = File(backupDir, GAME_DIR)
        diffList.forEach { backup(gameDir, backupGameDir, it) }
        writeDiffList(backupDir, diffList)
        emit(Progress.Success("备份完成。"))
    }

    suspend fun switch(
        currentClientType: ClientType,
        targetClientType: ClientType,
    ): Flow<Progress> = flow {
        val targetBackupDir = File(backupsDir, targetClientType.javaClass.simpleName)
        println(targetBackupDir)
        if (!targetBackupDir.exists()) emit(Progress.Failure("备份文件不存在"))
        val targetBackupGameDir = File(targetBackupDir, GAME_DIR)
        if (!targetBackupGameDir.exists()) emit(Progress.Failure("备份文件夹缺失"))
        targetBackupDir.backupDiffList.forEach {
            if (!File(targetBackupGameDir, it).exists()) emit(Progress.Failure("备份文件缺失 $it"))
        }
        val diffList = diffList(currentClientType, targetClientType)
        diffList.forEach { delete(gameDir, it) }
        currentClientType.gameDataDir.renameTo(targetClientType.gameDataDir)
        targetBackupDir.backupDiffList.forEach { restore(targetBackupGameDir, gameDir, it) }
        changeAudioPackageVersionFiles(currentClientType, targetClientType)
        changeChannelName(targetClientType)
        emit(Progress.Success("切换完成。"))
    }

    private suspend fun FlowCollector<Progress>.backup(gameDir: File, backupDir: File, fileName: String) =
        process("Backup", fileName) { File(gameDir, fileName).copyTo(File(backupDir, fileName)) }

    private suspend fun FlowCollector<Progress>.delete(gameDir: File, fileName: String) =
        process("Delete", fileName) { File(gameDir, fileName).delete() }

    private suspend fun FlowCollector<Progress>.restore(targetBackupGameDir: File, gameDir: File, fileName: String) =
        process("Restore", fileName) { File(targetBackupGameDir, fileName).copyTo(File(gameDir, fileName)) }

    private suspend fun FlowCollector<Progress>.process(
        tag: String,
        fileName: String,
        block: () -> Unit,
    ) = runCatching { withContext(Dispatchers.IO) { block() } }
        .onSuccess { emit(Progress.Processing("$tag success: $fileName")) }
        .onFailure { emit(Progress.Failure("$tag failure: ${fileName}, ${it.message}")) }

    private fun writeDiffList(backupDir: File, diffList: List<String>) {
        val content = StringBuilder().apply { diffList.forEach { appendLine(it) } }.toString()
        backupDir.backupDiffListFile.writeText(content)
    }

    val currentClientType: ClientType
        get() = when {
            File(gameDir, "YuanShen.exe").exists() -> ClientType.CN
            File(gameDir, "GenshinImpact.exe").exists() -> ClientType.Global
            else -> ClientType.Unknown
        }

    private val gameDir get() = File(AppConfig.gameRootDir, "Genshin Impact Game")

    private val ClientType.gameDataDirName
        get() = when (this) {
            ClientType.CN -> GAME_DATA_DIR_NAME_CN
            ClientType.Global -> GAME_DATA_DIR_NAME_GLOBAL
            ClientType.Unknown -> throw Exception("Unknown gameDataDirName.")
        }

    private val ClientType.gameDataDir get() = File(gameDir, gameDataDirName)

    private fun diffList(
        currentClientType: ClientType,
        targetClientType: ClientType
    ): List<String> = ArrayList<String>().apply {
        val targetPackageVersions = targetPackageVersions(targetClientType)
        addAll(
            targetPackageVersions(currentClientType)
                .filterNot { targetPackageVersions.contains(it) }
                .map { it.remoteName })
        add(CONFIG_INI)
        add(PACKAGE_VERSION)
    }

    private val File.backupDiffListFile: File get() = File(this, "DiffList.txt")

    private val File.backupDiffList: List<String> get() = backupDiffListFile.readLines()

    private fun changeAudioPackageVersionFiles(
        currentClientType: ClientType,
        targetClientType: ClientType
    ) {
        gameDir.listFiles { _, name -> name.startsWith("Audio_") && name.endsWith("_pkg_version") }?.forEach {
            val newText = it.readText().replace(currentClientType.gameDataDirName, targetClientType.gameDataDirName)
            it.writeText(newText)
        }
    }

    private fun changeChannelName(targetClientType: ClientType) {
        val channelName = File(targetClientType.gameDataDir, "Persistent/ChannelName")
        when (targetClientType) {
            ClientType.CN -> channelName.writeText(CHANNEL_NAME_CN)
            ClientType.Global -> channelName.writeText(CHANNEL_NAME_GLOBAL)
            ClientType.Unknown -> return
        }
    }

    private fun targetPackageVersions(targetClientType: ClientType): List<PackageVersion> = when (targetClientType) {
        ClientType.CN -> packageVersions(PACKAGE_VERSION_CN)
        ClientType.Global -> packageVersions(PACKAGE_VERSION_GLOBAL)
        ClientType.Unknown -> emptyList()
    }

    private fun packageVersions(name: String): List<PackageVersion> =
        File(backupsDir, name).readLines().map { Json.decodeFromString(it) }
}
