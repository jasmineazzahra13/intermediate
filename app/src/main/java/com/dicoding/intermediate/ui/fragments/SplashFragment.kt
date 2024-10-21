package com.dicoding.intermediate.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dicoding.intermediate.R
import com.dicoding.intermediate.databinding.FragmentSplashBinding
import com.dicoding.intermediate.utils.PreferManager
import com.dicoding.intermediate.utils.constant.AnimationConstants.SPLASH_ANIMATION
import org.koin.android.ext.android.inject

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val pref: PreferManager by inject()

    private fun initLoading() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (pref.isLogin) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment2)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }, SPLASH_ANIMATION)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun initUI() {}

    override fun initProcess() {
        initLoading()
    }

    override fun initObservers() {}
}