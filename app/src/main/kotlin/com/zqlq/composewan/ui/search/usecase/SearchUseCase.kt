package com.zqlq.composewan.ui.search.usecase

import com.zqlq.composewan.data.mapper.toPage
import com.zqlq.composewan.data.model.ArticlePage
import com.zqlq.network.WanRepository

/** 搜索用例。 */
class SearchUseCase(
    private val repository: WanRepository,
) {
    suspend fun search(
        query: String,
        page: Int = 0,
    ): ArticlePage = repository.searchArticles(query, page).toPage()
}
