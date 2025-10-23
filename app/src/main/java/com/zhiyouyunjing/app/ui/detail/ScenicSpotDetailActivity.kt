package com.zhiyouyunjing.app.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.zhiyouyunjing.app.R
import com.zhiyouyunjing.app.data.FavoritesManager
import com.zhiyouyunjing.app.databinding.ActivityScenicSpotDetailBinding
import com.zhiyouyunjing.app.model.ScenicSpot
import kotlinx.coroutines.launch

/**
 * 景点详情Activity
 */
class ScenicSpotDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScenicSpotDetailBinding
    private val viewModel: ScenicSpotDetailViewModel by viewModels()

    private lateinit var scenicSpot: ScenicSpot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScenicSpotDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 获取传递过来的景点数据
        scenicSpot = intent.getParcelableExtra(EXTRA_SCENIC_SPOT) ?: run {
            finish()
            return
        }

        setupToolbar()
        setupUI()
        setupClickListeners()
        observeFavoriteState()
    }

    /**
     * 设置Toolbar
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbar.title = scenicSpot.name
    }

    /**
     * 设置UI显示
     */
    private fun setupUI() {
        binding.apply {
            // 设置景点信息
            tvName.text = scenicSpot.name
            tvLocation.text = scenicSpot.location
            tvRating.text = scenicSpot.rating.toString()
            tvDescription.text = scenicSpot.description

            // 设置占位图（后续可以用真实图片）
            imgScenicSpot.setImageResource(R.drawable.ic_home_24)
            imgScenicSpot.setBackgroundColor(getColor(R.color.md_theme_primary_container))

            // 更新收藏按钮状态
            updateFavoriteButton(FavoritesManager.isFavorite(scenicSpot.id))
        }
    }

    /**
     * 设置点击事件
     */
    private fun setupClickListeners() {
        // 收藏按钮
        binding.btnFavorite.setOnClickListener {
            toggleFavorite()
        }

        // 分享按钮
        binding.btnShare.setOnClickListener {
            shareScenic()
        }
    }

    /**
     * 观察收藏状态变化
     */
    private fun observeFavoriteState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                FavoritesManager.favorites.collect { favorites ->
                    val isFavorite = favorites.any { it.id == scenicSpot.id }
                    updateFavoriteButton(isFavorite)
                }
            }
        }
    }

    /**
     * 更新收藏按钮状态
     */
    private fun updateFavoriteButton(isFavorite: Boolean) {
        binding.btnFavorite.apply {
            text = if (isFavorite) "已收藏" else "收藏"
            setIconResource(
                if (isFavorite) R.drawable.ic_favorite_24
                else R.drawable.ic_favorite_24
            )
            setIconTintResource(
                if (isFavorite) R.color.md_theme_primary
                else R.color.md_theme_on_surface_variant
            )
        }
    }

    /**
     * 切换收藏状态
     */
    private fun toggleFavorite() {
        val isFavorite = FavoritesManager.isFavorite(scenicSpot.id)

        if (isFavorite) {
            FavoritesManager.removeFavorite(scenicSpot.id)
            Snackbar.make(binding.root, "已取消收藏", Snackbar.LENGTH_SHORT).show()
        } else {
            FavoritesManager.addFavorite(scenicSpot)
            Snackbar.make(binding.root, "已添加到收藏", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * 分享景点
     */
    private fun shareScenic() {
        val shareText = """
            推荐一个好地方：${scenicSpot.name}
            
            位置：${scenicSpot.location}
            评分：⭐ ${scenicSpot.rating}
            
            ${scenicSpot.description}
            
            来自智游云境APP
        """.trimIndent()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        startActivity(Intent.createChooser(shareIntent, "分享到"))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        private const val EXTRA_SCENIC_SPOT = "extra_scenic_spot"

        /**
         * 启动景点详情页
         */
        fun start(context: Context, scenicSpot: ScenicSpot) {
            val intent = Intent(context, ScenicSpotDetailActivity::class.java).apply {
                putExtra(EXTRA_SCENIC_SPOT, scenicSpot)
            }
            context.startActivity(intent)
        }
    }
}