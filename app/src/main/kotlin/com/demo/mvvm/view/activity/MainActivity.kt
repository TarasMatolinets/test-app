package com.demo.mvvm.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.demo.demo.R
import com.demo.demo.databinding.ActivityMainBinding
import com.demo.mvvm.view.fragment.ShapeType
import com.demo.mvvm.view.fragment.ShapeFragment
import com.demo.mvvm.viewModel.MainActivityViewModel
import com.demo.utils.SharedPreferencesHelper


private const val NUM_PAGES = 3

private const val SQUARE = 0
private const val TRIANGLE = 1

class MainActivity : BaseActivity<MainActivityViewModel>() {

    override fun init(): MainActivityViewModel {
        val viewModel: MainActivityViewModel by viewModels { viewModelFactory }
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.viewModel = viewModel
        setSupportActionBar(binding.appToolbar)

        binding.shapePager.adapter = ShapePagerAdapter(supportFragmentManager)
        binding.tabTitle.setupWithViewPager(binding.shapePager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuSettings) {
            if (SharedPreferencesHelper.passCode != null) {
                startActivity(Intent(this, SettingsActivity::class.java))
            } else {
                startActivity(Intent(this, PassCodeActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private class ShapePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment = ShapeFragment(getShapeType(position))

        override fun getPageTitle(position: Int): CharSequence? {
            return getShapeType(position).name
        }

        private fun getShapeType(position: Int) = when (position) {
            SQUARE -> ShapeType.Square
            TRIANGLE -> ShapeType.Triangle
            else -> ShapeType.Hexagon
        }
    }
}