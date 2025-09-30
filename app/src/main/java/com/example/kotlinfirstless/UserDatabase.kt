package com.example.kotlinfirstless
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, FavoriteItem::class], version = 2)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile private var instance: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration() // при смене версии очищает базу
                    .build().also { instance = it }
            }
        }
    }
}

