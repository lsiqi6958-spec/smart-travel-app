package com.zhiyouyunjing.app.data

import com.zhiyouyunjing.app.model.ScenicSpot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 收藏管理器（单例）
 */
object FavoritesManager {

    private val _favorites = MutableStateFlow<List<ScenicSpot>>(emptyList())
    val favorites: StateFlow<List<ScenicSpot>> = _favorites.asStateFlow()

    /**
     * 添加收藏
     */
    fun addFavorite(spot: ScenicSpot) {
        val currentList = _favorites.value.toMutableList()
        if (!currentList.any { it.id == spot.id }) {
            currentList.add(spot.copy(isFavorite = true))
            _favorites.value = currentList
        }
    }

    /**
     * 移除收藏
     */
    fun removeFavorite(spotId: String) {
        val currentList = _favorites.value.toMutableList()
        currentList.removeAll { it.id == spotId }
        _favorites.value = currentList
    }

    /**
     * 切换收藏状态
     */
    fun toggleFavorite(spot: ScenicSpot) {
        if (isFavorite(spot.id)) {
            removeFavorite(spot.id)
        } else {
            addFavorite(spot)
        }
    }

    /**
     * 检查是否已收藏
     */
    fun isFavorite(spotId: String): Boolean {
        return _favorites.value.any { it.id == spotId }
    }

    /**
     * 获取所有收藏
     */
    fun getAllFavorites(): List<ScenicSpot> {
        return _favorites.value
    }
}