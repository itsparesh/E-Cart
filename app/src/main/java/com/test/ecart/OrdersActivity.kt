package com.test.ecart

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.ecart.room.cart.Cart
import com.test.ecart.room.cart.CartDB
import kotlinx.android.synthetic.main.activity_orders.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class OrdersActivity : AppCompatActivity() {
    private var userName: String? = null
    private var mCartDB: CartDB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        supportActionBar?.title = "Previous Orders"
        userName = this.intent.extras?.getString("userName")
        mCartDB = CartDB.getDatabase(this)
        QueryOrders().getAllItems()
    }

    private inner class QueryOrders {
        private var items: List<Cart?>? = null
        fun getAllItems() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    items = mCartDB?.cartDao?.getAllItems(userName)
                    runOnUiThread {
                        ordersList?.layoutManager = LinearLayoutManager(this@OrdersActivity)
                        val adapter = ItemsAdapter(items as List<Cart>)
                        ordersList?.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun cancelOrder() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    mCartDB?.cartDao?.cancelOrder(userName)
                    runOnUiThread {
                        ordersList?.layoutManager = LinearLayoutManager(this@OrdersActivity)
                        val adapter = ItemsAdapter(ArrayList())
                        ordersList?.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private inner class ItemsAdapter(itemList: List<Cart>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
        private var item: List<Cart> = ArrayList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val itm = item[position]
            holder.name.text = itm.name
            val str = "₹ " + itm.price.toString()
            holder.price.text = str
            holder.quantity.text = itm.variant
            holder.count.text = itm.count.toString()
            holder.add.visibility = View.GONE
            holder.remove.visibility = View.GONE
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.order_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        val id = menuItem.itemId
        if (id == R.id.deleteIcon) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure to cancel order?")
                    .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int -> QueryOrders().cancelOrder() }
                    .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.cancel() }
                    .show()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}