package com.demo.mvvm.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.demo.demo.R
import com.demo.demo.databinding.ActivityLockBinding
import com.demo.utils.SharedPreferencesHelper
import javax.inject.Inject

class LockActivity : BaseActivity<LockActivityViewModel>() {

    override fun init(): LockActivityViewModel {
        val viewModel: LockActivityViewModel by viewModels { viewModelFactory }
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!SharedPreferencesHelper.lockApp) {
            finish()
            return
        }

        val binding =
            DataBindingUtil.setContentView<ActivityLockBinding>(this, R.layout.activity_lock)

        binding.viewModel = viewModel
    }
}

class LockActivityViewModel @Inject constructor() : BaseActivityViewModel()