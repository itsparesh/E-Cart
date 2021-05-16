package com.test.ecart.room.items

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
open class Item: Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sNo")
    var sNo = 0
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "variant")
    var variant: String? = null
    @ColumnInfo(name = "price")
    var price = 0
    @ColumnInfo(name = "inventory")
    var inventory = 0
    @ColumnInfo(name = "count")
    var count = 0
}