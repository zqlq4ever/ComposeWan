package com.zqlq.common.data.response

import com.zqlq.common.data.bean.ArticleItem
import kotlinx.serialization.Serializable

/**
 * 文章列表响应
 * @property curPage 当前页码
 * @property offset 偏移量
 * @property over 是否结束
 * @property pageCount 总页数
 * @property size 每页大小
 * @property total 总条数
 * @property datas 文章列表
 *
 * JSON示例：
 * {
 *     "curPage": 1,
 *     "offset": 0,
 *     "over": false,
 *     "pageCount": 404,
 *     "size": 20,
 *     "total": 8062,
 *     "datas": [
 *         {
 *             "id": 30256,
 *             "title": "Android 14 新特性详解",
 *             "author": "张三",
 *             "niceDate": "2024-05-20",
 *             "link": "https://www.wanandroid.com/article/30256",
 *             "chapterName": "Android",
 *             "superChapterName": "移动开发"
 *         }
 *     ]
 * }
 */
@Serializable
data class ArticleListResponse(
    val curPage: Int = 0,
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0,
    val datas: List<ArticleItem>? = null
)
