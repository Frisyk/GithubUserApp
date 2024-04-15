package com.dicoding.githubuserapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.data.remote.response.ItemsItem
import com.dicoding.githubuserapp.data.remote.response.UsersGithubResponse
import com.dicoding.githubuserapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _usersGithub = MutableLiveData<List<ItemsItem>>()
    val usersGithub: LiveData<List<ItemsItem>> = _usersGithub

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainActivity"
    }

    init {
        findUsersGithub()
    }

    private fun findUsersGithub(user: String = "Huda") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(user)
        client.enqueue(object : Callback<UsersGithubResponse> {
            override fun onResponse(
                call: Call<UsersGithubResponse>,
                response: Response<UsersGithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _usersGithub.value = response.body()?.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UsersGithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun searchUsersGithub(input: String) {
        findUsersGithub(input)
    }
}