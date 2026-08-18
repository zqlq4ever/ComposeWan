package com.zqlq.composewan.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.R
import com.zqlq.composewan.ui.login.usecase.LoginUseCase
import com.zqlq.composewan.ui.login.viewmodel.contract.LoginEvent
import com.zqlq.composewan.ui.login.viewmodel.contract.LoginIntent
import com.zqlq.composewan.ui.login.viewmodel.contract.LoginUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 登录页面 ViewModel
 */
class LoginViewModel(
    private val useCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events: Flow<LoginEvent> = _events.receiveAsFlow()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UpdateUsername -> _uiState.update { it.copy(username = intent.username) }
            is LoginIntent.UpdatePassword -> _uiState.update { it.copy(password = intent.password) }
            LoginIntent.LoginClick -> login()
            LoginIntent.RegisterClick -> navigateToRegister()
        }
    }

    private fun login() {
        viewModelScope.launch {
            val username = _uiState.value.username
            val password = _uiState.value.password
            if (username.isEmpty()) {
                _events.send(LoginEvent.ShowMessageRes(R.string.please_input_username))
                return@launch
            }
            if (password.isEmpty()) {
                _events.send(LoginEvent.ShowMessageRes(R.string.please_input_password))
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            useCase
                .login(username, password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(LoginEvent.LoginSuccess)
                    _events.send(LoginEvent.ShowMessageRes(R.string.login_success))
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(LoginEvent.ShowMessageRes(R.string.login_failed))
                }
        }
    }

    private fun navigateToRegister() {
        viewModelScope.launch { _events.send(LoginEvent.NavigateToRegister) }
    }
}
