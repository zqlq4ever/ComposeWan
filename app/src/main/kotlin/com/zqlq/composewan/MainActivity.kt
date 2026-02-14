package com.zqlq.composewan

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.zqlq.common.ui.theme.ComposeWanTheme
import com.zqlq.common.utils.toast.ToastUtils
import com.zqlq.composewan.ui.navigation.MainNavigation

/**
 * 应用主入口 Activity
 * 负责初始化 UI 并设置边缘到边缘显示模式
 */
class MainActivity : ComponentActivity() {

    /**
     * Activity 创建时的初始化
     * @param savedInstanceState 保存的实例状态
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeWanTheme {
                AppContent()
            }
        }
    }
}

/**
 * 应用主内容 Composable
 * 实现双击返回键退出应用的功能，并加载主导航组件
 */
@Composable
fun AppContent() {
    val context = LocalContext.current
    val backPressInterval = 2000
    var lastBackPressTime by remember { mutableLongStateOf(0) }

    BackHandler {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastBackPressTime < backPressInterval) {
            (context as? ComponentActivity)?.finish()
        } else {
            ToastUtils.show("再按一次退出应用")
            lastBackPressTime = currentTime
        }
    }

    MainNavigation()
}

/**
 * AppContent 预览函数
 * 用于在 Android Studio 中预览应用主内容
 */
@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    ComposeWanTheme {
        AppContent()
    }
}
