package com.example.kotlinfirstless

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    suspend fun insert(favorite: FavoriteItem)

    @Query("SELECT * FROM favorites WHERE userId = :userId")
    suspend fun getFavoritesByUser(userId: Int): List<FavoriteItem>

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavorite(id: Int)

    @Query("SELECT * FROM favorites WHERE userId = :userId AND title = :title LIMIT 1")
    suspend fun getByUserAndTitle(userId: Int, title: String): FavoriteItem?

    // 🔹 Новый метод — проверка по названию
    @Query("SELECT * FROM favorites WHERE title = :title LIMIT 1")
    suspend fun getByTitle(title: String): FavoriteItem?
}
