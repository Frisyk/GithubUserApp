package com.dicoding.githubuserapp.data.remote.retrofit

import com.dicoding.githubuserapp.data.remote.response.ItemsItem
import com.dicoding.githubuserapp.data.remote.response.UserDetailsResponse
import com.dicoding.githubuserapp.data.remote.response.UsersGithubResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsers(@Query("q") username: String): Call<UsersGithubResponse>

    @GET("users/{username}")
    fun getUserDetails(@Path("username") username: String): Call<UserDetailsResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String?): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String?): Call<List<ItemsItem>>
}