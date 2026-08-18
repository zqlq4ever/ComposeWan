package com.zqlq.composewan.data.mapper

import android.text.Html
import com.zqlq.common.data.response.ArticleListResponse
import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.ArticlePage
import com.zqlq.composewan.data.model.BannerItem
import com.zqlq.composewan.data.model.HotKeyItem
import com.zqlq.composewan.data.model.SystemCategory
import com.zqlq.composewan.data.model.SystemChild
import com.zqlq.composewan.data.model.WebsiteItem
import com.zqlq.common.data.bean.ArticleItem as ApiArticle
import com.zqlq.common.data.bean.BannerItem as ApiBanner
import com.zqlq.common.data.bean.HotKeyItem as ApiHotKey
import com.zqlq.common.data.bean.SystemCategory as ApiSystem
import com.zqlq.common.data.bean.WebsiteItem as ApiWebsite

fun ApiBanner.toUi(): BannerItem =
    BannerItem(
        id = id,
        title = title.orEmpty().plainText(),
        imageUrl = imagePath.orEmpty(),
        url = url.orEmpty(),
    )

fun ApiArticle.toUi(forceCollected: Boolean = false): ArticleItem {
    val displayAuthor =
        author?.takeIf { it.isNotBlank() }
            ?: shareUser?.takeIf { it.isNotBlank() }
            ?: ""
    return ArticleItem(
        id = id,
        title = title.orEmpty().plainText(),
        author = displayAuthor,
        time = niceDate.orEmpty(),
        url = link.orEmpty(),
        chapterName = chapterName.orEmpty(),
        isCollect = forceCollected || collect,
        originId = originId,
    )
}

fun ArticleListResponse.toPage(forceCollected: Boolean = false): ArticlePage =
    ArticlePage(
        articles = datas.orEmpty().map { it.toUi(forceCollected) },
        over = over,
    )

fun ApiHotKey.toUi(): HotKeyItem = HotKeyItem(id = id, name = name.orEmpty())

fun ApiWebsite.toUi(): WebsiteItem =
    WebsiteItem(
        id = id,
        name = name.orEmpty(),
        url = link.orEmpty(),
    )

fun ApiSystem.toUi(): SystemCategory =
    SystemCategory(
        id = id,
        name = name.orEmpty(),
        children = children.orEmpty().map { it.toChild() },
    )

private fun ApiSystem.toChild(): SystemChild =
    SystemChild(
        id = id,
        name = name.orEmpty(),
    )

private fun String.plainText(): String = Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString().trim()
