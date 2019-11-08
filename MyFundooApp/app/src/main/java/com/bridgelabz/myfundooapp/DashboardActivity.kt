package com.bridgelabz.myfundooapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bridgelabz.myfundooapp.note_module.*
import com.bridgelabz.myfundooapp.user_module.LoginActivity
import com.google.android.material.navigation.NavigationView

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnNoteListener {

    private var notes = mutableListOf<Note>()
    var listadapter =  ListAdapter(this, notes, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)
        sharedPref()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    NotesFragment()
                ).commit()
            navigationView.setCheckedItem(R.id.nav_notes)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_notes -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                NotesFragment()
            ).commit()

            R.id.nav_reminders -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                ReminderFragment()
            ).commit()
        }
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        //search View
        val searchView: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                listadapter.LoadQuery("% {$query} %")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listadapter.LoadQuery("% {$newText} %")
                //listadapter.notifyDataSetChanged()
                listadapter.getFilter().filter(newText)
                Log.d("prachi",newText)
                //listadapter.notifyDataSetChanged()
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
   }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.app_bar_grid -> {
                    val mrecyleView = findViewById<RecyclerView>(R.id.recyclerview)
                    val staggeredGridLayoutManager: StaggeredGridLayoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    mrecyleView!!.layoutManager = (staggeredGridLayoutManager)
                }

                R.id.addNote -> {
                    startActivity(
                        Intent(
                            this,
                            AddNoteActivity::class.java
                        )
                    )
                }

                R.id.action_settings -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                }

                R.id.action_signout -> {
                    val token = getSharedPreferences("userEmail", Context.MODE_PRIVATE)
                    val editor = token.edit()
                    editor.putString("loginUserEmail", " ")
                    editor.apply()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Logout successfully", Toast.LENGTH_LONG).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNoteClick(position: Int) {
    }

    private fun sharedPref() {
        val token = getSharedPreferences("userEmail", Context.MODE_PRIVATE)
        val mail = token.getString("userEmail", "abc")
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val mailDrawer = navigationView.getHeaderView(0).findViewById<TextView>(R.id.drawerEmail)
        mailDrawer.text = mail
    }

//    private fun LoadQuery(title: String) {
//        val dbManager = NoteDataManager(this)
//        val projections = arrayOf("ID", "Title", "Description", "Date")
//        val selectionArgs = arrayOf(title)
//        val cursor = dbManager.Query(
//            projections, "Title like ?", selectionArgs,
//            "Title"
//        )
//        notes.clear()
//        if (cursor.moveToFirst()) {
//            do {
//                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
//                val Title = cursor.getString(cursor.getColumnIndex("Title"))
//                val Description = cursor.getString(cursor.getColumnIndex("Description"))
//                val Date = cursor.getString(cursor.getColumnIndex("Date"))
//                notes.add(Note(ID, Title, Description, Date))
//            } while (cursor.moveToNext())
//        }
//    }
}
