package com.dicoding.githubuserapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.remote.response.ItemsItem
import com.dicoding.githubuserapp.databinding.ActivityMainBinding
import com.dicoding.githubuserapp.ui.settings.SettingPreferences
import com.dicoding.githubuserapp.ui.settings.SettingsActivity
import com.dicoding.githubuserapp.ui.settings.SettingsViewModel
import com.dicoding.githubuserapp.ui.settings.ViewModelFactory
import com.dicoding.githubuserapp.ui.settings.dataStore

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        addRecyclerView()
        observeThemeSettings()
        observeUserData()
        searchBar()

        binding?.searchBar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuFav -> {
                    startActivity(Intent(this, FavoriteUsersActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.user_menu, menu)
        return true
    }


    private fun addRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding?.rvUsers?.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvUsers?.addItemDecoration(itemDecoration)
    }

    private fun observeThemeSettings() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            val nightMode =
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    private fun observeUserData() {
        mainViewModel.usersGithub.observe(this) { userGithub ->
            setUsersData(userGithub)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun searchBar() {
        binding?.run {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.toString()
                if (query.isEmpty()) {
                    mainViewModel.searchUsersGithub("huda")
                    searchView.hide()
                    searchBar.text = ""
                } else {
                    searchBar.text = query
                    searchView.hide()
                    mainViewModel.searchUsersGithub(query)
                }
                false
            }
        }
    }

    private fun setUsersData(userslist: List<ItemsItem>) {
        val adapter = UsersGithubAdapter()
        adapter.submitList(userslist)
        binding?.rvUsers?.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.loadingBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
