package com.demo.di.component

import android.app.Application
import com.demo.application.DemoApplication
import com.demo.di.module.ActivityBuilder
import com.demo.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ActivityBuilder::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<DemoApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
