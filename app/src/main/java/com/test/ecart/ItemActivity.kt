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
import com.test.ecart.room.items.Item
import com.test.ecart.room.items.ItemDB
import kotlinx.android.synthetic.main.activity_user_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ItemActivity : AppCompatActivity() {
    private var itemList: List<Item?>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                addInitialItems()
                getItems()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getItems() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                itemList = itemDB?.itemDao?.allItems
                runOnUiThread {
                    val adapter = UserAdapter(itemList as List<Item>)
                    productsList?.layoutManager = LinearLayoutManager(this@ItemActivity)
                    productsList?.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private var itemDB: ItemDB? = null
    }

    private inner class UserAdapter(itemList: List<Item>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
        private var item: List<Item> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val itm = item[position]
            holder.name.text = itm.name
            holder.price.text = itm.variant
            holder.quantity.visibility = View.GONE
            holder.count.visibility = View.GONE
            holder.add.visibility = View.GONE
            holder.remove.visibility = View.GONE
            holder.itemView.setOnClickListener {
                val intent = Intent(this@ItemActivity, AddItemActivity::class.java)
                intent.putExtra("itemName", itm.name)
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
            item = itemList
        }
    }

    private fun addInitialItems() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sdf = SimpleDateFormat("HH", Locale.US)
                val hours = Integer.valueOf(sdf.format(Date()))
                val item = Item()
                item.name = "T-shirt"
                if (hours in 13..22) item.price = 10 else item.price = 5
                item.inventory = 10
                item.variant = "Cloth"
                itemDB?.itemDao?.insert(item)
                val item1 = Item()
                item1.name = "Harry Potter"
                if (hours in 13..22) item1.price = 10 else item1.price = 5
                item1.inventory = 10
                item1.variant = "Book"
                itemDB?.itemDao?.insert(item1)
                val item2 = Item()
                item2.name = "Jeans"
                if (hours in 13..22) item2.price = 60 else item2.price = 50
                item2.inventory = 5
                item2.variant = "Cloth"
                itemDB?.itemDao?.insert(item2)
                val item3 = Item()
                item3.name = "Maths Book"
                if (hours in 13..22) item3.price = 90 else item3.price = 50
                item3.inventory = 5
                item3.variant = "Book"
                itemDB?.itemDao?.insert(item3)
                val item4 = Item()
                item4.name = "Narnia"
                if (hours in 13..22) item4.price = 20 else item4.price = 15
                item4.inventory = 10
                item4.variant = "Book"
                itemDB?.itemDao?.insert(item4)
                val item5 = Item()
                item5.name = "Shirt"
                if (hours in 13..22) item5.price = 30 else item5.price = 15
                item5.inventory = 3
                item5.variant = "Cloth"
                itemDB?.itemDao?.insert(item5)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}