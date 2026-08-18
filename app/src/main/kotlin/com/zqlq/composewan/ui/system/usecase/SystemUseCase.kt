package com.zqlq.composewan.ui.system.usecase

import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.SystemCategory
import com.zqlq.composewan.data.model.SystemChild

/**
 * 体系页用例。本阶段返回 mock。
 */
class SystemUseCase {
    fun loadCategories(): List<SystemCategory> =
        listOf(
            SystemCategory(
                id = 1,
                name = "Android",
                children =
                    listOf(
                        SystemChild(101, "Activity"),
                        SystemChild(102, "Service"),
                        SystemChild(103, "Broadcast"),
                        SystemChild(104, "ContentProvider"),
                        SystemChild(105, "Fragment"),
                        SystemChild(106, "View"),
                        SystemChild(107, "动画"),
                        SystemChild(108, "IPC"),
                    ),
            ),
            SystemCategory(
                id = 2,
                name = "Kotlin",
                children =
                    listOf(
                        SystemChild(201, "协程"),
                        SystemChild(202, "Flow"),
                        SystemChild(203, "扩展函数"),
                        SystemChild(204, "密封类"),
                        SystemChild(205, "数据类"),
                        SystemChild(206, "高阶函数"),
                    ),
            ),
            SystemCategory(
                id = 3,
                name = "Jetpack",
                children =
                    listOf(
                        SystemChild(301, "ViewModel"),
                        SystemChild(302, "LiveData"),
                        SystemChild(303, "Room"),
                        SystemChild(304, "Navigation"),
                        SystemChild(305, "DataStore"),
                        SystemChild(306, "WorkManager"),
                        SystemChild(307, "Hilt"),
                        SystemChild(308, "Paging"),
                    ),
            ),
            SystemCategory(
                id = 4,
                name = "Compose",
                children =
                    listOf(
                        SystemChild(401, "基础组件"),
                        SystemChild(402, "布局"),
                        SystemChild(403, "状态管理"),
                        SystemChild(404, "副作用"),
                        SystemChild(405, "动画"),
                        SystemChild(406, "手势"),
                        SystemChild(407, "主题"),
                    ),
            ),
            SystemCategory(
                id = 5,
                name = "网络",
                children =
                    listOf(
                        SystemChild(501, "Retrofit"),
                        SystemChild(502, "OkHttp"),
                        SystemChild(503, "WebSocket"),
                        SystemChild(504, "RESTful"),
                    ),
            ),
            SystemCategory(
                id = 6,
                name = "架构",
                children =
                    listOf(
                        SystemChild(601, "MVC"),
                        SystemChild(602, "MVP"),
                        SystemChild(603, "MVVM"),
                        SystemChild(604, "MVI"),
                        SystemChild(605, "Clean Architecture"),
                    ),
            ),
        )

    fun loadArticles(
        childName: String,
        idOffset: Int = 0,
    ): List<ArticleItem> =
        listOf(
            ArticleItem(1 + idOffset, "深入理解 $childName 开发", "张三", "2024-01-01", "https://www.wanandroid.com", childName, false),
            ArticleItem(2 + idOffset, "$childName 最佳实践指南", "李四", "2024-01-02", "https://www.wanandroid.com", childName, true),
            ArticleItem(3 + idOffset, "$childName 性能优化技巧", "王五", "2024-01-03", "https://www.wanandroid.com", childName, false),
            ArticleItem(4 + idOffset, "如何学习 $childName", "赵六", "2024-01-04", "https://www.wanandroid.com", childName, false),
        )
}
