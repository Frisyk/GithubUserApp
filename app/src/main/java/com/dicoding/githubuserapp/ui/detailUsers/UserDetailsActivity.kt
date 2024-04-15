package com.dicoding.githubuserapp.ui.detailUsers

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.data.local.Github
import com.dicoding.githubuserapp.data.remote.response.UserDetailsResponse
import com.dicoding.githubuserapp.databinding.ActivityUserDetailsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityUserDetailsBinding? = null
    private val binding get() = _binding

    private val userDetailsViewModel by viewModels<UserDetailsViewModel>()
    private val userFavoriteViewModel by viewModels<UserFavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_USER, Github::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_USER)
        }

        userDetailsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        if (user != null) {
            userDetailsViewModel.getUserDetails(user.username)
        }

        userDetailsViewModel.userDetails.observe(this) {
            setDetails(it)
        }

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        sectionsPagerAdapter.username = user?.username.toString()
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        user?.let {
            userFavoriteViewModel.isFavByUsername(it.username).observe(this) { isFav ->
                if (isFav) {
                    binding?.fabFav?.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_fav)
                    )
                } else {
                    binding?.fabFav?.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_fav_border)
                    )
                }

                binding?.fabFav?.setOnClickListener {
                    if (!isFav) {
                        userFavoriteViewModel.addFav(user)
                    } else {
                        userFavoriteViewModel.deleteFav(user)
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setDetails(user: UserDetailsResponse) {
        binding?.tvNickname?.text = user.login
        binding?.tvFullName?.text = user.name
        binding?.let {
            binding?.ivProfile?.let { it1 ->
                Glide.with(it.root)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .into(it1)
            }
        }
        binding?.follower?.text = resources.getString(R.string.followers, user.followers)
        binding?.following?.text = resources.getString(R.string.following, user.following)
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.loadingBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_USER = "extra_user"
    }
}