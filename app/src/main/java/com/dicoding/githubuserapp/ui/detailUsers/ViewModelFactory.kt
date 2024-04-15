package com.dicoding.githubuserapp.ui.detailUsers

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuserapp.data.GithubRepository
import com.dicoding.githubuserapp.di.Injection

class ViewModelFactory private constructor(private val githubRepository: GithubRepository) :
        ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserFavoriteViewModel::class.java)) {
                    return UserFavoriteViewModel(githubRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }

            companion object {
                @Volatile
                private var instance: ViewModelFactory? = null
                fun getInstance(context: Context): ViewModelFactory =
                    instance ?: synchronized(this) {
                        instance ?: ViewModelFactory(Injection.provideRepository(context))
                    }.also { instance = it }
            }
        }