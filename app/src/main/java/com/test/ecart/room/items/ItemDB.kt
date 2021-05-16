package com.test.ecart.room.items

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = ItemDB.VERSION)
abstract class ItemDB : RoomDatabase() {
    abstract val itemDao: ItemDao?

    companion object {
        const val VERSION = 2
        @Volatile
        private var INSTANCE: ItemDB? = null

        fun getDatabase(context: Context): ItemDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context,
                        ItemDB::class.java,
                        "item.db"
                ).fallbackToDestructiveMigration().
                build()
                INSTANCE = instance
                instance
            }
        }
    }
}