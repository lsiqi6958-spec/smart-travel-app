package com.zhiyouyunjing.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.zhiyouyunjing.app.data.SettingsManager

/**
 * 所有Activity的基类
 * 统一处理字体大小、主题模式等全局设置
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BaseActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 先应用主题，再调用super（确保主题在setContentView之前生效）
        applyThemeSettings()

        super.onCreate(savedInstanceState)

        Log.d(TAG, "${this::class.simpleName} created with fontScale=${resources.configuration.fontScale}")
    }

    /**
     * ✅ 正确的方式：在attachBaseContext中应用字体设置
     * 这个方法在Activity创建的最早期调用，确保字体设置在整个生命周期中生效
     */
    override fun attachBaseContext(newBase: Context) {
        // 应用字体缩放配置
        val context = applyFontScale(newBase)
        super.attachBaseContext(context)
    }

    /**
     * 应用字体缩放到Context
     */
    private fun applyFontScale(context: Context): Context {
        val fontSize = SettingsManager.fontSize.value
        val elderMode = SettingsManager.elderMode.value

        // 根据老年模式和字体大小设置计算最终的字体缩放比例
        val fontScale = when {
            elderMode -> 1.5f  // 老年模式：150%
            else -> fontSize.scale
        }

        Log.d(TAG, "Applying fontScale: $fontScale (elderMode=$elderMode, fontSize=$fontSize)")

        // 创建新的Configuration
        val configuration = Configuration(context.resources.configuration)
        configuration.fontScale = fontScale

        // ✅ 使用createConfigurationContext（推荐的方式）
        return context.createConfigurationContext(configuration)
    }

    /**
     * 应用主题设置
     */
    private fun applyThemeSettings() {
        val currentTheme = SettingsManager.themeMode.value
        val systemMode = currentTheme.value

        // 只在需要时切换主题（避免频繁切换）
        if (AppCompatDelegate.getDefaultNightMode() != systemMode) {
            AppCompatDelegate.setDefaultNightMode(systemMode)
            Log.d(TAG, "Theme changed to: ${currentTheme.displayName}")
        }
    }
}