package com.test.ecart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.test.ecart.room.user.User
import com.test.ecart.room.user.UserDB
import com.test.ecart.room.user.UserDao
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setOnClickListener()
        addInitialUsers()
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
}