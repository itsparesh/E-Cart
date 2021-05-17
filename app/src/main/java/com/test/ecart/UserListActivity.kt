package com.test.ecart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.ecart.room.user.User
import com.test.ecart.room.user.UserDB
import com.test.ecart.room.user.UserDao
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListActivity : AppCompatActivity() {

    private var userList: List<User>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        userDB = UserDB.getDatabase(this)
        getUsers()
    }

    private fun getUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userList = userDB?.userDao?.getAllUsers()
                runOnUiThread {
                    val adapter = UserAdapter(userList as List<User>)
                    productsList?.layoutManager = LinearLayoutManager(this@UserListActivity)
                    productsList?.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private var userDB: UserDB? = null
    }

    private inner class UserAdapter(itemList: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
        private var item: List<User> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val itm = item[position]
            holder.name.text = itm.userName
            holder.price.text = if (itm.type == UserDao.ADMIN) getString(R.string.admin) else getString(R.string.user)
            holder.quantity.visibility = View.GONE
            holder.count.visibility = View.GONE
            holder.add.visibility = View.GONE
            holder.remove.visibility = View.GONE
            holder.itemView.setOnClickListener {
                val intent = Intent(this@UserListActivity, RegistrationActivity::class.java)
                intent.putExtra("userName", itm.userName)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return item.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: TextView = itemView.findViewById(R.id.from_name)
            val price: TextView = itemView.findViewById(R.id.price_text)
            val quantity: TextView = itemView.findViewById(R.id.weight_text)
            val count: TextView = itemView.findViewById(R.id.cart_product_quantity_tv)
            val add: ImageView = itemView.findViewById(R.id.cart_plus_img)
            val remove: ImageView = itemView.findViewById(R.id.cart_minus_img)
        }

        init {
            MainActivity.selected = java.util.ArrayList()
            item = itemList
        }
    }
}