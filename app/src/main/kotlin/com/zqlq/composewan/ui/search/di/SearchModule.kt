package com.zqlq.composewan.ui.search.di

import com.zqlq.composewan.ui.search.usecase.SearchUseCase
import com.zqlq.composewan.ui.search.viewmodel.SearchViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * 搜索页 Koin Module
 */
val searchModule: Module =
    module {
        single { SearchUseCase(repository = get()) }
        viewModel { SearchViewModel(useCase = get()) }
    }
