package com.dicoding.githubuserapp.di

import android.content.Context
import com.dicoding.githubuserapp.data.GithubRepository
import com.dicoding.githubuserapp.data.local.GithubRoomDatabase
import com.dicoding.githubuserapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): GithubRepository {
        val apiService = ApiConfig.getApiService()
        val database = GithubRoomDatabase.getInstance(context)
        val dao = database.githubDao()
        return GithubRepository.getInstance(apiService, dao)
    }
}
