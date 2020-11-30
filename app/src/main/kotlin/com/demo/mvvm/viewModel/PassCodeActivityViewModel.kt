package com.demo.mvvm.viewModel


import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.demo.R
import com.demo.mvvm.view.activity.BaseActivityViewModel
import com.demo.utils.Event
import com.demo.utils.SharedPreferencesHelper
import javax.inject.Inject


private const val PassCodeLength = 4
private const val MaxWrongPassCode = 2

class PassCodeActivityViewModel @Inject constructor() : BaseActivityViewModel() {

    var changePassCode = false
    private var wrongPassCodeCounter = 0
    private var passCodeState: PassCodeState = PassCodeState.Create
    private var tempPassCode: String = ""

    private var passCodeEnterMutable: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var passCodeEnter: LiveData<Event<Boolean>> = passCodeEnterMutable

    private var passCodeConfirmMutable: MutableLiveData<Event<Int>> = MutableLiveData()
    var passCodeConfirm: LiveData<Event<Int>> = passCodeConfirmMutable

    private var passCodeLimitMutable: MutableLiveData<Event<Unit>> = MutableLiveData()
    var passCodeLimit: LiveData<Event<Unit>> = passCodeLimitMutable

    private var passCodeWrongMutable: MutableLiveData<Event<Unit>> = MutableLiveData()
    var passCodeWrong: LiveData<Event<Unit>> = passCodeWrongMutable

    private var passCodeChangeMutable: MutableLiveData<Event<Unit>> = MutableLiveData()
    var passCodeChange: LiveData<Event<Unit>> = passCodeChangeMutable

    private var mutableAppLockedMutable: MutableLiveData<Event<Unit>> = MutableLiveData()
    var appLocked: LiveData<Event<Unit>> = mutableAppLockedMutable

    val title: ObservableInt = ObservableInt(R.string.create_passcode)

    init {
        if (SharedPreferencesHelper.passCode != null) {
            passCodeState = PassCodeState.Enter
            title.set(R.string.enter_passcode)
        } else {
            passCodeState = PassCodeState.Create
            title.set(R.string.create_passcode)
        }
    }

    fun handlePassCode(passCode: String) {
        if (passCode.length < PassCodeLength) {
            passCodeLimitMutable.value = Event(Unit)
            return
        }
    }

    private fun handleEnterPassCode(passCode: String) {
        when {
            passCode == SharedPreferencesHelper.passCode -> {
                if (changePassCode) {
                    tempPassCode = ""
                    title.set(R.string.create_passcode)
                    passCodeState = PassCodeState.Create
                    passCodeChangeMutable.value = Event(Unit)
                } else {
                    passCodeEnterMutable.value = Event(true)
                    SharedPreferencesHelper.lockApp = false
                }
            }
            wrongPassCodeCounter < MaxWrongPassCode -> {
                wrongPassCodeCounter++
                passCodeWrongMutable.value = Event(Unit)
            }
            else -> {
                wrongPassCodeCounter = 0
                SharedPreferencesHelper.lockApp = true
                mutableAppLockedMutable.value = Event(Unit)
            }
        }
    }

    fun handleFinishTypePassCode(text: String) {
        if (passCodeState == PassCodeState.Create) {
            tempPassCode = text
            title.set(R.string.confirm_passcode)
            passCodeState = PassCodeState.Confirm
        } else if (passCodeState == PassCodeState.Confirm && text == tempPassCode) {
            SharedPreferencesHelper.passCode = text
            val res = if (changePassCode) {
                R.string.confirm_passcode_changed
            } else {
                R.string.confirm_passcode_created
            }
            passCodeConfirmMutable.value = Event(res)
        } else if (passCodeState == PassCodeState.Enter) {
            handleEnterPassCode(text)
        } else {
            passCodeWrongMutable.value = Event(Unit)
        }
    }
}

enum class PassCodeState {
    Create, Enter, Confirm
}
