package com.test.ecart.room.cart

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Cart::class], version = 1)
abstract class CartDB : RoomDatabase() {
    abstract val cartDao: CartDao?

    companion object {
        @Volatile
        private var INSTANCE: CartDB? = null

        fun getDatabase(context: Context): CartDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context,
                        CartDB::class.java,
                        "cart.db"
                ).fallbackToDestructiveMigration().
                build()
                INSTANCE = instance
                instance
            }
        }
    }
}