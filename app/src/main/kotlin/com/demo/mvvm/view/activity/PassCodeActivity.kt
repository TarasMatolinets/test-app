package com.demo.mvvm.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.databinding.DataBindingUtil
import com.demo.demo.R
import com.demo.demo.databinding.ActivityPasscodeBinding
import com.demo.mvvm.view.CircleParams
import com.demo.mvvm.view.DotTextField
import com.demo.mvvm.view.LineParams
import com.demo.mvvm.viewModel.PassCodeActivityViewModel
import com.demo.utils.EventObserver

class PassCodeActivity : BaseActivity<PassCodeActivityViewModel>() {

    override fun init(): PassCodeActivityViewModel {
        val viewModel: PassCodeActivityViewModel by viewModels { viewModelFactory }
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPasscodeBinding>(
            this,
            R.layout.activity_passcode
        )

        binding.viewModel = viewModel

        viewModel.changePassCode = intent.getBooleanExtra(CHANGE_PASSCODE, false)

        binding.composeDotView.setContent {
            val passCode = remember { mutableStateOf(TextFieldValue("")) }

            DotTextField(
                passCode,
                circleParams = CircleParams(Color.White, 15f),
                lineParams = LineParams(Color.Blue, Offset(10f, 10f), Offset(20f, 20f), 5f),
                shapeCount = 4,
                onImeActionPerformed = { _, _ -> viewModel.handlePassCode(passCode.value.text) },
                onValueChange = { update ->
                    if (update.text.length == 4) {
                        passCode.value = TextFieldValue("")
                        return@DotTextField
                    } else {
                        passCode.value = update
                    }
                },
                onFinishTypeListener = {
                    viewModel.handleFinishTypePassCode(it.text)
                }
            )
        }
        handleViewModels()
    }

    private fun handleViewModels() {
        viewModel.passCodeEnter.observe(this, EventObserver {
            if (it) {
                Toast.makeText(this, R.string.enter_sucess_passcode, Toast.LENGTH_SHORT).show()
                if (isTaskRoot) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    finish()
                }
            }
        })

        viewModel.passCodeChange.observe(this, EventObserver {
        })

        viewModel.passCodeConfirm.observe(this, EventObserver {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            finish()
        })
        viewModel.passCodeLimit.observe(this, EventObserver {
            Toast.makeText(this, R.string.pass_code_limit_message, Toast.LENGTH_SHORT).show()
        })

        viewModel.passCodeWrong.observe(this, EventObserver {
            Toast.makeText(this, R.string.wrong_passcode, Toast.LENGTH_SHORT).show()
        })

        viewModel.appLocked.observe(this, EventObserver {
            launchLockActivity()
        })
    }

    companion object {
        private const val CHANGE_PASSCODE = "CHANGE_PASSCODE_EXTRA"

        fun getBundle(changePassCode: Boolean) = Bundle().apply {
            putBoolean(CHANGE_PASSCODE, changePassCode)
        }
    }
}