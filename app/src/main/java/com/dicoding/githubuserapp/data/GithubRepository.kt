package com.dicoding.githubuserapp.data


import androidx.lifecycle.LiveData
import com.dicoding.githubuserapp.data.local.Github
import com.dicoding.githubuserapp.data.local.GithubDao
import com.dicoding.githubuserapp.data.remote.retrofit.ApiService


class GithubRepository private constructor(
    private val apiService: ApiService,
    private val githubDao: GithubDao,
) {
    fun getAllFavUsers(): LiveData<List<Github>> = githubDao.getAllFavUsers()
    suspend fun addFavUser(github: Github) {
       githubDao.insert(github)
    }
    suspend fun deleteFavUser(github: Github) {
        githubDao.delete(github)
    }
    fun checkFavByUsername(username: String): LiveData<Boolean> {
        return githubDao.isFavoriteUser(username)
    }

    companion object {
        @Volatile
        private var instance: GithubRepository? = null
        fun getInstance(
            apiService: ApiService,
            githubDao: GithubDao,
        ): GithubRepository =
            instance ?: synchronized(this) {
                instance ?: GithubRepository(apiService, githubDao)
            }.also { instance = it }
    }
}