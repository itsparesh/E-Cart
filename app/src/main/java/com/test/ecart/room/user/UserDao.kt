package com.test.ecart.room.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: User?)

    @Query("SELECT * FROM User WHERE userName = :userName")
    fun getUser(userName: String?): User?

    @Query("SELECT EXISTS (SELECT 1 FROM User WHERE userName = :userName AND password = :password)")
    fun exists(userName: String?, password: String?): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM User WHERE userName = :userName)")
    fun userNameExists(userName: String?): Boolean

    companion object {
        const val USER = 0
        const val ADMIN = 1
    }

}