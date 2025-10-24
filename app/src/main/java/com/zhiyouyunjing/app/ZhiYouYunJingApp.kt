package com.zhiyouyunjing.app

import android.app.Application
import com.zhiyouyunjing.app.data.SettingsManager

/**
 * 自定义Application类
 */
class ZhiYouYunJingApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化设置管理器
        SettingsManager.init(this)
    }
}