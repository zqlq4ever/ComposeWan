package com.zqlq.composewan.ui.system.di

import com.zqlq.composewan.data.model.SystemChild
import com.zqlq.composewan.ui.system.usecase.SystemUseCase
import com.zqlq.composewan.ui.system.viewmodel.SystemDetailViewModel
import com.zqlq.composewan.ui.system.viewmodel.SystemViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * 体系页 Koin Module
 */
val systemModule: Module =
    module {
        single { SystemUseCase() }
        viewModel { SystemViewModel(useCase = get()) }
        viewModel { params ->
            SystemDetailViewModel(
                categoryName = params.get(),
                children = params.get<List<SystemChild>>(),
                useCase = get(),
            )
        }
    }
