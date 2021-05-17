package com.test.ecart

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.test.ecart.room.items.Item
import com.test.ecart.room.items.ItemDB
import com.test.ecart.room.items.ItemDB.Companion.getDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var mList: List<Item?>? = ArrayList()
    private var userName: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getIntentData()
        setupUI()
    }

    private fun getIntentData() {
        val bundle = intent?.extras
        userName = bundle?.getString("userName")
        password = bundle?.getString("password")
    }

    private fun setupUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        navigationView?.setNavigationItemSelectedListener(this)
        val header = navigationView?.getHeaderView(0)
        val navName = header?.findViewById<TextView>(R.id.userName)
        val userType = header?.findViewById<TextView>(R.id.userType)
        navName?.text = userName
        userType?.text = userName
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.syncState()
        mItemDB = getDatabase(this)

        // Database population should occur only once that is when the app is opened first
        //val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        /*if (!preferences.getBoolean("firstTime", false)) {
            DatabaseAsync().insertData()
            val editor = preferences.edit()
            editor.putBoolean("firstTime", true)
            editor.apply()
        }*/
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseAsync().insertData()
            QueryDB().getData()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.shopping) {
            val intent = Intent(this@MainActivity, OrdersActivity::class.java)
            intent.putExtra("userName", userName)
            startActivity(intent)
        }
        return false
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawerLayout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            drawer.closeDrawer(GravityCompat.START)
        }
        finish()
    }

    /**
     * Database seeding
     * Ideally done in a backend or a separate app to manage the items
     */
    private class DatabaseAsync {
        fun insertData() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val sdf = SimpleDateFormat("HH", Locale.US)
                    val hours = Integer.valueOf(sdf.format(Date()))
                    val item = Item()
                    item.name = "T-shirt"
                    if (hours in 13..22) item.price = 10 else item.price = 5
                    item.inventory = 10
                    item.variant = "Cloth"
                    mItemDB?.itemDao?.insert(item)
                    val item1 = Item()
                    item1.name = "Harry Potter"
                    if (hours in 13..22) item1.price = 10 else item1.price = 5
                    item1.inventory = 10
                    item1.variant = "Book"
                    mItemDB?.itemDao?.insert(item1)
                    val item2 = Item()
                    item2.name = "Jeans"
                    if (hours in 13..22) item2.price = 60 else item2.price = 50
                    item2.inventory = 5
                    item2.variant = "Cloth"
                    mItemDB?.itemDao?.insert(item2)
                    val item3 = Item()
                    item3.name = "Maths Book"
                    if (hours in 13..22) item3.price = 90 else item3.price = 50
                    item3.inventory = 5
                    item3.variant = "Book"
                    mItemDB?.itemDao?.insert(item3)
                    val item4 = Item()
                    item4.name = "Narnia"
                    if (hours in 13..22) item4.price = 20 else item4.price = 15
                    item4.inventory = 10
                    item4.variant = "Book"
                    mItemDB?.itemDao?.insert(item4)
                    val item5 = Item()
                    item5.name = "Shirt"
                    if (hours in 13..22) item5.price = 30 else item5.price = 15
                    item5.inventory = 3
                    item5.variant = "Cloth"
                    mItemDB?.itemDao?.insert(item5)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.cart) {
            val item1 = selected as ArrayList<Item>
            val intent = Intent(this@MainActivity, CartActivity::class.java)
            val b = Bundle()
            b.putSerializable("cart", item1)
            intent.putExtras(b)
            intent.putExtra("userName", userName)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class QueryDB {
        private var newList: List<Item?>? = ArrayList()

        fun getData() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    newList = mItemDB?.itemDao?.allItems
                    runOnUiThread {
                        mList = newList
                        val adapter = ItemsAdapter(mList as List<Item>)
                        productsList?.layoutManager = LinearLayoutManager(this@MainActivity)
                        productsList?.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private inner class ItemsAdapter(itemList: List<Item>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {
        private var item: List<Item> = ArrayList()

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
            holder.add.setOnClickListener {
                itm.count = itm.count + 1
                holder.count.text = itm.count.toString()
                if (!selected.contains(itm)) selected.add(itm)
            }
            holder.remove.setOnClickListener {
                if (itm.count > 0) itm.count = itm.count - 1
                holder.count.text = itm.count.toString()
                if (itm.count == 0) selected.remove(itm)
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
            selected = ArrayList()
            item = itemList
        }
    }

    companion object {
        private var mItemDB: ItemDB? = null
        var selected: MutableList<Item> = ArrayList()
    }
}