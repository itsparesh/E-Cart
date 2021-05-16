package com.test.ecart.room.cart

import androidx.room.*

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg cart: Cart?)

    @Query("SELECT * FROM Cart WHERE userName like :userName")
    fun getAllItems(userName: String?): List<Cart?>?

    @Query("SELECT EXISTS (SELECT 1 FROM Cart WHERE userName = :userName)")
    fun exists(userName: String?): Boolean

    @Query("UPDATE Cart SET userName = '' WHERE userName like :userName")
    fun cancelOrder(userName: String?)

}