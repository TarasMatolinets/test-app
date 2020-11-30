package com.demo.mvvm.viewModel

import com.demo.mvvm.view.activity.BaseActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DELAY = 1000L

class SplashActivityViewModel @Inject constructor() : BaseActivityViewModel() {
    private val splashViewModelScope = CoroutineScope(Dispatchers.Main)

    init {
        splashViewModelScope.launch {
            delay(DELAY)
        }
    }
}
