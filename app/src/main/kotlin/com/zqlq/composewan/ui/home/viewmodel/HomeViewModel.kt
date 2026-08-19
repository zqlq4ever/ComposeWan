package com.zqlq.composewan.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.R
import com.zqlq.composewan.ui.home.usecase.HomeUseCase
import com.zqlq.composewan.ui.home.viewmodel.contract.HomeEvent
import com.zqlq.composewan.ui.home.viewmodel.contract.HomeIntent
import com.zqlq.composewan.ui.home.viewmodel.contract.HomeUiState
import com.zqlq.composewan.ui.common.toLoadError
import com.zqlq.network.ApiException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 首页 ViewModel
 * 处理用户意图，更新 UI 状态
 */
class HomeViewModel(
    private val useCase: HomeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    private var nextPage = 0

    init {
        handleIntent(HomeIntent.LoadData)
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadData -> loadData()
            HomeIntent.Refresh -> refresh()
            HomeIntent.LoadMore -> loadMore()
            is HomeIntent.BannerClick -> onBannerClick(intent.url)
            is HomeIntent.ArticleClick -> onArticleClick(intent.url)
            HomeIntent.SearchClick -> onSearchClick()
            is HomeIntent.CollectClick -> onCollectClick(intent.articleId, intent.isCollect)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                useCase.loadBanners() to useCase.loadArticles(page = 0)
            }.onSuccess { (banners, page) ->
                nextPage = 1
                _uiState.update {
                    it.copy(
                        banners = banners,
                        articles = page.articles,
                        isLoading = false,
                        hasMore = !page.over,
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.toLoadError()) }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            runCatching {
                useCase.loadBanners() to useCase.loadArticles(page = 0)
            }.onSuccess { (banners, page) ->
                nextPage = 1
                _uiState.update {
                    it.copy(
                        banners = banners,
                        articles = page.articles,
                        isRefreshing = false,
                        hasMore = !page.over,
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isRefreshing = false, error = e.toLoadError()) }
            }
        }
    }

    private fun loadMore() {
        viewModelScope.launch {
            val current = _uiState.value
            if (current.isLoading || !current.hasMore) return@launch
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                useCase.loadArticles(page = nextPage)
            }.onSuccess { page ->
                nextPage += 1
                _uiState.update {
                    it.copy(
                        articles = it.articles + page.articles,
                        isLoading = false,
                        hasMore = !page.over,
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onBannerClick(url: String) {
        viewModelScope.launch { _events.send(HomeEvent.NavigateToWebView(url)) }
    }

    private fun onArticleClick(url: String) {
        viewModelScope.launch { _events.send(HomeEvent.NavigateToWebView(url)) }
    }

    private fun onSearchClick() {
        viewModelScope.launch { _events.send(HomeEvent.NavigateToSearch) }
    }

    private fun onCollectClick(
        articleId: Int,
        isCollect: Boolean,
    ) {
        viewModelScope.launch {
            runCatching {
                if (isCollect) {
                    useCase.uncollect(articleId)
                } else {
                    useCase.collect(articleId)
                }
            }.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        articles =
                            state.articles.map { article ->
                                if (article.id == articleId) article.copy(isCollect = !isCollect) else article
                            },
                    )
                }
            }.onFailure { e ->
                _events.send(e.toMessageEvent())
            }
        }
    }

    private fun Throwable.toMessageEvent(): HomeEvent =
        if (this is ApiException && isNotLoggedIn) {
            HomeEvent.ShowMessageRes(R.string.please_login)
        } else {
            HomeEvent.ShowMessage(message ?: "")
        }
}
