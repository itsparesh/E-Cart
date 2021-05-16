package com.test.ecart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.test.ecart.room.user.User
import com.test.ecart.room.user.UserDB
import com.test.ecart.room.user.UserDao
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        registerButtonET?.setOnClickListener {
            val userName = userNameET?.editableText.toString()
            val password = passwordET?.editableText.toString()
            val confirmPassword = confirmPasswordET?.editableText.toString()
            if (userName.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()) {
                if (password == confirmPassword) {
                    checkCredentials(userName, password)
                } else {
                    runOnUiThread {
                        Toast.makeText(this@RegistrationActivity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@RegistrationActivity, "Please enter all details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addUser(userName: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userDB = UserDB.getDatabase(this@RegistrationActivity)
                val user = User()
                user.userName = userName
                user.password = password
                if (adminSwitch?.isChecked == true) {
                    user.type = UserDao.ADMIN
                } else {
                    user.type = UserDao.USER
                }
                userDB.userDao?.insert(user)
                runOnUiThread {
                    Toast.makeText(this@RegistrationActivity, "User Registered", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@RegistrationActivity, "User Registration Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkCredentials(userName: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val userDB = UserDB.getDatabase(this@RegistrationActivity)
            val result = userDB.userDao?.userNameExists(userName) == true
            if (!result) {
                addUser(userName, password)
            } else {
                runOnUiThread {
                    Toast.makeText(this@RegistrationActivity, "User already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}