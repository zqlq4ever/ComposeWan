package com.zqlq.composewan.di

import com.zqlq.composewan.ui.collect.di.collectModule
import com.zqlq.composewan.ui.home.di.homeModule
import com.zqlq.composewan.ui.hot.di.hotModule
import com.zqlq.composewan.ui.login.di.loginModule
import com.zqlq.composewan.ui.search.di.searchModule
import com.zqlq.composewan.ui.system.di.systemModule

/**
 * 聚合各 Feature 的 Koin Module
 */
val appModules =
    listOf(
        homeModule,
        hotModule,
        systemModule,
        searchModule,
        loginModule,
        collectModule,
    )
