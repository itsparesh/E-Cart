package com.test.ecart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_admin_main.*

class AdminMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        setOnClickListener()
        adminNameTV?.text = intent?.getStringExtra("userName")
    }

    private fun setOnClickListener() {
        addUserBtnET?.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
        addItemBtnET?.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
        editUsersBtnET?.setOnClickListener {
            startActivity(Intent(this, UserListActivity::class.java))
        }
        editItemsBtnET?.setOnClickListener {
            startActivity(Intent(this, ItemActivity::class.java))
        }
    }
}