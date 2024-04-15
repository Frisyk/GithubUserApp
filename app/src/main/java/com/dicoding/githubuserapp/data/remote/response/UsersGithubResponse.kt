package com.dicoding.githubuserapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class UsersGithubResponse(

	@field:SerializedName("items")
	val items: List<ItemsItem>
)

data class ItemsItem(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    )
