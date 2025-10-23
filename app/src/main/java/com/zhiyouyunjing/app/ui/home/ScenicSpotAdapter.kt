package com.zhiyouyunjing.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zhiyouyunjing.app.R
import com.zhiyouyunjing.app.databinding.ItemScenicSpotBinding
import com.zhiyouyunjing.app.model.ScenicSpot

/**
 * 景点列表适配器
 */
class ScenicSpotAdapter(
    private val onItemClick: (ScenicSpot) -> Unit,
    private val onFavoriteClick: (ScenicSpot) -> Unit
) : ListAdapter<ScenicSpot, ScenicSpotAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScenicSpotBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemScenicSpotBinding,
        private val onItemClick: (ScenicSpot) -> Unit,
        private val onFavoriteClick: (ScenicSpot) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(spot: ScenicSpot) {
            binding.apply {
                // 设置景点信息
                tvName.text = spot.name
                tvLocation.text = spot.location
                tvRating.text = "⭐ ${spot.rating}"
                tvDescription.text = spot.description

                // 设置占位图（暂时用图标代替真实图片）
                imgScenicSpot.setImageResource(R.drawable.ic_home_24)
                imgScenicSpot.setBackgroundColor(
                    binding.root.context.getColor(R.color.md_theme_primary_container)
                )

                // 设置收藏按钮状态
                btnFavorite.setImageResource(
                    if (spot.isFavorite) R.drawable.ic_favorite_24
                    else R.drawable.ic_favorite_24
                )
                btnFavorite.setColorFilter(
                    binding.root.context.getColor(
                        if (spot.isFavorite) R.color.md_theme_primary
                        else R.color.md_theme_on_surface_variant
                    )
                )

                // 点击事件
                root.setOnClickListener { onItemClick(spot) }
                btnFavorite.setOnClickListener { onFavoriteClick(spot) }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ScenicSpot>() {
        override fun areItemsTheSame(oldItem: ScenicSpot, newItem: ScenicSpot): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScenicSpot, newItem: ScenicSpot): Boolean {
            return oldItem == newItem
        }
    }
}