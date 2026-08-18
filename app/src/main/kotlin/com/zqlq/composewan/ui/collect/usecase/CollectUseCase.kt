package com.zqlq.composewan.ui.collect.usecase

import com.zqlq.composewan.data.mapper.toPage
import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.ArticlePage
import com.zqlq.network.WanRepository

/** 收藏页用例。 */
class CollectUseCase(
    private val repository: WanRepository,
) {
    suspend fun loadCollectArticles(page: Int): ArticlePage =
        repository.getCollectArticles(page).toPage(forceCollected = true)

    suspend fun uncollect(article: ArticleItem) {
        repository.uncollect(id = article.id, originId = article.originId)
    }
}
