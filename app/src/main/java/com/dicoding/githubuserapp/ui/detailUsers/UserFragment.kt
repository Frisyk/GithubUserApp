package com.dicoding.githubuserapp.ui.detailUsers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.data.remote.response.ItemsItem
import com.dicoding.githubuserapp.databinding.FragmentUserBinding
import com.dicoding.githubuserapp.ui.UsersGithubAdapter

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding
    private val userDetailsViewModel by viewModels<UserDetailsViewModel>()
    private var position: Int = 0
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding?.rvFollow?.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding?.rvFollow?.addItemDecoration(itemDecoration)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        userDetailsViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        if (position == 1) {
            userDetailsViewModel.getFollowers(username)
            userDetailsViewModel.followers.observe(viewLifecycleOwner) { followers ->
                setUsersData(followers)
            }
        } else {
            userDetailsViewModel.getFollowing(username)
            userDetailsViewModel.following.observe(viewLifecycleOwner) { following ->
                setUsersData(following)
            }
        }
    }

    private fun setUsersData(followList: List<ItemsItem>) {
        val adapter = UsersGithubAdapter()
        adapter.submitList(followList)
        binding?.rvFollow?.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.loadingBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}