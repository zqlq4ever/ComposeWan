package com.zqlq.composewan.ui.collect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.R
import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.ui.collect.usecase.CollectUseCase
import com.zqlq.composewan.ui.collect.viewmodel.contract.CollectEvent
import com.zqlq.composewan.ui.collect.viewmodel.contract.CollectIntent
import com.zqlq.composewan.ui.collect.viewmodel.contract.CollectUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 收藏页面 ViewModel
 */
class CollectViewModel(
    private val useCase: CollectUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CollectUiState())
    val uiState: StateFlow<CollectUiState> = _uiState.asStateFlow()

    private val _events = Channel<CollectEvent>(Channel.BUFFERED)
    val events: Flow<CollectEvent> = _events.receiveAsFlow()

    init {
        handleIntent(CollectIntent.LoadCollectList)
    }

    fun handleIntent(intent: CollectIntent) {
        when (intent) {
            CollectIntent.LoadCollectList -> loadCollectList()
            CollectIntent.RefreshCollectList -> refreshCollectList()
            CollectIntent.LoadMoreCollectList -> loadMoreCollectList()
            is CollectIntent.OnArticleClick -> onArticleClick(intent.article)
            is CollectIntent.OnUncollectClick -> onUncollectClick(intent.article)
        }
    }

    private fun loadCollectList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                delay(1000)
                useCase.loadCollectArticles()
            }.onSuccess { articles ->
                _uiState.update {
                    it.copy(articles = articles, isLoading = false, hasMore = articles.size < 20)
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
                _events.send(CollectEvent.ShowMessageRes(R.string.load_failed))
            }
        }
    }

    private fun refreshCollectList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            delay(1000)
            runCatching { useCase.loadCollectArticles() }
                .onSuccess { articles ->
                    _uiState.update {
                        it.copy(articles = articles, isRefreshing = false, hasMore = articles.size < 20)
                    }
                }.onFailure {
                    _uiState.update { it.copy(isRefreshing = false) }
                    _events.send(CollectEvent.ShowMessageRes(R.string.refresh_failed))
                }
        }
    }

    private fun loadMoreCollectList() {
        viewModelScope.launch {
            if (_uiState.value.isLoadingMore || !_uiState.value.hasMore) return@launch
            _uiState.update { it.copy(isLoadingMore = true) }
            delay(1000)
            runCatching { useCase.loadCollectArticles() }
                .onSuccess { newArticles ->
                    val updated = _uiState.value.articles + newArticles
                    _uiState.update {
                        it.copy(articles = updated, isLoadingMore = false, hasMore = updated.size < 40)
                    }
                }.onFailure {
                    _uiState.update { it.copy(isLoadingMore = false) }
                    _events.send(CollectEvent.ShowMessageRes(R.string.load_more_failed))
                }
        }
    }

    private fun onArticleClick(article: ArticleItem) {
        viewModelScope.launch { _events.send(CollectEvent.NavigateToWebView(article.url)) }
    }

    private fun onUncollectClick(article: ArticleItem) {
        viewModelScope.launch {
            _uiState.update { it.copy(articles = it.articles.filter { item -> item.id != article.id }) }
            _events.send(CollectEvent.ShowMessageRes(R.string.uncollect_success))
        }
    }
}
