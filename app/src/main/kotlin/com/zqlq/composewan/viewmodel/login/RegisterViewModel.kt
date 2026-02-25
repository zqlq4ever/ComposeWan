package com.zqlq.composewan.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.state.login.RegisterEffect
import com.zqlq.composewan.state.login.RegisterIntent
import com.zqlq.composewan.state.login.RegisterState
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
 * 注册页面 ViewModel
 * 处理用户注册逻辑
 */
class RegisterViewModel : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEffect>()
    val effect: SharedFlow<RegisterEffect> = _effect.asSharedFlow()

    fun processIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.UpdateUsername -> updateUsername(intent.username)
            is RegisterIntent.UpdatePassword -> updatePassword(intent.password)
            is RegisterIntent.UpdateConfirmPassword -> updateConfirmPassword(intent.confirmPassword)
            is RegisterIntent.RegisterClick -> register()
        }
    }

    private fun updateUsername(username: String) {
        _state.update { it.copy(username = username) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword) }
    }

    private fun register() {
        viewModelScope.launch {
            val username = _state.value.username
            val password = _state.value.password
            val confirmPassword = _state.value.confirmPassword

            if (username.isEmpty()) {
                _effect.emit(RegisterEffect.ShowMessage("请输入用户名"))
                return@launch
            }

            if (password.isEmpty()) {
                _effect.emit(RegisterEffect.ShowMessage("请输入密码"))
                return@launch
            }

            if (password.length < 6) {
                _effect.emit(RegisterEffect.ShowMessage("密码长度不能少于6位"))
                return@launch
            }

            if (confirmPassword != password) {
                _effect.emit(RegisterEffect.ShowMessage("两次输入的密码不一致"))
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            delay(1000) // 模拟网络请求

            runCatching {
                // 这里可以添加真实的注册逻辑
                // 模拟注册成功
                true
            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(RegisterEffect.RegisterSuccess)
                _effect.emit(RegisterEffect.ShowMessage("注册成功"))
            }.onFailure {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(RegisterEffect.ShowMessage("注册失败: ${it.message}"))
            }
        }
    }
}
