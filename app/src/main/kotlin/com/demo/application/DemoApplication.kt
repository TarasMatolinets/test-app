package com.demo.application

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.demo.di.component.DaggerAppComponent
import com.demo.manager.PassCodeManager
import com.demo.utils.SharedPreferencesHelper
import com.demo.utils.WorkChecker
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class DemoApplication : DaggerApplication() {
    @Inject
    lateinit var passCodeManager: PassCodeManager

    @Inject
    lateinit var workChecker: WorkChecker

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SharedPreferencesHelper.init(this)
        MultiDex.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        appComponent.inject(this)

        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        workChecker.runWorkWatcher()
        ProcessLifecycleOwner.get().lifecycle.addObserver(passCodeManager)
    }
}