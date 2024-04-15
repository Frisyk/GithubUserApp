package com.dicoding.githubuserapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GithubDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(github: Github)

    @Delete
    suspend fun delete(github: Github)

    @Query("SELECT * from github ORDER BY username ASC")
    fun getAllFavUsers(): LiveData<List<Github>>

    @Query("SELECT EXISTS(SELECT * from github where username = :username)")
    fun isFavoriteUser(username: String): LiveData<Boolean>
}