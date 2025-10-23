package com.zhiyouyunjing.app.ui.camera

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.zhiyouyunjing.app.R
import com.zhiyouyunjing.app.databinding.FragmentCameraBinding

/**
 * 相机Fragment - AI景点识别
 */
class CameraFragment : Fragment(R.layout.fragment_camera) {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCameraBinding.bind(view)

        setupClickListeners()
    }

    /**
     * 设置点击事件监听
     */
    private fun setupClickListeners() {
        binding.btnTakePhoto.setOnClickListener {
            // 这里将来实现相机功能
            Snackbar.make(
                requireView(),  // ✅ 正确
                "相机功能开发中...",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}