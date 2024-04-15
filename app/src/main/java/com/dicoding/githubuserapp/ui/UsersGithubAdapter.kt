package com.dicoding.githubuserapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.data.local.Github
import com.dicoding.githubuserapp.data.remote.response.ItemsItem
import com.dicoding.githubuserapp.databinding.UserListItemBinding
import com.dicoding.githubuserapp.ui.detailUsers.UserDetailsActivity

class UsersGithubAdapter : ListAdapter<ItemsItem, UsersGithubAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val users = getItem(position)
        val favUser = Github(
            users.login,
            users.avatarUrl
        )
        holder.bind(users)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, UserDetailsActivity::class.java)
            intentDetail.putExtra(UserDetailsActivity.EXTRA_USER, favUser)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(private val binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(users: ItemsItem){
            binding.tvUsername.text = users.login
            Glide.with(binding.root)
                .load(users.avatarUrl)
                .circleCrop()
                .into(binding.ivProfile)        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem.login == newItem.login
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}