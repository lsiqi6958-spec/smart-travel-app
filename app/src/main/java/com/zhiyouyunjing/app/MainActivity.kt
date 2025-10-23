package com.zhiyouyunjing.app

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.zhiyouyunjing.app.databinding.ActivityMainBinding

/**
 * 主Activity - 智游云境旅游APP
 * 采用单Activity + 多Fragment架构
 * 使用Navigation Component管理页面导航
 */
class MainActivity : AppCompatActivity() {

    // ViewBinding - 类型安全的视图访问
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化 ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置底部导航
        setupBottomNavigation()

        // 配置返回键处理
        setupBackPressHandler()
    }

    /**
     * 配置底部导航栏与 Navigation Component 的集成
     */
    private fun setupBottomNavigation() {
        // 获取 NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // 将底部导航与 NavController 绑定
        // 这将自动处理导航项的选中状态和页面跳转
        val bottomNav = binding.bottomNavigation
        bottomNav.setupWithNavController(navController)

        // 可选：监听导航事件
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 在这里可以根据不同页面做特殊处理
            when (destination.id) {
                R.id.navigation_home -> {
                    // 首页特殊处理（如果需要）
                }
                R.id.navigation_camera -> {
                    // 相机页特殊处理
                }
                R.id.navigation_favorites -> {
                    // 收藏页特殊处理
                }
                R.id.navigation_profile -> {
                    // 个人中心特殊处理
                }
            }
        }
    }

    /**
     * 配置系统返回键行为
     * 使用 OnBackPressedDispatcher（Android 推荐的新API）
     */
    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navHostFragment = supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController

                if (!navController.popBackStack()) {
                    finish()
                }
            }
        })
    }

    /**
     * 支持向上导航（用于 Toolbar 的返回按钮）
     */
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}