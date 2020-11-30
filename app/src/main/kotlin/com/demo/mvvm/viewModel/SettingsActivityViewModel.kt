package com.demo.mvvm.viewModel

import androidx.databinding.ObservableInt
import com.demo.demo.R
import com.demo.mvvm.view.activity.BaseActivityViewModel
import com.demo.utils.SharedPreferencesHelper
import javax.inject.Inject

class SettingsActivityViewModel @Inject constructor() : BaseActivityViewModel() {
    val title: ObservableInt = ObservableInt(R.string.turn_off_passcode)

    init {
        setTitle()
    }

    fun setPassCodeState() {
        SharedPreferencesHelper.passCodeOn = !SharedPreferencesHelper.passCodeOn
        setTitle()
    }

    private fun setTitle() {
        if (SharedPreferencesHelper.passCodeOn) {
            title.set(R.string.turn_off_passcode)
        } else {
            title.set(R.string.turn_on_passcode)
        }
    }

}
