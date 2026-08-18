package com.zqlq.composewan.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.R
import com.zqlq.composewan.ui.login.usecase.LoginUseCase
import com.zqlq.composewan.ui.login.viewmodel.contract.RegisterEvent
import com.zqlq.composewan.ui.login.viewmodel.contract.RegisterIntent
import com.zqlq.composewan.ui.login.viewmodel.contract.RegisterUiState
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
 * 注册页面 ViewModel
 */
class RegisterViewModel(
    private val useCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _events = Channel<RegisterEvent>(Channel.BUFFERED)
    val events: Flow<RegisterEvent> = _events.receiveAsFlow()

    fun handleIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.UpdateUsername -> {
                _uiState.update { it.copy(username = intent.username) }
            }

            is RegisterIntent.UpdatePassword -> {
                _uiState.update { it.copy(password = intent.password) }
            }

            is RegisterIntent.UpdateConfirmPassword -> {
                _uiState.update { it.copy(confirmPassword = intent.confirmPassword) }
            }

            RegisterIntent.RegisterClick -> {
                register()
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            val username = _uiState.value.username
            val password = _uiState.value.password
            val confirmPassword = _uiState.value.confirmPassword
            if (username.isEmpty()) {
                _events.send(RegisterEvent.ShowMessageRes(R.string.please_input_username))
                return@launch
            }
            if (password.isEmpty()) {
                _events.send(RegisterEvent.ShowMessageRes(R.string.please_input_password))
                return@launch
            }
            if (password.length < 6) {
                _events.send(RegisterEvent.ShowMessageRes(R.string.password_too_short))
                return@launch
            }
            if (confirmPassword != password) {
                _events.send(RegisterEvent.ShowMessageRes(R.string.password_not_match))
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            useCase
                .register(username, password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(RegisterEvent.RegisterSuccess)
                    _events.send(RegisterEvent.ShowMessageRes(R.string.register_success))
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(RegisterEvent.ShowMessageRes(R.string.register_failed))
                }
        }
    }
}
