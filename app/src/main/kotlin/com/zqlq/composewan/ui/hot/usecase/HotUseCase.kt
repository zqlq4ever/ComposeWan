package com.zqlq.composewan.ui.hot.usecase

import com.zqlq.composewan.data.mapper.toUi
import com.zqlq.composewan.data.model.HotKeyItem
import com.zqlq.composewan.data.model.WebsiteItem
import com.zqlq.network.WanRepository

/** 热门页用例。 */
class HotUseCase(
    private val repository: WanRepository,
) {
    suspend fun loadHotKeys(): List<HotKeyItem> = repository.getHotKeys().map { it.toUi() }

    suspend fun loadWebsites(): List<WebsiteItem> = repository.getWebsites().map { it.toUi() }
}
