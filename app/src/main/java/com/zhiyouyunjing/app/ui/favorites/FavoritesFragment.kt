package com.zhiyouyunjing.app.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zhiyouyunjing.app.R
import com.zhiyouyunjing.app.databinding.FragmentFavoritesBinding
import com.zhiyouyunjing.app.model.ScenicSpot
import com.zhiyouyunjing.app.ui.home.ScenicSpotAdapter
import kotlinx.coroutines.launch

/**
 * 收藏Fragment
 */
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var adapter: ScenicSpotAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)

        setupRecyclerView()
        observeViewModel()
    }

    /**
     * 设置RecyclerView
     */
    private fun setupRecyclerView() {
        adapter = ScenicSpotAdapter(
            onItemClick = { spot ->
                showMessage("查看：${spot.name}")
                // TODO: 跳转到景点详情
            },
            onFavoriteClick = { spot ->
                viewModel.removeFavorite(spot)
                showMessage("已取消收藏 ${spot.name}")
            }
        )

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoritesFragment.adapter
            setHasFixedSize(true)
        }
    }

    /**
     * 观察ViewModel数据
     */
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUI(state)
                }
            }
        }
    }

    /**
     * 更新UI
     */
    private fun updateUI(state: FavoritesUiState) {
        if (state.favorites.isEmpty()) {
            // 显示空状态
            binding.rvFavorites.isVisible = false
            binding.emptyView.isVisible = true
        } else {
            // 显示列表
            binding.rvFavorites.isVisible = true
            binding.emptyView.isVisible = false
            adapter.submitList(state.favorites)
        }
    }

    /**
     * 显示提示
     */
    private fun showMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}