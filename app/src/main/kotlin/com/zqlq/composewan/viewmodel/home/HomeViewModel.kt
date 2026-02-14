package com.zqlq.composewan.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.BannerItem
import com.zqlq.composewan.state.home.HomeEffect
import com.zqlq.composewan.state.home.HomeIntent
import com.zqlq.composewan.state.home.HomeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 首页 ViewModel
 * 处理用户意图，更新 UI 状态
 */
class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect: SharedFlow<HomeEffect> = _effect.asSharedFlow()

    init {
        processIntent(HomeIntent.LoadData)
    }

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadData -> loadData()
            is HomeIntent.Refresh -> refresh()
            is HomeIntent.LoadMore -> loadMore()
            is HomeIntent.BannerClick -> onBannerClick(intent.url)
            is HomeIntent.ArticleClick -> onArticleClick(intent.url)
            is HomeIntent.SearchClick -> onSearchClick()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            runCatching {
                val banners = mockBanners()
                val articles = mockArticles()
                Pair(banners, articles)
            }.onSuccess { (banners, articles) ->
                _state.update { 
                    it.copy(
                        banners = banners,
                        articles = articles,
                        isLoading = false
                    )
                }
            }.onFailure { e ->
                _state.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun refresh() {
        loadData()
    }

    private fun loadMore() {
        viewModelScope.launch {
            val moreArticles = mockArticles().map { 
                it.copy(id = it.id + _state.value.articles.size)
            }
            _state.update { 
                it.copy(articles = it.articles + moreArticles)
            }
        }
    }

    private fun onBannerClick(url: String) {
        viewModelScope.launch {
            _effect.emit(HomeEffect.NavigateToWebView(url))
        }
    }

    private fun onArticleClick(url: String) {
        viewModelScope.launch {
            _effect.emit(HomeEffect.NavigateToWebView(url))
        }
    }

    private fun onSearchClick() {
        viewModelScope.launch {
            _effect.emit(HomeEffect.NavigateToSearch)
        }
    }

    private fun mockBanners(): List<BannerItem> = listOf(
        BannerItem(1, "Banner 1", "", "https://www.wanandroid.com"),
        BannerItem(2, "Banner 2", "", "https://www.wanandroid.com"),
        BannerItem(3, "Banner 3", "", "https://www.wanandroid.com"),
        BannerItem(4, "Banner 4", "", "https://www.wanandroid.com"),
        BannerItem(5, "Banner 5", "", "https://www.wanandroid.com")
    )

    private fun mockArticles(): List<ArticleItem> = listOf(
        ArticleItem(1, "Android Jetpack Compose 入门教程", "张三", "2024-01-01", "https://www.wanandroid.com"),
        ArticleItem(2, "Kotlin 协程实战指南", "李四", "2024-01-02", "https://www.wanandroid.com"),
        ArticleItem(3, "MVVM 架构设计模式详解", "王五", "2024-01-03", "https://www.wanandroid.com"),
        ArticleItem(4, "Retrofit + OkHttp 网络请求最佳实践", "赵六", "2024-01-04", "https://www.wanandroid.com"),
        ArticleItem(5, "Room 数据库使用详解", "钱七", "2024-01-05", "https://www.wanandroid.com"),
        ArticleItem(6, "Hilt 依赖注入入门", "孙八", "2024-01-06", "https://www.wanandroid.com"),
        ArticleItem(7, "Compose 动画效果实现", "周九", "2024-01-07", "https://www.wanandroid.com"),
        ArticleItem(8, "Material Design 3 组件使用", "吴十", "2024-01-08", "https://www.wanandroid.com")
    )
}
