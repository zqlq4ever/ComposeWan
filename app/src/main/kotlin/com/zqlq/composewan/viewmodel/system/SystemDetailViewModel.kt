package com.zqlq.composewan.viewmodel.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.SystemChild
import com.zqlq.composewan.state.system.SystemDetailEffect
import com.zqlq.composewan.state.system.SystemDetailIntent
import com.zqlq.composewan.state.system.SystemDetailState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * 体系详情页面 ViewModel
 *
 * @param categoryName 分类名称
 * @param children 子分类列表
 */
class SystemDetailViewModel(
    private val categoryName: String,
    private val children: List<SystemChild>
) : ViewModel() {

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(
        SystemDetailState(
            categoryName = categoryName,
            children = children,
            selectedChildId = children.firstOrNull()?.id ?: 0
        )
    )
    val state: StateFlow<SystemDetailState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SystemDetailEffect>()
    val effect: SharedFlow<SystemDetailEffect> = _effect.asSharedFlow()

    init {
        loadArticles()
    }

    fun processIntent(intent: SystemDetailIntent) {
        when (intent) {
            is SystemDetailIntent.SelectChild -> selectChild(intent.childId)
            is SystemDetailIntent.ArticleClick -> onArticleClick(intent.url)
            is SystemDetailIntent.CollectClick -> onCollectClick(intent.articleId, intent.isCollect)
            is SystemDetailIntent.LoadMore -> loadMoreArticles()
        }
    }

    private fun selectChild(childId: Int) {
        _state.update {
            it.copy(
                selectedChildId = childId,
                articles = emptyList()
            )
        }
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            runCatching {
                mockArticles()
            }.onSuccess { articles ->
                _state.update {
                    it.copy(
                        articles = articles,
                        isLoading = false
                    )
                }
            }.onFailure {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "加载失败"
                    )
                }
            }
        }
    }

    private fun loadMoreArticles() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            runCatching {
                mockArticles().map { 
                    it.copy(id = it.id + _state.value.articles.size)
                }
            }.onSuccess { moreArticles ->
                _state.update {
                    it.copy(
                        articles = it.articles + moreArticles,
                        isLoading = false
                    )
                }
            }.onFailure {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onArticleClick(url: String) {
        viewModelScope.launch {
            _effect.emit(SystemDetailEffect.NavigateToWebView(url))
        }
    }

    private fun onCollectClick(articleId: Int, isCollect: Boolean) {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    articles = state.articles.map { article ->
                        if (article.id == articleId) {
                            article.copy(isCollect = !isCollect)
                        } else {
                            article
                        }
                    }
                )
            }
        }
    }

    private fun mockArticles(): List<ArticleItem> {
        val selectedChild = _state.value.children.find { it.id == _state.value.selectedChildId } ?: return emptyList()
        
        return listOf(
            ArticleItem(
                id = 1,
                title = "深入理解 ${selectedChild.name} 开发",
                author = "张三",
                time = "2024-01-01",
                url = "https://www.wanandroid.com",
                chapterName = selectedChild.name,
                isCollect = false
            ),
            ArticleItem(
                id = 2,
                title = "${selectedChild.name} 最佳实践指南",
                author = "李四",
                time = "2024-01-02",
                url = "https://www.wanandroid.com",
                chapterName = selectedChild.name,
                isCollect = true
            ),
            ArticleItem(
                id = 3,
                title = "${selectedChild.name} 性能优化技巧",
                author = "王五",
                time = "2024-01-03",
                url = "https://www.wanandroid.com",
                chapterName = selectedChild.name,
                isCollect = false
            ),
            ArticleItem(
                id = 4,
                title = "如何学习 ${selectedChild.name}",
                author = "赵六",
                time = "2024-01-04",
                url = "https://www.wanandroid.com",
                chapterName = selectedChild.name,
                isCollect = false
            )
        )
    }
}

/**
 * SystemDetailViewModel Factory
 */
class SystemDetailViewModelFactory(
    private val categoryName: String,
    private val children: List<SystemChild>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SystemDetailViewModel::class.java)) {
            return SystemDetailViewModel(categoryName, children) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
