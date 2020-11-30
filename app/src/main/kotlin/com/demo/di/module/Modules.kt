package com.demo.di.module

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.di.ViewModelFactory
import com.demo.di.scope.ViewModelKey
import com.demo.mvvm.view.activity.*
import com.demo.mvvm.viewModel.*
import com.demo.manager.PassCodeManager
import com.demo.utils.WorkChecker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Provider
import javax.inject.Singleton

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [PassCodeActivityModule::class])
    abstract fun bindPassCodeActivity(): PassCodeActivity

    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [LockActivityModule::class])
    abstract fun bindLockActivity(): LockActivity

    @ContributesAndroidInjector(modules = [SettingsActivityModule::class])
    abstract fun bindSettingsActivity(): SettingsActivity
}

@Module
abstract class LockActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(LockActivityViewModel::class)
    abstract fun bindLockViewModelKey(viewModel: LockActivityViewModel): ViewModel
}

@Module
abstract class SplashActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(SplashActivityViewModel::class)
    abstract fun bindMainViewModelKey(viewModel: SplashActivityViewModel): ViewModel
}

@Module
abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainViewModelKey(viewModel: MainActivityViewModel): ViewModel

}

@Module
abstract class PassCodeActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(PassCodeActivityViewModel::class)
    abstract fun bindPassCodeViewModelKey(viewModel: PassCodeActivityViewModel): ViewModel
}

@Module
abstract class SettingsActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(SettingsActivityViewModel::class)
    abstract fun bindSettingsViewModelKey(viewModel: SettingsActivityViewModel): ViewModel
}

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun providePassCodeManager(application: Application) = PassCodeManager(application)

    @Singleton
    @Provides
    fun provideWorkChecker(application: Application) = WorkChecker(application)

    companion object {
        @Provides
        fun provideViewModelFactory(providers: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory =
            ViewModelFactory(providers)
    }
}

