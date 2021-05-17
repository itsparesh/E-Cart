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

class ItemActivity : AppCompatActivity() {
    private var itemList: List<Item?>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        getItems()
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
            MainActivity.selected = java.util.ArrayList()
            item = itemList
        }
    }
}