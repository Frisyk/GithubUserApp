package com.dicoding.githubuserapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.data.remote.response.ItemsItem
import com.dicoding.githubuserapp.databinding.ActivityFavoriteUsersBinding
import com.dicoding.githubuserapp.ui.detailUsers.UserFavoriteViewModel
import com.dicoding.githubuserapp.ui.detailUsers.ViewModelFactory

class FavoriteUsersActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteUsersBinding? = null
    private val binding get() = _binding
    private val adapter = UsersGithubAdapter()

    private val userFavoriteViewModel by viewModels<UserFavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val layoutManager = LinearLayoutManager(this)
        binding?.rvFavUsers?.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvFavUsers?.addItemDecoration(itemDecoration)

        userFavoriteViewModel.getFav().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl.toString())
                items.add(item)
            }
            adapter.submitList(items)
            binding?.rvFavUsers?.adapter = adapter
        }

    }
}