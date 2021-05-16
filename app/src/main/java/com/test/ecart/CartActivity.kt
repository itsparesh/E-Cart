package com.test.ecart

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.ecart.room.cart.Cart
import com.test.ecart.room.cart.CartDB
import com.test.ecart.room.items.Item
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CartActivity : AppCompatActivity() {

    private var items: ArrayList<Item>? = null
    private var userName: String? = null
    private var mCartDb: CartDB? = null
    private var mPriceText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setupUI()
    }

    private fun setupUI() {
        supportActionBar?.title = "My Cart"
        items = this.intent.extras?.getSerializable("cart") as ArrayList<Item>?
        userName = this.intent.extras?.getString("userName")
        mPriceText = findViewById(R.id.price)
        mCartDb = CartDB.getDatabase(this)
        cartRV?.layoutManager = LinearLayoutManager(this)
        setAdapter()
        var price = 0
        for (i in items!!) {
            price += i.price * i.count
        }
        val str = "₹ $price"
        mPriceText?.text = str
    }

    private fun setAdapter() {
        val adapter: ItemsAdapter? = items?.let { ItemsAdapter(it) }
        cartRV?.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.done) {
            if (items?.size == 0) {
                Toast.makeText(this, "Add some items to checkout", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to checkout?")
                        .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int -> InsertCartItem().placeOder() }
                        .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.cancel() }
                        .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class InsertCartItem {

        fun placeOder() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    for (i in items!!) {
                        val c = Cart()
                        c.userName = userName!!
                        c.name = i.name
                        c.count = i.count
                        c.inventory = i.inventory
                        c.price = i.price
                        c.variant = i.variant
                        mCartDb?.cartDao?.insert(c)
                        runOnUiThread {
                            Toast.makeText(this@CartActivity, "Order Placed Successfully", Toast.LENGTH_SHORT).show()
                            items?.clear()
                            setAdapter()
                            MainActivity.selected = ArrayList()
                            finish()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private inner class ItemsAdapter(itemList: ArrayList<Item>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
        private var item: ArrayList<Item> = ArrayList()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val itm = item[position]
            holder.name.text = itm.name
            holder.price.text = itm.price.toString()
            holder.quantity.text = itm.variant
            holder.count.text = itm.count.toString()
            holder.add.setOnClickListener {
                itm.count = itm.count + 1
                holder.count.text = itm.count.toString()
            }
            holder.remove.setOnClickListener {
                if (itm.count > 0) itm.count = itm.count - 1
                if (itm.count == 0) {
                    item.removeAt(holder.absoluteAdapterPosition)
                    notifyDataSetChanged()
                } else holder.count.text = itm.count.toString()
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
}