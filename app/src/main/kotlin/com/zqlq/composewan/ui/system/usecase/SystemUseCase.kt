package com.zqlq.composewan.ui.system.usecase

import com.zqlq.composewan.data.mapper.toPage
import com.zqlq.composewan.data.mapper.toUi
import com.zqlq.composewan.data.model.ArticlePage
import com.zqlq.composewan.data.model.SystemCategory
import com.zqlq.network.WanRepository

/** 体系页用例。 */
class SystemUseCase(
    private val repository: WanRepository,
) {
    suspend fun loadCategories(): List<SystemCategory> = repository.getSystemTree().map { it.toUi() }

    suspend fun loadArticles(
        cid: Int,
        page: Int,
    ): ArticlePage = repository.getArticlesByCid(cid, page).toPage()

    suspend fun collect(id: Int) {
        repository.collect(id)
    }

    suspend fun uncollect(id: Int) {
        repository.uncollectOrigin(id)
    }
}
