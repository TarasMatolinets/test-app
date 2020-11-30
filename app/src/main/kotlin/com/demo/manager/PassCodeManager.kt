package com.demo.manager

import android.content.Context
import androidx.lifecycle.*
import com.demo.utils.Event
import com.demo.utils.SharedPreferencesHelper
import javax.inject.Inject

class PassCodeManager @Inject constructor(context: Context) : LifecycleObserver {

    private val appLockMutable = MutableLiveData<Event<Boolean>>()
    val appLock: LiveData<Event<Boolean>> = appLockMutable

    private val showPassCodeScreenMutable = MutableLiveData<Event<Boolean>>()
    val showPassCodeScreen: LiveData<Event<Boolean>> = showPassCodeScreenMutable

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startApp() {
        if (SharedPreferencesHelper.lockApp) {
            appLockMutable.value = Event(SharedPreferencesHelper.lockApp)
            return
        }

        if (SharedPreferencesHelper.passCode != null && SharedPreferencesHelper.passCodeOn
        ) {
            SharedPreferencesHelper.showPassCodeScreen = false
            showPassCodeScreenMutable.value = Event(true)
        } else {
            showPassCodeScreenMutable.value = Event(false)
        }
    }
}