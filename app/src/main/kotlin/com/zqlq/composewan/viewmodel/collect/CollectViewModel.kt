package com.zqlq.composewan.viewmodel.collect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.state.collect.CollectEffect
import com.zqlq.composewan.state.collect.CollectIntent
import com.zqlq.composewan.state.collect.CollectState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 收藏页面 ViewModel
 * 处理用户意图，更新 UI 状态
 */
class CollectViewModel : ViewModel() {

    private val _state = MutableStateFlow(CollectState())
    val state: StateFlow<CollectState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CollectEffect>()
    val effect: SharedFlow<CollectEffect> = _effect.asSharedFlow()

    init {
        processIntent(CollectIntent.LoadCollectList)
    }

    fun processIntent(intent: CollectIntent) {
        when (intent) {
            is CollectIntent.LoadCollectList -> loadCollectList()
            is CollectIntent.RefreshCollectList -> refreshCollectList()
            is CollectIntent.LoadMoreCollectList -> loadMoreCollectList()
            is CollectIntent.OnArticleClick -> onArticleClick(intent.article)
            is CollectIntent.OnUncollectClick -> onUncollectClick(intent.article)
        }
    }

    private fun loadCollectList() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            runCatching {
                delay(1000)
                mockCollectArticles()
            }.onSuccess { articles ->
                _state.update { 
                    it.copy(
                        articles = articles,
                        isLoading = false,
                        hasMore = articles.size < 20
                    )
                }
            }.onFailure { e ->
                _state.update { 
                    it.copy(
                        isLoading = false
                    )
                }
                _effect.emit(CollectEffect.ShowMessage("加载失败: ${e.message}"))
            }
        }
    }

    private fun refreshCollectList() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            delay(1000)

            runCatching {
                mockCollectArticles()
            }.onSuccess { articles ->
                _state.update {
                    it.copy(
                        articles = articles,
                        isRefreshing = false,
                        hasMore = articles.size < 20
                    )
                }
            }.onFailure { e ->
                _state.update { it.copy(isRefreshing = false) }
                _effect.emit(CollectEffect.ShowMessage("刷新失败: ${e.message}"))
            }
        }
    }

    private fun loadMoreCollectList() {
        viewModelScope.launch {
            if (_state.value.isLoadingMore || !_state.value.hasMore) return@launch
            
            _state.update { it.copy(isLoadingMore = true) }

            delay(1000)

            runCatching {
                mockCollectArticles()
            }.onSuccess { newArticles ->
                val updatedArticles = _state.value.articles + newArticles
                _state.update {
                    it.copy(
                        articles = updatedArticles,
                        isLoadingMore = false,
                        hasMore = updatedArticles.size < 40
                    )
                }
            }.onFailure { e ->
                _state.update { it.copy(isLoadingMore = false) }
                _effect.emit(CollectEffect.ShowMessage("加载更多失败: ${e.message}"))
            }
        }
    }

    private fun onArticleClick(article: ArticleItem) {
        viewModelScope.launch {
            _effect.emit(CollectEffect.NavigateToWebView(article.url))
        }
    }

    private fun onUncollectClick(article: ArticleItem) {
        viewModelScope.launch {
            // 从列表中移除该文章
            _state.update {
                it.copy(
                    articles = it.articles.filter { it.id != article.id }
                )
            }
            _effect.emit(CollectEffect.ShowMessage("取消收藏成功"))
        }
    }

    /**
     * 模拟收藏文章数据
     */
    private fun mockCollectArticles(): List<ArticleItem> {
        return listOf(
            ArticleItem(
                id = 1,
                title = "Android Jetpack Compose 学习指南",
                author = "Compose开发者",
                time = "2024-01-15",
                url = "https://example.com/compose-guide",
                chapterName = "Compose",
                isCollect = true
            ),
            ArticleItem(
                id = 2,
                title = "Kotlin 协程深度解析",
                author = "Kotlin专家",
                time = "2024-01-12",
                url = "https://example.com/coroutines",
                chapterName = "Kotlin",
                isCollect = true
            ),
            ArticleItem(
                id = 3,
                title = "MVI 架构设计模式详解",
                author = "架构师",
                time = "2024-01-10",
                url = "https://example.com/mvi",
                chapterName = "架构",
                isCollect = true
            ),
            ArticleItem(
                id = 4,
                title = "Android 性能优化实战",
                author = "性能专家",
                time = "2024-01-08",
                url = "https://example.com/performance",
                chapterName = "优化",
                isCollect = true
            ),
            ArticleItem(
                id = 5,
                title = "Jetpack Room 数据库最佳实践",
                author = "数据库专家",
                time = "2024-01-05",
                url = "https://example.com/room",
                chapterName = "Jetpack",
                isCollect = true
            )
        )
    }
}
