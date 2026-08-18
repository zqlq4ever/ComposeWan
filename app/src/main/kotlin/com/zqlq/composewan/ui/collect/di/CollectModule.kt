package com.zqlq.composewan.ui.collect.di

import com.zqlq.composewan.ui.collect.usecase.CollectUseCase
import com.zqlq.composewan.ui.collect.viewmodel.CollectViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * 收藏页 Koin Module
 */
val collectModule: Module =
    module {
        single { CollectUseCase(repository = get()) }
        viewModel { CollectViewModel(useCase = get()) }
    }
