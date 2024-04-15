package com.dicoding.githubuserapp.ui.detailUsers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuserapp.data.GithubRepository
import com.dicoding.githubuserapp.data.local.Github
import kotlinx.coroutines.launch

class UserFavoriteViewModel(private val githubRepository: GithubRepository) : ViewModel() {

    fun getFav() = githubRepository.getAllFavUsers()

    fun addFav(github: Github) {
        viewModelScope.launch {
            githubRepository.addFavUser(github)
        }
    }

    fun isFavByUsername(username: String): LiveData<Boolean> {
       return githubRepository.checkFavByUsername(username)
    }

    fun deleteFav(github: Github) {
        viewModelScope.launch {
            githubRepository.deleteFavUser(github)
        }
    }
}