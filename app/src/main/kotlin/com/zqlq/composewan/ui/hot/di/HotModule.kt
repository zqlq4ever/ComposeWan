package com.zqlq.composewan.ui.hot.di

import com.zqlq.composewan.ui.hot.usecase.HotUseCase
import com.zqlq.composewan.ui.hot.viewmodel.HotViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * 热门页 Koin Module
 */
val hotModule: Module =
    module {
        single { HotUseCase() }
        viewModel { HotViewModel(useCase = get()) }
    }
