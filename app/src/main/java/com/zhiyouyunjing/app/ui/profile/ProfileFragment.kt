package com.zhiyouyunjing.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.zhiyouyunjing.app.R
import com.zhiyouyunjing.app.data.UserManager
import com.zhiyouyunjing.app.databinding.FragmentProfileBinding
import com.zhiyouyunjing.app.ui.login.LoginActivity
import com.zhiyouyunjing.app.ui.settings.SettingsActivity
import kotlinx.coroutines.launch

/**
 * 个人中心Fragment
 */
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // ✅ 注册Activity Result Launcher
    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            // 设置已修改，需要刷新MainActivity
            activity?.recreate()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        setupUI()
        observeUserState()
    }

    private fun setupUI() {
        // 用户信息卡片点击
        binding.cardUserInfo.setOnClickListener {
            if (!UserManager.isLoggedIn.value) {
                // 跳转到登录页面
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }

        // 退出登录按钮（初始隐藏）
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        // 老年模式 - 跳转到设置页面
        binding.itemElderMode.setOnClickListener {
            // ✅ 使用settingsLauncher启动
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            settingsLauncher.launch(intent)
        }

        // 应用设置 - 跳转到设置页面
        binding.itemSettings.setOnClickListener {
            // ✅ 使用settingsLauncher启动
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            settingsLauncher.launch(intent)
        }
    }

    /**
     * 观察用户登录状态
     */
    private fun observeUserState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                UserManager.currentUser.collect { user ->
                    updateUI(user)
                }
            }
        }
    }

    /**
     * 更新UI显示
     */
    private fun updateUI(user: com.zhiyouyunjing.app.model.User?) {
        if (user != null) {
            // 已登录状态
            binding.textUsername.text = user.username
            binding.textUserHint.text = user.email
            binding.btnLogout.isVisible = true
        } else {
            // 未登录状态
            binding.textUsername.text = "游客"
            binding.textUserHint.text = "点击登录"
            binding.btnLogout.isVisible = false
        }
    }

    /**
     * 显示退出登录确认对话框
     */
    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("退出登录")
            .setMessage("确定要退出登录吗？")
            .setPositiveButton("确定") { _, _ ->
                UserManager.logout()
                Snackbar.make(requireView(), "已退出登录", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}