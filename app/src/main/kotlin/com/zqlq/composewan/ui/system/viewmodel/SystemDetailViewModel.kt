package com.zqlq.composewan.ui.system.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.data.model.SystemChild
import com.zqlq.composewan.ui.system.usecase.SystemUseCase
import com.zqlq.composewan.ui.system.viewmodel.contract.SystemDetailEvent
import com.zqlq.composewan.ui.system.viewmodel.contract.SystemDetailIntent
import com.zqlq.composewan.ui.system.viewmodel.contract.SystemDetailUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 体系详情页面 ViewModel
 *
 * @param categoryName 分类名称
 * @param children 子分类列表
 */
class SystemDetailViewModel(
    categoryName: String,
    children: List<SystemChild>,
    private val useCase: SystemUseCase,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            SystemDetailUiState(
                categoryName = categoryName,
                children = children,
                selectedChildId = children.firstOrNull()?.id ?: 0,
            ),
        )
    val uiState: StateFlow<SystemDetailUiState> = _uiState.asStateFlow()

    private val _events = Channel<SystemDetailEvent>(Channel.BUFFERED)
    val events: Flow<SystemDetailEvent> = _events.receiveAsFlow()

    init {
        loadArticles()
    }

    fun handleIntent(intent: SystemDetailIntent) {
        when (intent) {
            is SystemDetailIntent.SelectChild -> selectChild(intent.childId)
            is SystemDetailIntent.ArticleClick -> onArticleClick(intent.url)
            is SystemDetailIntent.CollectClick -> onCollectClick(intent.articleId, intent.isCollect)
            SystemDetailIntent.LoadMore -> loadMoreArticles()
        }
    }

    private fun selectChild(childId: Int) {
        _uiState.update { it.copy(selectedChildId = childId, articles = emptyList()) }
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val childName = selectedChildName()
                useCase.loadArticles(childName)
            }.onSuccess { articles ->
                _uiState.update { it.copy(articles = articles, isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun loadMoreArticles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                val childName = selectedChildName()
                useCase.loadArticles(childName, idOffset = _uiState.value.articles.size)
            }.onSuccess { moreArticles ->
                _uiState.update {
                    it.copy(articles = it.articles + moreArticles, isLoading = false)
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onArticleClick(url: String) {
        viewModelScope.launch { _events.send(SystemDetailEvent.NavigateToWebView(url)) }
    }

    private fun onCollectClick(
        articleId: Int,
        isCollect: Boolean,
    ) {
        _uiState.update { state ->
            state.copy(
                articles =
                    state.articles.map { article ->
                        if (article.id == articleId) article.copy(isCollect = !isCollect) else article
                    },
            )
        }
    }

    private fun selectedChildName(): String =
        _uiState.value.children
            .find { it.id == _uiState.value.selectedChildId }
            ?.name
            .orEmpty()
}
