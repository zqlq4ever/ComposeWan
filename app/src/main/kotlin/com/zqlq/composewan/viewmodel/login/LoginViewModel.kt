package com.zqlq.composewan.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.state.login.LoginEffect
import com.zqlq.composewan.state.login.LoginIntent
import com.zqlq.composewan.state.login.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 登录页面 ViewModel
 * 处理用户登录逻辑
 */
class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UpdateUsername -> updateUsername(intent.username)
            is LoginIntent.UpdatePassword -> updatePassword(intent.password)
            is LoginIntent.LoginClick -> login()
            is LoginIntent.RegisterClick -> navigateToRegister()
        }
    }

    private fun updateUsername(username: String) {
        _state.update { it.copy(username = username) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    private fun login() {
        viewModelScope.launch {
            val username = _state.value.username
            val password = _state.value.password

            if (username.isEmpty()) {
                _effect.emit(LoginEffect.ShowMessage("请输入用户名"))
                return@launch
            }

            if (password.isEmpty()) {
                _effect.emit(LoginEffect.ShowMessage("请输入密码"))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            delay(1000) // 模拟网络请求

            runCatching {
                // 这里可以添加真实的登录逻辑
                // 模拟登录成功
                true
            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(LoginEffect.LoginSuccess)
                _effect.emit(LoginEffect.ShowMessage("登录成功"))
            }.onFailure {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(LoginEffect.ShowMessage("登录失败: ${it.message}"))
            }
        }
    }

    private fun navigateToRegister() {
        viewModelScope.launch {
            _effect.emit(LoginEffect.NavigateToRegister)
        }
    }
}
