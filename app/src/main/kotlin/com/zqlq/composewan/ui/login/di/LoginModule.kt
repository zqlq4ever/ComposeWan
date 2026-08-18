package com.zqlq.composewan.ui.login.di

import com.zqlq.composewan.ui.login.usecase.LoginUseCase
import com.zqlq.composewan.ui.login.viewmodel.LoginViewModel
import com.zqlq.composewan.ui.login.viewmodel.RegisterViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * 登录/注册 Koin Module
 */
val loginModule: Module =
    module {
        single { LoginUseCase() }
        viewModel { LoginViewModel(useCase = get()) }
        viewModel { RegisterViewModel(useCase = get()) }
    }
