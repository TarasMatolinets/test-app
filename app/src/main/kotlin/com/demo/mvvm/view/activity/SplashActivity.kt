package com.demo.mvvm.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.demo.demo.R
import com.demo.demo.databinding.ActivitySplashBinding
import com.demo.mvvm.viewModel.SplashActivityViewModel

class SplashActivity : BaseActivity<SplashActivityViewModel>() {

    override fun init(): SplashActivityViewModel {
        val viewModel: SplashActivityViewModel by viewModels { viewModelFactory }
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)

        binding.viewModel = viewModel
    }
}