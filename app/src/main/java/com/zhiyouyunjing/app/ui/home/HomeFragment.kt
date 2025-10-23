package com.zhiyouyunjing.app.ui.home

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
import com.zhiyouyunjing.app.databinding.FragmentHomeBinding
import com.zhiyouyunjing.app.model.ScenicSpot
import kotlinx.coroutines.launch

/**
 * 首页Fragment - 景点展示
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: ScenicSpotAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        setupRecyclerView()
        observeViewModel()
    }

    /**
     * 设置RecyclerView
     */
    private fun setupRecyclerView() {
        adapter = ScenicSpotAdapter(
            onItemClick = { spot ->
                // 点击景点卡片
                showMessage("点击了：${spot.name}")
                // TODO: 跳转到景点详情页
            },
            onFavoriteClick = { spot ->
                // 点击收藏按钮
                viewModel.toggleFavorite(spot)
                showMessage(
                    if (!spot.isFavorite) "已收藏 ${spot.name}"
                    else "已取消收藏 ${spot.name}"
                )
            }
        )

        binding.rvScenicSpots.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
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
    private fun updateUI(state: HomeUiState) {
        // 显示/隐藏加载进度
        binding.progressBar.isVisible = state.isLoading

        // 显示错误信息
        if (state.error != null) {
            showMessage("加载失败: ${state.error}")
        }

        // 更新列表数据
        if (state.data.isNotEmpty()) {
            adapter.submitList(state.data)
        }
    }

    /**
     * 显示提示消息
     */
    private fun showMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}