package com.zqlq.composewan.viewmodel.hot

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.R
import com.zqlq.composewan.data.model.HotKeyItem
import com.zqlq.composewan.data.model.WebsiteItem
import com.zqlq.composewan.state.hot.HotEffect
import com.zqlq.composewan.state.hot.HotIntent
import com.zqlq.composewan.state.hot.HotState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 热门页面 ViewModel
 */
class HotViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(HotState())
    val state: StateFlow<HotState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HotEffect>()
    val effect: SharedFlow<HotEffect> = _effect.asSharedFlow()

    init {
        processIntent(HotIntent.LoadData)
    }

    fun processIntent(intent: HotIntent) {
        when (intent) {
            is HotIntent.LoadData -> loadData()
            is HotIntent.HotKeyClick -> onHotKeyClick(intent.name)
            is HotIntent.WebsiteClick -> onWebsiteClick(intent.url)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            runCatching {
                val hotKeys = mockHotKeys()
                val websites = mockWebsites()
                Pair(hotKeys, websites)
            }.onSuccess { (hotKeys, websites) ->
                _state.update {
                    it.copy(
                        hotKeys = hotKeys,
                        websites = websites,
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

    private fun onHotKeyClick(name: String) {
        viewModelScope.launch {
            val message = getApplication<Application>().getString(R.string.clicked_item, name)
            _effect.emit(HotEffect.ShowToast(message))
        }
    }

    private fun onWebsiteClick(url: String) {
        viewModelScope.launch {
            _effect.emit(HotEffect.NavigateToWebView(url))
        }
    }

    private fun mockHotKeys(): List<HotKeyItem> = listOf(
        HotKeyItem(1, "Kotlin"),
        HotKeyItem(2, "Java"),
        HotKeyItem(3, "Android"),
        HotKeyItem(4, "Flutter"),
        HotKeyItem(5, "Compose"),
        HotKeyItem(6, "Jetpack"),
        HotKeyItem(7, "MVVM"),
        HotKeyItem(8, "MVI"),
        HotKeyItem(9, "Retrofit"),
        HotKeyItem(10, "OkHttp"),
        HotKeyItem(11, "Room"),
        HotKeyItem(12, "Hilt"),
        HotKeyItem(13, "Coroutines"),
        HotKeyItem(14, "Flow"),
        HotKeyItem(15, "LiveData")
    )

    private fun mockWebsites(): List<WebsiteItem> = listOf(
        WebsiteItem(1, "玩Android", "https://www.wanandroid.com"),
        WebsiteItem(2, "掘金", "https://juejin.cn"),
        WebsiteItem(3, "CSDN", "https://www.csdn.net"),
        WebsiteItem(4, "GitHub", "https://github.com"),
        WebsiteItem(5, "Stack Overflow", "https://stackoverflow.com"),
        WebsiteItem(6, "Google Developers", "https://developers.google.com"),
        WebsiteItem(7, "Android官方", "https://developer.android.com"),
        WebsiteItem(8, "Kotlin官方", "https://kotlinlang.org"),
        WebsiteItem(9, "简书", "https://www.jianshu.com")
    )
}