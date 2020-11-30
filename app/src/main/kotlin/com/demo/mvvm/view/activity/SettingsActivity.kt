package com.demo.mvvm.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.demo.demo.R
import com.demo.demo.databinding.ActivitySettingsBinding
import com.demo.mvvm.viewModel.SettingsActivityViewModel

class SettingsActivity : BaseActivity<SettingsActivityViewModel>() {

    override fun init(): SettingsActivityViewModel {
        val viewModel: SettingsActivityViewModel by viewModels { viewModelFactory }
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivitySettingsBinding>(
            this,
            R.layout.activity_settings
        )
        binding.viewModel = viewModel

        setSupportActionBar(binding.settingsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.tvEditPassCode.setOnClickListener {
            val intent = Intent(this, PassCodeActivity::class.java)
            intent.putExtras(PassCodeActivity.getBundle(true))
            startActivity(intent)
        }
        binding.tvTurnOffPassCode.setOnClickListener {
            viewModel.setPassCodeState()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}