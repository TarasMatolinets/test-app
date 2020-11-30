package com.demo.mvvm.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.manager.PassCodeManager
import com.demo.utils.EventObserver
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


abstract class BaseActivity<VM : BaseActivityViewModel> : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var passCodeManager: PassCodeManager

    protected lateinit var viewModel: VM

    protected abstract fun init(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = init()

        passCodeManager.showPassCodeScreen.observe(this, EventObserver {
            if (it) {
                startActivity(Intent(this, PassCodeActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
        })
        passCodeManager.appLock.observe(this, EventObserver {
            if (it) {
                launchLockActivity()
            }
        })
    }

    open fun launchLockActivity() {
        val intent = Intent(this, LockActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}

abstract class BaseActivityViewModel : ViewModel()