package com.zqlq.composewan.di

import com.zqlq.network.WanRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/** 网络仓储。 */
val networkModule: Module =
    module {
        single { WanRepository() }
    }
