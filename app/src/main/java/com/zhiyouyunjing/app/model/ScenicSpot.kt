package com.zhiyouyunjing.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 景点数据模型
 */
@Parcelize
data class ScenicSpot(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val location: String,
    val rating: Float,
    var isFavorite: Boolean = false
) : Parcelable