package com.test.ecart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.test.ecart.room.items.Item
import com.test.ecart.room.items.ItemDB
import com.test.ecart.room.user.User
import com.test.ecart.room.user.UserDB
import com.test.ecart.room.user.UserDao
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setOnClickListener()
        addInitialUsers()
        addInitialItems()
    }

    private fun setOnClickListener() {
        loginButtonET?.setOnClickListener {
            val userName = userNameET?.editableText.toString()
            val password = passwordET?.editableText.toString()
            if (userName.isNotEmpty() && password.isNotEmpty()) {
                Log.d("LoginActivity", userName)
                checkCredentials(userName, password)
            } else {
                Toast.makeText(this@LoginActivity, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }

        addAccountButtonET?.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }
    }

    private fun checkCredentials(userName: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = UserDB.getDatabase(this@LoginActivity)
            val result = db.userDao?.exists(userName, password) == true
            val tempUser = db.userDao?.getUser(userName)
            runOnUiThread {
                if (tempUser?.type == UserDao.ADMIN) {
                    if (result) {
                        val intent = Intent(this@LoginActivity, AdminMainActivity::class.java)
                        intent.putExtra("userName", userName)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid details", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (result) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("userName", userName)
                        intent.putExtra("password", password)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun addInitialUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = UserDB.getDatabase(this@LoginActivity)
            val result = db.userDao?.userNameExists("testuser1") == true
            if (!result) {
                val user = User()
                user.userName = "testuser1"
                user.password = "12345"
                db.userDao?.insert(user)
            }

            val result2 = db.userDao?.userNameExists("admin2") == true
            if (!result2) {
                val user = User()
                user.userName = "admin2"
                user.password = "12345"
                user.type = UserDao.ADMIN
                db.userDao?.insert(user)
            }
        }
    }



    private fun addInitialItems() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val mItemDB = ItemDB.getDatabase(this@LoginActivity)
                val sdf = SimpleDateFormat("HH", Locale.US)
                val hours = Integer.valueOf(sdf.format(Date()))
                val item = Item()
                item.name = "T-shirt"
                if (hours in 13..22) item.price = 10 else item.price = 5
                item.inventory = 10
                item.variant = "Cloth"
                mItemDB.itemDao?.insert(item)
                val item1 = Item()
                item1.name = "Harry Potter"
                if (hours in 13..22) item1.price = 10 else item1.price = 5
                item1.inventory = 10
                item1.variant = "Book"
                mItemDB.itemDao?.insert(item1)
                val item2 = Item()
                item2.name = "Jeans"
                if (hours in 13..22) item2.price = 60 else item2.price = 50
                item2.inventory = 5
                item2.variant = "Cloth"
                mItemDB.itemDao?.insert(item2)
                val item3 = Item()
                item3.name = "Maths Book"
                if (hours in 13..22) item3.price = 90 else item3.price = 50
                item3.inventory = 5
                item3.variant = "Book"
                mItemDB.itemDao?.insert(item3)
                val item4 = Item()
                item4.name = "Narnia"
                if (hours in 13..22) item4.price = 20 else item4.price = 15
                item4.inventory = 10
                item4.variant = "Book"
                mItemDB.itemDao?.insert(item4)
                val item5 = Item()
                item5.name = "Shirt"
                if (hours in 13..22) item5.price = 30 else item5.price = 15
                item5.inventory = 3
                item5.variant = "Cloth"
                mItemDB.itemDao?.insert(item5)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}