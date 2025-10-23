package com.zhiyouyunjing.app.data

import android.content.Context
import android.content.SharedPreferences
import com.zhiyouyunjing.app.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 用户管理器（单例）
 * 负责用户登录状态管理和持久化
 */
object UserManager {

    private lateinit var prefs: SharedPreferences

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    /**
     * 初始化（在Application中调用）
     */
    fun init(context: Context) {
        prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        loadUserFromPrefs()
    }

    /**
     * 登录（本地验证）
     */
    fun login(username: String, password: String): Boolean {
        // TODO: 这里应该调用后端API验证
        // 现在使用简单的本地验证
        if (username.isNotEmpty() && password.length >= 6) {
            val user = User(
                id = "user_${System.currentTimeMillis()}",
                username = username,
                email = "$username@example.com",
                avatar = "",
                phone = ""
            )

            saveUser(user)
            return true
        }
        return false
    }

    /**
     * 注册（本地模拟）
     */
    fun register(username: String, password: String, email: String): Boolean {
        // TODO: 调用后端注册API
        // 现在直接创建用户
        if (username.isNotEmpty() && password.length >= 6 && email.isNotEmpty()) {
            val user = User(
                id = "user_${System.currentTimeMillis()}",
                username = username,
                email = email,
                avatar = "",
                phone = ""
            )

            saveUser(user)
            return true
        }
        return false
    }

    /**
     * 保存用户信息
     */
    private fun saveUser(user: User) {
        _currentUser.value = user
        _isLoggedIn.value = true

        // 持久化到SharedPreferences
        prefs.edit().apply {
            putString("user_id", user.id)
            putString("username", user.username)
            putString("email", user.email)
            putString("avatar", user.avatar)
            putString("phone", user.phone)
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    /**
     * 从本地加载用户信息
     */
    private fun loadUserFromPrefs() {
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)
        if (isLoggedIn) {
            val user = User(
                id = prefs.getString("user_id", "") ?: "",
                username = prefs.getString("username", "") ?: "",
                email = prefs.getString("email", "") ?: "",
                avatar = prefs.getString("avatar", "") ?: "",
                phone = prefs.getString("phone", "") ?: ""
            )
            _currentUser.value = user
            _isLoggedIn.value = true
        }
    }

    /**
     * 退出登录
     */
    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false

        prefs.edit().clear().apply()
    }

    /**
     * 获取当前用户
     */
    fun getCurrentUser(): User? {
        return _currentUser.value
    }
}