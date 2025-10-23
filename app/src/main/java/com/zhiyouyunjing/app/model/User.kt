package com.zhiyouyunjing.app.model

/**
 * 用户数据模型
 */
data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatar: String = "",
    val phone: String = ""
)