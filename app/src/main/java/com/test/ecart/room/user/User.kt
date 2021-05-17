package com.test.ecart.room.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class User: Serializable {
    @PrimaryKey
    @ColumnInfo(name = "userName")
    var userName: String = ""
    @ColumnInfo(name = "password")
    var password: String? = null
    @ColumnInfo(name = "type")
    var type: Int = UserDao.USER
    @ColumnInfo(name = "sNo")
    var sNo = 0
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "variant")
    var variant: String? = ""
    @ColumnInfo(name = "price")
    var price = 0
    @ColumnInfo(name = "inventory")
    var inventory = 0
    @ColumnInfo(name = "count")
    var count = 0
}