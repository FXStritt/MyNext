package com.example.mynext

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.mynext.data.MainRoomDB
import com.example.mynext.util.MainRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repository: MainRepository //TODO for debug purposes, access to main rep to clear DB. Remove when not needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        //Hide title and show textview instead in order to set a click listener on it
        supportActionBar?.setDisplayShowTitleEnabled(false)
        findViewById<TextView>(R.id.toolbarTitle).setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.CategoriesFragment)
        }

    }

    override fun onStart() {
        super.onStart()
        val dao = MainRoomDB.getDatabase(application).dao()
        repository = MainRepository(dao)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_debug_delete ->  {
                GlobalScope.launch { repository.deleteAll() }
                true
            }
            R.id.action_debug_populate -> {
                GlobalScope.launch { repository.populateDummyItems(applicationContext) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}