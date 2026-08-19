package com.zqlq.composewan.ui.hot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.ui.common.toLoadError
import com.zqlq.composewan.ui.hot.usecase.HotUseCase
import com.zqlq.composewan.ui.hot.viewmodel.contract.HotEvent
import com.zqlq.composewan.ui.hot.viewmodel.contract.HotIntent
import com.zqlq.composewan.ui.hot.viewmodel.contract.HotUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 热门页面 ViewModel
 */
class HotViewModel(
    private val useCase: HotUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HotUiState())
    val uiState: StateFlow<HotUiState> = _uiState.asStateFlow()

    private val _events = Channel<HotEvent>(Channel.BUFFERED)
    val events: Flow<HotEvent> = _events.receiveAsFlow()

    init {
        handleIntent(HotIntent.LoadData)
    }

    fun handleIntent(intent: HotIntent) {
        when (intent) {
            HotIntent.LoadData -> loadData()
            is HotIntent.HotKeyClick -> onHotKeyClick(intent.name)
            is HotIntent.WebsiteClick -> onWebsiteClick(intent.url)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                useCase.loadHotKeys() to useCase.loadWebsites()
            }.onSuccess { (hotKeys, websites) ->
                _uiState.update {
                    it.copy(hotKeys = hotKeys, websites = websites, isLoading = false)
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.toLoadError()) }
            }
        }
    }

    private fun onHotKeyClick(name: String) {
        viewModelScope.launch { _events.send(HotEvent.NavigateToSearch(name)) }
    }

    private fun onWebsiteClick(url: String) {
        viewModelScope.launch { _events.send(HotEvent.NavigateToWebView(url)) }
    }
}
