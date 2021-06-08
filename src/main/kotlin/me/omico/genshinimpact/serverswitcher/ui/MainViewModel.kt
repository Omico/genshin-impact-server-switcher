package me.omico.genshinimpact.serverswitcher.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.omico.genshinimpact.serverswitcher.model.ClientType
import me.omico.genshinimpact.serverswitcher.preference.AppConfig
import me.omico.genshinimpact.serverswitcher.utility.PackageParser

class MainViewModel {

    lateinit var viewModelScope: CoroutineScope

    private val _message: MutableSharedFlow<String> = MutableStateFlow("")
    val message: SharedFlow<String> = _message

    private val _gameRootDir: MutableStateFlow<String> = MutableStateFlow(AppConfig.gameRootDir)
    val gameRootDir: StateFlow<String> = _gameRootDir

    private val _currentClientType: MutableStateFlow<ClientType> = MutableStateFlow(ClientType.Unknown)
    val currentClientType: StateFlow<ClientType> = _currentClientType

    fun backup(targetClientType: ClientType) {
        viewModelScope.launch {
            updateMessage()
            PackageParser.backup(targetClientType).cancellable().collect {
                updateMessage(it.message)
                if (it.isFailure) cancel()
            }
        }
    }

    fun switch(currentClientType: ClientType, targetClientType: ClientType) {
        viewModelScope.launch {
            updateMessage()
            PackageParser.switch(currentClientType, targetClientType).cancellable().collect {
                updateMessage(it.message)
                if (it.isFailure) cancel()
            }
            updateCurrentClientType()
        }
    }

    fun updateGameRootDir(gameRootDir: String) {
        viewModelScope.launch {
            _gameRootDir.value = gameRootDir
            AppConfig.gameRootDir = gameRootDir
            _currentClientType.value = PackageParser.currentClientType
        }
    }

    fun updateCurrentClientType() {
        viewModelScope.launch {
            _currentClientType.value = PackageParser.currentClientType
        }
    }

    private fun updateMessage(message: String = "") {
        _message.tryEmit(message)
        println(message)
    }
}
