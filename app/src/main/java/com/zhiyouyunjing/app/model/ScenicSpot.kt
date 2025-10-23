package com.zhiyouyunjing.app.model

/**
 * 景点数据模型
 */
data class ScenicSpot(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val location: String,
    val rating: Float,
    var isFavorite: Boolean = false
)