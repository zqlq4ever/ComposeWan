package com.zqlq.composewan.ui.screens.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zqlq.common.utils.toast.ToastUtils
import com.zqlq.composewan.ui.login.viewmodel.RegisterViewModel
import com.zqlq.composewan.ui.login.viewmodel.contract.RegisterEvent
import com.zqlq.composewan.ui.login.viewmodel.contract.RegisterIntent
import org.koin.androidx.compose.koinViewModel

/**
 * 注册页面
 *
 * @param onBack 返回回调
 * @param onRegisterSuccess 注册成功回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: RegisterViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // 密码可见性状态
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // 系统返回键处理
    BackHandler {
        onBack()
    }

    // 处理副作用
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RegisterEvent.RegisterSuccess -> {
                    onRegisterSuccess()
                }

                is RegisterEvent.ShowMessageRes -> {
                    ToastUtils.show(event.resId)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("注册") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(),
            )
        },
    ) {
        // 整体纯白背景
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(it),
        ) {
            // 头部下方间距
            Spacer(modifier = Modifier.height(40.dp))

            // 表单内容
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 用户名输入框
                TextField(
                    value = state.username,
                    onValueChange = { viewModel.handleIntent(RegisterIntent.UpdateUsername(it)) },
                    placeholder = { Text("请输入账号") },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedLabelColor = Color.Transparent,
                            unfocusedLabelColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    shape = RoundedCornerShape(0.dp),
                    supportingText = { },
                    leadingIcon = null,
                    trailingIcon = null,
                )

                // 密码输入框
                TextField(
                    value = state.password,
                    onValueChange = { viewModel.handleIntent(RegisterIntent.UpdatePassword(it)) },
                    placeholder = { Text("请输入密码") },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedLabelColor = Color.Transparent,
                            unfocusedLabelColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    shape = RoundedCornerShape(0.dp),
                    supportingText = { },
                    leadingIcon = null,
                    trailingIcon = {
                        // 密码显示/隐藏按钮
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.size(24.dp),
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (passwordVisible) "隐藏密码" else "显示密码",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    },
                )

                // 密码确认输入框
                TextField(
                    value = state.confirmPassword,
                    onValueChange = { viewModel.handleIntent(RegisterIntent.UpdateConfirmPassword(it)) },
                    placeholder = { Text("请确认密码") },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    colors =
                        TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedLabelColor = Color.Transparent,
                            unfocusedLabelColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    shape = RoundedCornerShape(0.dp),
                    supportingText = { },
                    leadingIcon = null,
                    trailingIcon = {
                        // 密码显示/隐藏按钮
                        IconButton(
                            onClick = { confirmPasswordVisible = !confirmPasswordVisible },
                            modifier = Modifier.size(24.dp),
                        ) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (confirmPasswordVisible) "隐藏密码" else "显示密码",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    },
                )

                // 注册按钮
                Button(
                    onClick = { viewModel.handleIntent(RegisterIntent.RegisterClick) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text("注册")
                    }
                }

                // 错误信息
                state.errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 24.dp),
                    )
                }
            }
        }
    }
}
