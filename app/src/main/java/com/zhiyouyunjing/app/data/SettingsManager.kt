package com.zhiyouyunjing.app.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 设置管理器 - 使用单例模式
 */
object SettingsManager {

    private const val TAG = "SettingsManager"
    private const val PREFS_NAME = "app_settings"
    private const val KEY_ELDER_MODE = "elder_mode"
    private const val KEY_FONT_SIZE = "font_size"
    private const val KEY_THEME_MODE = "theme_mode"

    private lateinit var prefs: SharedPreferences

    // 老年模式
    private val _elderMode = MutableStateFlow(false)
    val elderMode: StateFlow<Boolean> = _elderMode

    // 字体大小
    private val _fontSize = MutableStateFlow(FontSize.MEDIUM)
    val fontSize: StateFlow<FontSize> = _fontSize

    // 主题模式
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode

    /**
     * 初始化（在Application中调用）
     */
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        loadSettings()
        Log.d(TAG, "SettingsManager initialized")
    }

    /**
     * 加载保存的设置（带容错处理）
     */
    private fun loadSettings() {
        // 老年模式
        _elderMode.value = try {
            prefs.getBoolean(KEY_ELDER_MODE, false)
        } catch (e: Exception) {
            Log.e(TAG, "加载老年模式设置失败，使用默认值", e)
            prefs.edit().remove(KEY_ELDER_MODE).apply()
            false
        }

        // 字体大小
        _fontSize.value = try {
            val value = prefs.getInt(KEY_FONT_SIZE, FontSize.MEDIUM.value)
            FontSize.fromValue(value)
        } catch (e: ClassCastException) {
            Log.e(TAG, "字体大小数据类型错误，清除旧数据", e)
            prefs.edit().remove(KEY_FONT_SIZE).apply()
            FontSize.MEDIUM
        } catch (e: Exception) {
            Log.e(TAG, "加载字体大小设置失败，使用默认值", e)
            prefs.edit().remove(KEY_FONT_SIZE).apply()
            FontSize.MEDIUM
        }

        // 主题模式
        _themeMode.value = try {
            val value = prefs.getInt(KEY_THEME_MODE, ThemeMode.SYSTEM.value)
            ThemeMode.fromValue(value)
        } catch (e: ClassCastException) {
            Log.e(TAG, "主题模式数据类型错误，清除旧数据", e)
            prefs.edit().remove(KEY_THEME_MODE).apply()
            ThemeMode.SYSTEM
        } catch (e: Exception) {
            Log.e(TAG, "加载主题模式设置失败，使用默认值", e)
            prefs.edit().remove(KEY_THEME_MODE).apply()
            ThemeMode.SYSTEM
        }

        // 应用主题
        AppCompatDelegate.setDefaultNightMode(_themeMode.value.value)

        Log.d(TAG, "设置已加载: elderMode=${_elderMode.value}, fontSize=${_fontSize.value}, themeMode=${_themeMode.value}")
    }

    /**
     * 设置老年模式
     */
    fun setElderMode(enabled: Boolean) {
        _elderMode.value = enabled
        prefs.edit().putBoolean(KEY_ELDER_MODE, enabled).apply()
        Log.d(TAG, "老年模式已保存: $enabled")
    }

    /**
     * 设置字体大小
     */
    fun setFontSize(size: FontSize) {
        _fontSize.value = size
        prefs.edit().putInt(KEY_FONT_SIZE, size.value).apply()
        Log.d(TAG, "字体大小已保存: $size")
    }

    /**
     * 设置主题模式
     */
    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        prefs.edit().putInt(KEY_THEME_MODE, mode.value).apply()

        // 立即应用主题
        AppCompatDelegate.setDefaultNightMode(mode.value)
        Log.d(TAG, "主题模式已保存: $mode")
    }

    /**
     * 字体大小枚举
     */
    enum class FontSize(val value: Int, val displayName: String, val scale: Float) {
        SMALL(0, "小", 0.85f),
        MEDIUM(1, "中", 1.0f),
        LARGE(2, "大", 1.15f),
        EXTRA_LARGE(3, "特大", 1.3f);

        companion object {
            fun fromValue(value: Int): FontSize {
                return values().find { it.value == value } ?: MEDIUM
            }
        }
    }

    /**
     * 主题模式枚举
     */
    enum class ThemeMode(val value: Int, val displayName: String) {
        LIGHT(AppCompatDelegate.MODE_NIGHT_NO, "浅色"),
        DARK(AppCompatDelegate.MODE_NIGHT_YES, "深色"),
        SYSTEM(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, "跟随系统");

        companion object {
            fun fromValue(value: Int): ThemeMode {
                return when (value) {
                    AppCompatDelegate.MODE_NIGHT_NO -> LIGHT
                    AppCompatDelegate.MODE_NIGHT_YES -> DARK
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> SYSTEM
                    else -> SYSTEM
                }
            }
        }
    }
}