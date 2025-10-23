package com.zhiyouyunjing.app.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.zhiyouyunjing.app.R
import com.zhiyouyunjing.app.databinding.FragmentProfileBinding

/**
 * 个人中心Fragment
 */
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        setupUI()
    }

    private fun setupUI() {
        // 用户名点击事件 - 将来实现登录功能
        binding.textUsername.setOnClickListener {
            Snackbar.make(
                requireView(),
                "登录功能开发中...",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}