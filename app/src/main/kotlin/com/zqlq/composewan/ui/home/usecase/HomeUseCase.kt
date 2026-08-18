package com.zqlq.composewan.ui.home.usecase

import com.zqlq.composewan.data.mapper.toPage
import com.zqlq.composewan.data.mapper.toUi
import com.zqlq.composewan.data.model.ArticlePage
import com.zqlq.composewan.data.model.BannerItem
import com.zqlq.network.WanRepository

/** 首页用例。 */
class HomeUseCase(
    private val repository: WanRepository,
) {
    suspend fun loadBanners(): List<BannerItem> = repository.getBanners().map { it.toUi() }

    suspend fun loadArticles(page: Int): ArticlePage = repository.getHomeArticles(page).toPage()

    suspend fun collect(id: Int) {
        repository.collect(id)
    }

    suspend fun uncollect(id: Int) {
        repository.uncollectOrigin(id)
    }
}
