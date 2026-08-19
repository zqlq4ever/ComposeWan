package com.zqlq.composewan

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zqlq.common.utils.prefs.AppPreferences
import com.zqlq.common.utils.prefs.ThemeMode
import com.zqlq.common.utils.prefs.ThemeSkin
import com.zqlq.common.utils.toast.ToastUtils
import com.zqlq.compose.ui.theme.ComposeWanTheme
import com.zqlq.composewan.ui.navigation.MainNavigation

/**
 * 应用主入口 Activity
 * 负责初始化 UI 并设置边缘到边缘显示模式
 */
class MainActivity : AppCompatActivity() {
    /**
     * Activity 创建时的初始化
     * @param savedInstanceState 保存的实例状态
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by AppPreferences.themeModeFlow.collectAsStateWithLifecycle()
            val themeSkin by AppPreferences.themeSkinFlow.collectAsStateWithLifecycle()
            ComposeWanTheme(
                themeMode = themeMode,
                themeSkin = themeSkin,
            ) {
                AppContent()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
            (context as? AppCompatActivity)?.finish()
        } else {
            ToastUtils.show(R.string.press_back_again_to_exit)
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
    ComposeWanTheme(
        themeMode = ThemeMode.SYSTEM,
        themeSkin = ThemeSkin.PURPLE,
    ) {
        AppContent()
    }
}
