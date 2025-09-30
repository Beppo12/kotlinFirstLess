package com.example.kotlinfirstless

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUser(username: String): User?

    @Query("SELECT * FROM favorites WHERE userId = :userId AND title = :title LIMIT 1")
    suspend fun getByUserAndTitle(userId: Int, title: String): FavoriteItem?
}