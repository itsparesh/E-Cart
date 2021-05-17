package com.test.ecart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.test.ecart.room.items.Item
import com.test.ecart.room.items.ItemDB
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddItemActivity : AppCompatActivity() {
    private var isData: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        setOnClickListener()
        if (!intent?.getStringExtra("itemName").isNullOrBlank()) {
            isData = true
            addBtnET?.text = getString(R.string.update)
            itemNameET?.setText(intent?.getStringExtra("itemName"))
        }
    }

    private fun setOnClickListener() {
        try {
            addBtnET?.setOnClickListener {
                val itemName = itemNameET?.editableText.toString()
                val variant = variantET?.editableText.toString()
                val price = priceET?.editableText.toString()
                val inventory = inventoryET?.editableText.toString()
                if (itemName.isNotBlank() && variant.isNotBlank() && price.isNotBlank() && inventory.isNotBlank()) {
                    price.toIntOrNull()?.let { it1 -> inventory.toIntOrNull()?.let { it2 -> addItem(itemName, variant, it1, it2) } }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@AddItemActivity, "Please enter valid details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this@AddItemActivity, "Please enter valid details", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun addItem(itemName: String, variant: String, price: Int, inventory: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val itemDB = ItemDB.getDatabase(this@AddItemActivity)
                val item = Item()
                item.name = itemName
                item.variant = variant
                item.price = price
                item.inventory = inventory
                itemDB.itemDao?.insert(item)
                runOnUiThread {
                    if (isData) {
                        Toast.makeText(this@AddItemActivity, "Item Updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AddItemActivity, "Item Added", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    if (isData) {
                        Toast.makeText(this@AddItemActivity, "Item Update Failed", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AddItemActivity, "Item Addition Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}