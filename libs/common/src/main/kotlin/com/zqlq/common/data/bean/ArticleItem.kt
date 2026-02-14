package com.zqlq.common.data.bean

import kotlinx.serialization.Serializable

/**
 * 文章列表项
 * @property apkLink APK链接
 * @property audit 审核状态
 * @property author 作者
 * @property canEdit 是否可编辑
 * @property chapterId 章节ID
 * @property chapterName 章节名称
 * @property collect 是否收藏
 * @property courseId 课程ID
 * @property desc 描述
 * @property descMd Markdown描述
 * @property envelopePic 封面图片
 * @property fresh 是否新鲜
 * @property host 主机
 * @property id 文章ID
 * @property link 链接
 * @property niceDate 友好日期
 * @property niceShareDate 友好分享日期
 * @property origin 来源
 * @property prefix 前缀
 * @property projectLink 项目链接
 * @property publishTime 发布时间
 * @property realSuperChapterId 真实的父章节ID
 * @property selfVisible 自身可见性
 * @property shareDate 分享日期
 * @property shareUser 分享用户
 * @property superChapterId 父章节ID
 * @property superChapterName 父章节名称
 * @property tags 标签
 * @property title 标题
 * @property type 类型
 * @property userId 用户ID
 * @property visible 可见性
 * @property zan 点赞数
 */
@Serializable
data class ArticleItem(
    val apkLink: String? = null,
    val audit: Int = 0,
    val author: String? = null,
    val canEdit: Boolean = false,
    val chapterId: Int = 0,
    val chapterName: String? = null,
    val collect: Boolean = false,
    val courseId: Int = 0,
    val desc: String? = null,
    val descMd: String? = null,
    val envelopePic: String? = null,
    val fresh: Boolean = false,
    val host: String? = null,
    val id: Int = 0,
    val link: String? = null,
    val niceDate: String? = null,
    val niceShareDate: String? = null,
    val origin: String? = null,
    val prefix: String? = null,
    val projectLink: String? = null,
    val publishTime: Long = 0,
    val realSuperChapterId: Int = 0,
    val selfVisible: Int = 0,
    val shareDate: Long = 0,
    val shareUser: String? = null,
    val superChapterId: Int = 0,
    val superChapterName: String? = null,
    val tags: List<TagItem>? = null,
    val title: String? = null,
    val type: Int = 0,
    val userId: Int = 0,
    val visible: Int = 0,
    val zan: Int = 0
)

/**
 * 文章标签
 * @property name 标签名称
 * @property url 标签URL
 */
@Serializable
data class TagItem(
    val name: String? = null,
    val url: String? = null
)
