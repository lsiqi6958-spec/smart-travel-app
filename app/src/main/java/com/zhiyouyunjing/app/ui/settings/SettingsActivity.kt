package com.zhiyouyunjing.app.ui.settings

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.zhiyouyunjing.app.BaseActivity
import com.zhiyouyunjing.app.R
import com.zhiyouyunjing.app.data.SettingsManager
import com.zhiyouyunjing.app.databinding.ActivitySettingsBinding

/**
 * 设置页面
 */
class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    // 用于防止重复触发
    private var isUpdatingUI = false
    // 标记是否需要在返回时通知MainActivity刷新
    private var needsRefresh = false

    companion object {
        private const val TAG = "SettingsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "━━━ onCreate called ━━━")

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadCurrentSettings()
        setupEventListeners()
    }

    /**
     * 设置Toolbar
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "设置"
        }
    }

    /**
     * 加载当前设置值到UI（不触发监听器）
     */
    private fun loadCurrentSettings() {
        isUpdatingUI = true

        // 老年模式
        val elderMode = SettingsManager.elderMode.value
        binding.switchElderMode.isChecked = elderMode

        // 字体大小
        val fontSize = SettingsManager.fontSize.value
        binding.seekbarFontSize.progress = when (fontSize) {
            SettingsManager.FontSize.SMALL -> 0
            SettingsManager.FontSize.MEDIUM -> 1
            SettingsManager.FontSize.LARGE -> 2
            SettingsManager.FontSize.EXTRA_LARGE -> 3
        }

        // 主题模式
        when (SettingsManager.themeMode.value) {
            SettingsManager.ThemeMode.LIGHT -> binding.radioThemeLight.isChecked = true
            SettingsManager.ThemeMode.DARK -> binding.radioThemeDark.isChecked = true
            SettingsManager.ThemeMode.SYSTEM -> binding.radioThemeSystem.isChecked = true
        }

        // 更新UI显示
        updateFontSizeLabel(fontSize)
        updateFontSizePreview(fontSize)
        updateElderModeUI(elderMode)

        isUpdatingUI = false

        Log.d(TAG, "设置已加载: elderMode=$elderMode, fontSize=$fontSize")
    }

    /**
     * 设置事件监听器（只设置一次）
     */
    private fun setupEventListeners() {
        // 老年模式Switch
        binding.switchElderMode.setOnCheckedChangeListener { _, isChecked ->
            if (!isUpdatingUI) {
                Log.d(TAG, "▶▶▶ 老年模式切换: $isChecked")
                handleElderModeChange(isChecked)
            }
        }

        // 字体大小SeekBar - 只在停止拖动时保存
        binding.seekbarFontSize.setOnSeekBarChangeListener(
            object : android.widget.SeekBar.OnSeekBarChangeListener {

                override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {
                    Log.d(TAG, "👆 开始拖动SeekBar")
                }

                override fun onProgressChanged(
                    seekBar: android.widget.SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser && !isUpdatingUI) {
                        val fontSize = getFontSizeFromProgress(progress)
                        // 仅更新预览，不保存设置
                        updateFontSizeLabel(fontSize)
                        updateFontSizePreview(fontSize)
                    }
                }

                override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                    if (seekBar != null && !isUpdatingUI) {
                        val fontSize = getFontSizeFromProgress(seekBar.progress)
                        Log.d(TAG, "▶▶▶ 字体大小调整: $fontSize")
                        handleFontSizeChange(fontSize)
                    }
                }
            }
        )

        // 主题模式RadioGroup
        binding.radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            if (!isUpdatingUI) {
                val themeMode = when (checkedId) {
                    R.id.radio_theme_light -> SettingsManager.ThemeMode.LIGHT
                    R.id.radio_theme_dark -> SettingsManager.ThemeMode.DARK
                    R.id.radio_theme_system -> SettingsManager.ThemeMode.SYSTEM
                    else -> SettingsManager.ThemeMode.SYSTEM
                }
                Log.d(TAG, "▶▶▶ 主题模式切换: $themeMode")
                handleThemeChange(themeMode)
            }
        }
    }

    /**
     * 处理老年模式切换
     */
    private fun handleElderModeChange(enabled: Boolean) {
        // 保存设置
        SettingsManager.setElderMode(enabled)

        // 更新UI状态
        updateElderModeUI(enabled)

        // 标记需要刷新
        needsRefresh = true

        // 显示提示
        val message = "老年模式已${if (enabled) "开启" else "关闭"}，返回后生效"
        showMessage(message)

        Log.d(TAG, "老年模式已保存: $enabled")
    }

    /**
     * 处理字体大小调整
     */
    private fun handleFontSizeChange(fontSize: SettingsManager.FontSize) {
        // 保存设置
        SettingsManager.setFontSize(fontSize)

        // 标记需要刷新
        needsRefresh = true

        // 显示提示
        showMessage("字体大小已设置为：${fontSize.displayName}，返回后生效")

        Log.d(TAG, "字体大小已保存: $fontSize")
    }

    /**
     * 处理主题切换
     */
    private fun handleThemeChange(themeMode: SettingsManager.ThemeMode) {
        // 保存设置
        SettingsManager.setThemeMode(themeMode)

        // 标记需要刷新
        needsRefresh = true

        // 显示提示
        showMessage("主题已切换为：${themeMode.displayName}，重启APP后完全生效")

        Log.d(TAG, "主题模式已保存: $themeMode")
    }

    /**
     * 更新老年模式相关UI
     */
    private fun updateElderModeUI(enabled: Boolean) {
        // 老年模式开启时禁用字体大小调节
        binding.seekbarFontSize.isEnabled = !enabled
        binding.tvFontSizeLabel.isEnabled = !enabled

        if (enabled) {
            binding.tvFontSizeLabel.text = "当前字体：老年模式（固定超大）"
            binding.tvFontSizePreview.apply {
                text = "字体预览：老年模式（固定150%）"
                textSize = 14f * 1.5f
            }
        } else {
            val fontSize = SettingsManager.fontSize.value
            updateFontSizeLabel(fontSize)
            updateFontSizePreview(fontSize)
        }
    }

    /**
     * 更新字体大小标签
     */
    private fun updateFontSizeLabel(fontSize: SettingsManager.FontSize) {
        binding.tvFontSizeLabel.text = "当前字体：${fontSize.displayName}"
    }

    /**
     * 更新字体预览
     */
    private fun updateFontSizePreview(fontSize: SettingsManager.FontSize) {
        binding.tvFontSizePreview.apply {
            text = "字体预览：${fontSize.displayName}"
            textSize = 14f * fontSize.scale
        }
    }

    /**
     * 从进度值获取字体大小
     */
    private fun getFontSizeFromProgress(progress: Int): SettingsManager.FontSize {
        return when (progress) {
            0 -> SettingsManager.FontSize.SMALL
            1 -> SettingsManager.FontSize.MEDIUM
            2 -> SettingsManager.FontSize.LARGE
            3 -> SettingsManager.FontSize.EXTRA_LARGE
            else -> SettingsManager.FontSize.MEDIUM
        }
    }

    /**
     * 显示提示消息
     */
    private fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * 处理返回按钮
     */
    override fun onSupportNavigateUp(): Boolean {
        handleFinish()
        return true
    }

    /**
     * 处理系统返回键（兼容旧版本）
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        handleFinish()
    }

    /**
     * 统一处理Activity关闭
     */
    private fun handleFinish() {
        if (needsRefresh) {
            Log.d(TAG, "设置已修改，通知调用者刷新")
            // 设置结果码，告诉调用者需要刷新
            setResult(RESULT_OK)
        }
        finish()
    }
}