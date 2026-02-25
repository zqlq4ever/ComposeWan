package com.zqlq.composewan.viewmodel.system

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.composewan.R
import com.zqlq.composewan.data.model.SystemCategory
import com.zqlq.composewan.data.model.SystemChild
import com.zqlq.composewan.state.system.SystemEffect
import com.zqlq.composewan.state.system.SystemIntent
import com.zqlq.composewan.state.system.SystemState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 体系页面 ViewModel
 */
class SystemViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(SystemState())
    val state: StateFlow<SystemState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SystemEffect>()
    val effect: SharedFlow<SystemEffect> = _effect.asSharedFlow()

    init {
        processIntent(SystemIntent.LoadData)
    }

    fun processIntent(intent: SystemIntent) {
        when (intent) {
            is SystemIntent.LoadData -> loadData()
            is SystemIntent.ToggleExpand -> toggleExpand(intent.categoryId)
            is SystemIntent.ChildClick -> onChildClick(intent.categoryName, intent.children)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            runCatching {
                mockCategories()
            }.onSuccess { categories ->
                _state.update {
                    it.copy(
                        categories = categories,
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

    private fun toggleExpand(categoryId: Int) {
        _state.update { state ->
            val expandedIds = state.expandedIds.toMutableSet()
            if (expandedIds.contains(categoryId)) {
                expandedIds.remove(categoryId)
            } else {
                expandedIds.add(categoryId)
            }
            state.copy(expandedIds = expandedIds)
        }
    }

    private fun onChildClick(categoryName: String, children: List<SystemChild>) {
        viewModelScope.launch {
            _effect.emit(SystemEffect.NavigateToSystemDetail(categoryName, children))
        }
    }

    private fun mockCategories(): List<SystemCategory> = listOf(
        SystemCategory(
            id = 1,
            name = "Android",
            children = listOf(
                SystemChild(101, "Activity"),
                SystemChild(102, "Service"),
                SystemChild(103, "Broadcast"),
                SystemChild(104, "ContentProvider"),
                SystemChild(105, "Fragment"),
                SystemChild(106, "View"),
                SystemChild(107, "动画"),
                SystemChild(108, "IPC")
            )
        ),
        SystemCategory(
            id = 2,
            name = "Kotlin",
            children = listOf(
                SystemChild(201, "协程"),
                SystemChild(202, "Flow"),
                SystemChild(203, "扩展函数"),
                SystemChild(204, "密封类"),
                SystemChild(205, "数据类"),
                SystemChild(206, "高阶函数")
            )
        ),
        SystemCategory(
            id = 3,
            name = "Jetpack",
            children = listOf(
                SystemChild(301, "ViewModel"),
                SystemChild(302, "LiveData"),
                SystemChild(303, "Room"),
                SystemChild(304, "Navigation"),
                SystemChild(305, "DataStore"),
                SystemChild(306, "WorkManager"),
                SystemChild(307, "Hilt"),
                SystemChild(308, "Paging")
            )
        ),
        SystemCategory(
            id = 4,
            name = "Compose",
            children = listOf(
                SystemChild(401, "基础组件"),
                SystemChild(402, "布局"),
                SystemChild(403, "状态管理"),
                SystemChild(404, "副作用"),
                SystemChild(405, "动画"),
                SystemChild(406, "手势"),
                SystemChild(407, "主题")
            )
        ),
        SystemCategory(
            id = 5,
            name = "网络",
            children = listOf(
                SystemChild(501, "Retrofit"),
                SystemChild(502, "OkHttp"),
                SystemChild(503, "WebSocket"),
                SystemChild(504, "RESTful")
            )
        ),
        SystemCategory(
            id = 6,
            name = "架构",
            children = listOf(
                SystemChild(601, "MVC"),
                SystemChild(602, "MVP"),
                SystemChild(603, "MVVM"),
                SystemChild(604, "MVI"),
                SystemChild(605, "Clean Architecture")
            )
        )
    )
}
