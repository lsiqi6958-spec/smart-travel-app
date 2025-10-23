package com.zhiyouyunjing.app.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.zhiyouyunjing.app.databinding.ActivityLoginBinding

/**
 * 登录/注册Activity
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = LoginPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // 绑定TabLayout和ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "登录"
                1 -> "注册"
                else -> ""
            }
        }.attach()
    }

    /**
     * ViewPager2适配器
     */
    private class LoginPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> LoginTabFragment()
                1 -> RegisterTabFragment()
                else -> LoginTabFragment()
            }
        }
    }
}