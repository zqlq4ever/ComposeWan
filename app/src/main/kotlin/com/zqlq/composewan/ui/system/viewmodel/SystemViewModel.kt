package com.zqlq.composewan.ui.system.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.data.model.SystemChild
import com.zqlq.composewan.ui.system.usecase.SystemUseCase
import com.zqlq.composewan.ui.system.viewmodel.contract.SystemEvent
import com.zqlq.composewan.ui.system.viewmodel.contract.SystemIntent
import com.zqlq.composewan.ui.system.viewmodel.contract.SystemUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 体系页面 ViewModel
 */
class SystemViewModel(
    private val useCase: SystemUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SystemUiState())
    val uiState: StateFlow<SystemUiState> = _uiState.asStateFlow()

    private val _events = Channel<SystemEvent>(Channel.BUFFERED)
    val events: Flow<SystemEvent> = _events.receiveAsFlow()

    init {
        handleIntent(SystemIntent.LoadData)
    }

    fun handleIntent(intent: SystemIntent) {
        when (intent) {
            SystemIntent.LoadData -> loadData()
            is SystemIntent.ToggleExpand -> toggleExpand(intent.categoryId)
            is SystemIntent.ChildClick -> onChildClick(intent.categoryName, intent.children)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCase.loadCategories() }
                .onSuccess { categories ->
                    _uiState.update { it.copy(categories = categories, isLoading = false) }
                }.onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    private fun toggleExpand(categoryId: Int) {
        _uiState.update { state ->
            val expandedIds = state.expandedIds.toMutableSet()
            if (!expandedIds.add(categoryId)) {
                expandedIds.remove(categoryId)
            }
            state.copy(expandedIds = expandedIds)
        }
    }

    private fun onChildClick(
        categoryName: String,
        children: List<SystemChild>,
    ) {
        viewModelScope.launch {
            _events.send(SystemEvent.NavigateToSystemDetail(categoryName, children))
        }
    }
}
