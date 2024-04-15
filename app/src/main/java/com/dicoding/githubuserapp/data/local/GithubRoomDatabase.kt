package com.dicoding.githubuserapp.data.local

import android.content.Context
import androidx.room.*

@Database(entities = [Github::class], version = 1, exportSchema = false)
abstract class GithubRoomDatabase : RoomDatabase() {
    abstract fun githubDao(): GithubDao

    companion object {
        @Volatile
        private var instance: GithubRoomDatabase? = null

        fun getInstance(context: Context): GithubRoomDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    GithubRoomDatabase::class.java, "users_fav.db"
                ).build()
            }
    }
}