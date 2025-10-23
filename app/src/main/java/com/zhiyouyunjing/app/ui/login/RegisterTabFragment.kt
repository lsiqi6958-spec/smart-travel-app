package com.zhiyouyunjing.app.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.zhiyouyunjing.app.R
import com.zhiyouyunjing.app.data.UserManager
import com.zhiyouyunjing.app.databinding.FragmentRegisterTabBinding

/**
 * 注册Tab
 */
class RegisterTabFragment : Fragment(R.layout.fragment_register_tab) {

    private var _binding: FragmentRegisterTabBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterTabBinding.bind(view)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            performRegister()
        }
    }

    private fun performRegister() {
        val username = binding.etUsername.text?.toString() ?: ""
        val email = binding.etEmail.text?.toString() ?: ""
        val password = binding.etPassword.text?.toString() ?: ""

        // 验证输入
        if (username.isEmpty()) {
            binding.tilUsername.error = "请输入用户名"
            return
        }
        if (email.isEmpty()) {
            binding.tilEmail.error = "请输入邮箱"
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "邮箱格式不正确"
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
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        // 执行注册
        val success = UserManager.register(username, password, email)
        if (success) {
            Snackbar.make(requireView(), "注册成功", Snackbar.LENGTH_SHORT).show()
            requireActivity().finish()
        } else {
            Snackbar.make(requireView(), "注册失败", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}