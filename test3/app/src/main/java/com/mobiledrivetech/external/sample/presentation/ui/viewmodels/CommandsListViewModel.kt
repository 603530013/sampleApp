package com.mobiledrivetech.external.sample.presentation.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiledrivetech.external.common.base.extensions.kInject
import com.mobiledrivetech.external.sample.domain.models.Commands
import com.mobiledrivetech.external.sample.domain.usecase.ExecuteCommandUC
import com.mobiledrivetech.external.sample.domain.usecase.GetAllCommandsUC
import com.mobiledrivetech.external.sample.domain.usecase.InitializeMiddlewareUC
import com.mobiledrivetech.external.sample.presentation.ui.mapper.toDisplayedCommand
import kotlinx.coroutines.launch

class CommandsListViewModel : ViewModel() {
    private val initializeMiddlewareUC: InitializeMiddlewareUC by kInject()
    private val getAllCommandsUC: GetAllCommandsUC by kInject()
    private val executeCommandUC: ExecuteCommandUC by kInject()

    // TODO: Add more commands here to show in the list
    val allCommands = getAllCommandsUC().map { it.toDisplayedCommand() }
    val commandResult: MutableLiveData<String> = MutableLiveData()

    init {
        // we need to initialize middleware before we can use it
        viewModelScope.launch {
            initializeMiddlewareUC()
        }
    }

    fun onItemClick(command: Commands) =
        viewModelScope.launch {
            executeCommandUC(command = command).onSuccess {
                commandResult.postValue(it.toString())
            }.onFailure {
                commandResult.postValue(it.message)
            }
        }
}
