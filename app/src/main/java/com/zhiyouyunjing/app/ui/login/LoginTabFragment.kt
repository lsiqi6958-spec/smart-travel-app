package com.zhiyouyunjing.app.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.zhiyouyunjing.app.R
import com.zhiyouyunjing.app.data.UserManager
import com.zhiyouyunjing.app.databinding.FragmentLoginTabBinding

/**
 * 登录Tab
 */
class LoginTabFragment : Fragment(R.layout.fragment_login_tab) {

    private var _binding: FragmentLoginTabBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginTabBinding.bind(view)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.tvForgotPassword.setOnClickListener {
            Snackbar.make(requireView(), "找回密码功能开发中", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun performLogin() {
        val username = binding.etUsername.text?.toString() ?: ""
        val password = binding.etPassword.text?.toString() ?: ""

        // 验证输入
        if (username.isEmpty()) {
            binding.tilUsername.error = "请输入用户名"
            return
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = "请输入密码"
            return
        }
        if (password.length < 6) {
            binding.tilPassword.error = "密码至少6位"
            return
        }

        // 清除错误提示
        binding.tilUsername.error = null
        binding.tilPassword.error = null

        // 执行登录
        val success = UserManager.login(username, password)
        if (success) {
            Snackbar.make(requireView(), "登录成功", Snackbar.LENGTH_SHORT).show()
            requireActivity().finish()
        } else {
            Snackbar.make(requireView(), "登录失败", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}