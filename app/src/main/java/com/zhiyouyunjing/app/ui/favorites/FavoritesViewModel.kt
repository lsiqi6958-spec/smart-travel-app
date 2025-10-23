package com.zhiyouyunjing.app.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhiyouyunjing.app.data.FavoritesManager
import com.zhiyouyunjing.app.model.ScenicSpot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 收藏页ViewModel
 */
class FavoritesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    /**
     * 加载收藏列表
     */
    private fun loadFavorites() {
        viewModelScope.launch {
            FavoritesManager.favorites.collect { favorites ->
                _uiState.value = FavoritesUiState(favorites)
            }
        }
    }

    /**
     * 移除收藏
     */
    fun removeFavorite(spot: ScenicSpot) {
        FavoritesManager.removeFavorite(spot.id)
    }
}

/**
 * 收藏页UI状态
 */
data class FavoritesUiState(
    val favorites: List<ScenicSpot> = emptyList()
)