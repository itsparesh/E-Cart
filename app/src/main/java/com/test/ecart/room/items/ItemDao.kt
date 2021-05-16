package com.test.ecart.room.items

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg item: Item?)

    @get:Query("SELECT * FROM Item")
    val allItems: List<Item?>?
}