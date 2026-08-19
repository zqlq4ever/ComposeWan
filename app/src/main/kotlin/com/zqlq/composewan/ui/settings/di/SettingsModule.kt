package com.zqlq.composewan.ui.settings.di

import com.zqlq.composewan.ui.settings.usecase.SettingsUseCase
import com.zqlq.composewan.ui.settings.viewmodel.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/** 设置页 Koin Module。 */
val settingsModule: Module =
    module {
        single { SettingsUseCase(repository = get()) }
        viewModel { SettingsViewModel(useCase = get()) }
    }
