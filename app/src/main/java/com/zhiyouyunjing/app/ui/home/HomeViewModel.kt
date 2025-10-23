package com.zhiyouyunjing.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhiyouyunjing.app.model.ScenicSpot
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.zhiyouyunjing.app.data.FavoritesManager

/**
 * 首页ViewModel
 */
class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadAttractions()
    }

    /**
     * 加载景点数据（使用Mock数据）
     */
    private fun loadAttractions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // 模拟网络延迟
                delay(800)

                // Mock数据
                val mockData = listOf(
                    ScenicSpot(
                        id = "1",
                        name = "故宫博物院",
                        description = "中国明清两代的皇家宫殿，世界文化遗产，珍藏大量文物",
                        imageUrl = "",
                        location = "北京市东城区",
                        rating = 4.8f,
                        isFavorite = false
                    ),
                    ScenicSpot(
                        id = "2",
                        name = "西湖",
                        description = "杭州最著名的景点，以秀丽的湖光山色和众多的名胜古迹闻名",
                        imageUrl = "",
                        location = "浙江省杭州市",
                        rating = 4.7f,
                        isFavorite = false
                    ),
                    ScenicSpot(
                        id = "3",
                        name = "长城",
                        description = "世界七大奇迹之一，中国古代的军事防御工程",
                        imageUrl = "",
                        location = "北京市延庆区",
                        rating = 4.9f,
                        isFavorite = true
                    ),
                    ScenicSpot(
                        id = "4",
                        name = "黄山",
                        description = "中国十大风景名胜之一，以奇松、怪石、云海、温泉闻名",
                        imageUrl = "",
                        location = "安徽省黄山市",
                        rating = 4.8f,
                        isFavorite = false
                    ),
                    ScenicSpot(
                        id = "5",
                        name = "九寨沟",
                        description = "世界自然遗产，以翠海、叠瀑、彩林、雪峰、藏情闻名",
                        imageUrl = "",
                        location = "四川省阿坝州",
                        rating = 4.9f,
                        isFavorite = false
                    ),
                    ScenicSpot(
                        id = "6",
                        name = "张家界",
                        description = "世界地质公园，以独特的石英砂岩峰林地貌著称",
                        imageUrl = "",
                        location = "湖南省张家界市",
                        rating = 4.7f,
                        isFavorite = false
                    ),
                    ScenicSpot(
                        id = "7",
                        name = "丽江古城",
                        description = "世界文化遗产，保存完好的少数民族古城",
                        imageUrl = "",
                        location = "云南省丽江市",
                        rating = 4.6f,
                        isFavorite = false
                    ),
                    ScenicSpot(
                        id = "8",
                        name = "桂林山水",
                        description = "甲天下的山水风光，以漓江风光和喀斯特地貌为代表",
                        imageUrl = "",
                        location = "广西壮族自治区桂林市",
                        rating = 4.8f,
                        isFavorite = false
                    )
                )

                _uiState.update {
                    it.copy(
                        data = mockData,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    /**
     * 切换收藏状态
     */
    fun toggleFavorite(spot: ScenicSpot) {
        // 更新收藏管理器
        FavoritesManager.toggleFavorite(spot)

        // 更新本地列表显示
        val updatedList = _uiState.value.data.map {
            if (it.id == spot.id) {
                it.copy(isFavorite = FavoritesManager.isFavorite(spot.id))
            } else {
                it
            }
        }
        _uiState.update { it.copy(data = updatedList) }
    }

    /**
     * 刷新数据
     */
    fun refresh() {
        loadAttractions()
    }
}

/**
 * 首页UI状态
 */
data class HomeUiState(
    val data: List<ScenicSpot> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)