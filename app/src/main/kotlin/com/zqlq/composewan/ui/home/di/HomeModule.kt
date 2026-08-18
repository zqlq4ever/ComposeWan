package com.zqlq.composewan.ui.home.di

import com.zqlq.composewan.ui.home.usecase.HomeUseCase
import com.zqlq.composewan.ui.home.viewmodel.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * 首页 Koin Module
 */
val homeModule: Module =
    module {
        single { HomeUseCase() }
        viewModel { HomeViewModel(useCase = get()) }
    }
