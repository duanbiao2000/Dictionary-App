package com.ahmed_apps.dictionary_app.ui.theme

// 导入 Activity 类，用于创建和管理 Android 应用中的活动屏幕。
import android.app.Activity

// 导入 Build 类，用于获取设备的硬件和软件版本信息。
import android.os.Build

// 导入 isSystemInDarkTheme 函数，用于检查系统是否处于深色模式。
import androidx.compose.foundation.isSystemInDarkTheme

// 导入 MaterialTheme 类，用于定义 Material Design 主题。
import androidx.compose.material3.MaterialTheme

// 导入 darkColorScheme 函数，用于创建深色主题的颜色方案。
import androidx.compose.material3.darkColorScheme

// 导入 dynamicDarkColorScheme 函数，用于根据系统颜色动态生成深色主题的颜色方案。
import androidx.compose.material3.dynamicDarkColorScheme

// 导入 dynamicLightColorScheme 函数，用于根据系统颜色动态生成浅色主题的颜色方案。
import androidx.compose.material3.dynamicLightColorScheme

// 导入 lightColorScheme 函数，用于创建浅色主题的颜色方案。
import androidx.compose.material3.lightColorScheme

// 导入 @Composable 注解，用于标记可组合的函数。
import androidx.compose.runtime.Composable

// 导入 SideEffect 函数，用于执行具有副作用的操作。
import androidx.compose.runtime.SideEffect

// 导入 toArgb 扩展函数，用于将颜色转换为 ARGB 格式。
import androidx.compose.ui.graphics.toArgb

// 导入 LocalContext，用于获取当前上下文。
import androidx.compose.ui.platform.LocalContext

// 导入 LocalView，用于获取当前视图。
import androidx.compose.ui.platform.LocalView

// 导入 WindowCompat 类，用于兼容不同版本的 Window API。
import androidx.core.view.WindowCompat


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

/**
 * Defines the application theme, supporting light and dark modes as well as dynamic color schemes.
 *
 * @param darkTheme Indicates whether to use the dark theme. By default, it is determined based on the system theme.
 * @param dynamicColor Indicates whether to enable dynamic color schemes. Requires Android 12 or above. Defaults to true.
 * @param content The Composable function to be displayed within the themed context.
 *
 * This function integrates the theme style judgment and application logic for the DictionaryApp. It decides whether to use a light or dark theme,
 * and on Android 12 and above, it can enable dynamic color schemes that respond to changes in ambient light and device settings.
 */
@Composable
fun DictionaryAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // 确定颜色方案，基于是否支持动态颜色以及系统或用户是否偏好深色主题。
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // 获取当前视图以判断是否处于编辑模式。如果不是编辑模式，则继续应用主题样式。
    val view = LocalView.current
    if (!view.isInEditMode) {
        // 使用 SideEffect 处理副作用，例如状态栏颜色变化，而不影响组件的可组合性。
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    // 应用确定的颜色方案和字体样式，并显示提供的内容。
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

