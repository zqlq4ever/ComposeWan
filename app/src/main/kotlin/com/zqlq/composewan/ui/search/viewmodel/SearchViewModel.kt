package com.zqlq.composewan.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.ui.common.toLoadError
import com.zqlq.composewan.ui.search.usecase.SearchUseCase
import com.zqlq.composewan.ui.search.viewmodel.contract.SearchEvent
import com.zqlq.composewan.ui.search.viewmodel.contract.SearchIntent
import com.zqlq.composewan.ui.search.viewmodel.contract.SearchUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 搜索 ViewModel
 */
@OptIn(FlowPreview::class)
class SearchViewModel(
    private val useCase: SearchUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _events = Channel<SearchEvent>(Channel.BUFFERED)
    val events: Flow<SearchEvent> = _events.receiveAsFlow()

    private val searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .onEach { query -> performSearch(query) }
            .launchIn(viewModelScope)
    }

    fun handleIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateQuery -> updateQuery(intent.query)
            SearchIntent.Search -> performSearch(_uiState.value.searchQuery)
            SearchIntent.Clear -> clearSearch()
            is SearchIntent.ArticleClick -> onArticleClick(intent.url)
        }
    }

    private fun updateQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchQuery.value = query
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) return
        searchJob?.cancel()
        searchJob =
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, error = null) }
                try {
                    val page = useCase.search(query)
                    _uiState.update {
                        it.copy(searchResults = page.articles, isLoading = false, error = null)
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Throwable) {
                    _uiState.update { it.copy(isLoading = false, error = e.toLoadError()) }
                }
            }
    }

    private fun clearSearch() {
        searchJob?.cancel()
        searchJob = null
        _uiState.update { SearchUiState() }
        searchQuery.value = ""
    }

    private fun onArticleClick(url: String) {
        viewModelScope.launch { _events.send(SearchEvent.NavigateToWebView(url)) }
    }
}
