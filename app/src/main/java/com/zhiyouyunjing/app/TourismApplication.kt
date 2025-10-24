package com.zhiyouyunjing.app

import android.app.Application
import com.zhiyouyunjing.app.data.UserManager
import com.zhiyouyunjing.app.data.SettingsManager  // ✅ 添加这行

/**
 * Application类
 */
class TourismApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化设置管理器（必须在最前面）
        SettingsManager.init(this)  // ✅ 添加这行

        // 初始化用户管理器
        UserManager.init(this)
    }


}