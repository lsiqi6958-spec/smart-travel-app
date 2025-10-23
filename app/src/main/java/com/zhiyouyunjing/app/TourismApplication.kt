package com.zhiyouyunjing.app

import android.app.Application
import com.zhiyouyunjing.app.data.UserManager

/**
 * Application类
 */
class TourismApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化用户管理器
        UserManager.init(this)
    }
}