package com.zqlq.composewan.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.state.search.SearchEffect
import com.zqlq.composewan.state.search.SearchIntent
import com.zqlq.composewan.state.search.SearchState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 搜索 ViewModel
 * 处理用户意图，更新 UI 状态
 */
@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchEffect>()
    val effect: SharedFlow<SearchEffect> = _effect.asSharedFlow()

    private val searchQuery = MutableStateFlow("")

    init {
        searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .onEach { query ->
                performSearch(query)
            }
            .launchIn(viewModelScope)
    }

    fun processIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateQuery -> updateQuery(intent.query)
            is SearchIntent.Search -> performSearch(_state.value.searchQuery)
            is SearchIntent.Clear -> clearSearch()
            is SearchIntent.ArticleClick -> onArticleClick(intent.url)
        }
    }

    private fun updateQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        searchQuery.value = query
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            runCatching {
                mockSearchResults(query)
            }.onSuccess { results ->
                _state.update {
                    it.copy(
                        searchResults = results,
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

    private fun clearSearch() {
        _state.update {
            SearchState()
        }
        searchQuery.value = ""
    }

    private fun onArticleClick(url: String) {
        viewModelScope.launch {
            _effect.emit(SearchEffect.NavigateToWebView(url))
        }
    }

    private fun mockSearchResults(query: String): List<ArticleItem> {
        val allArticles = listOf(
            ArticleItem(1, "Android Jetpack Compose 入门教程", "张三", "2024-01-01", "https://www.wanandroid.com", "Android", false),
            ArticleItem(2, "Kotlin 协程实战指南", "李四", "2024-01-02", "https://www.wanandroid.com", "Kotlin", true),
            ArticleItem(3, "MVVM 架构设计模式详解", "王五", "2024-01-03", "https://www.wanandroid.com", "架构", false),
            ArticleItem(4, "Retrofit + OkHttp 网络请求最佳实践", "赵六", "2024-01-04", "https://www.wanandroid.com", "网络", false),
            ArticleItem(5, "Room 数据库使用详解", "钱七", "2024-01-05", "https://www.wanandroid.com", "数据库", true),
            ArticleItem(6, "Hilt 依赖注入入门", "孙八", "2024-01-06", "https://www.wanandroid.com", "依赖注入", false),
            ArticleItem(7, "Compose 动画效果实现", "周九", "2024-01-07", "https://www.wanandroid.com", "动画", false),
            ArticleItem(8, "Material Design 3 组件使用", "吴十", "2024-01-08", "https://www.wanandroid.com", "UI", false),
            ArticleItem(9, "Android 性能优化技巧", "郑一", "2024-01-09", "https://www.wanandroid.com", "性能", false),
            ArticleItem(10, "Kotlin Flow 响应式编程", "冯二", "2024-01-10", "https://www.wanandroid.com", "Kotlin", true)
        )

        return allArticles.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.author.contains(query, ignoreCase = true) ||
                    it.chapterName.contains(query, ignoreCase = true)
        }.ifEmpty {
            listOf(
                ArticleItem(
                    id = 0,
                    title = "未找到 \"$query\" 相关结果",
                    author = "",
                    time = "",
                    url = "",
                    chapterName = "",
                    isCollect = false
                )
            )
        }
    }
}
