package com.zqlq.composewan.ui.about.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.zqlq.composewan.R
import com.zqlq.composewan.ui.components.MeshGradientBackground

/**
 * 关于页面
 *
 * @param onBack 返回回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 系统返回键处理
    BackHandler {
        onBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        // 使用TopAppBarDefaults配置标题栏，自动处理状态栏
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.about_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                        )
                    }
                },
                // 使用默认的TopAppBar样式，自动处理状态栏颜色
                colors = TopAppBarDefaults.topAppBarColors(),
            )
        },
    ) { innerPadding ->
        // 使用 MeshGradientBackground 作为页面底色背景，开启动态流动效果
        MeshGradientBackground(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            animated = true,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Spacer(modifier = Modifier.size(30.dp))

                // 毛玻璃卡片：半透明背景保证内容在多彩渐变上可读
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AsyncImage(
                            model =
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data("https://picsum.photos/300?random=1blur=2")
                                    .memoryCachePolicy(CachePolicy.ENABLED) // 内存缓存
                                    .diskCachePolicy(CachePolicy.DISABLED) // 禁用磁盘缓存
                                    .build(),
                            contentDescription = stringResource(R.string.cd_app_icon),
                            modifier =
                                Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )

                        Text(
                            text = stringResource(R.string.about_app_info),
                            textAlign = TextAlign.Center,
                            modifier =
                                Modifier
                                    .padding(top = 24.dp)
                                    .fillMaxWidth(),
                        )

                        Text(
                            text = stringResource(R.string.about_description),
                            modifier =
                                Modifier
                                    .padding(top = 24.dp)
                                    .fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
