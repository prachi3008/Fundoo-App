package com.bridgelabz.myfundooapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.ClipboardManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bridgelabz.myfundooapp.note_module.AddNoteActivity
import com.bridgelabz.myfundooapp.note_module.NoteDataManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.row.*
import kotlinx.android.synthetic.main.row.view.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var listNotes = ArrayList<Note>()

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_notes -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                Notes_Fragment()
            ).commit()

            R.id.nav_reminders -> supportFragmentManager.beginTransaction().replace(
                R.id.loginButton,
                Reminder_Fragment()
            ).commit()

        }
        var drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)
        LoadQuery("%")

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
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

        if (savedStateRegistry == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Notes_Fragment()).commit()
            navigationView.setCheckedItem(R.id.nav_notes)
        }

    }

    override fun onBackPressed() {
        var drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    private fun LoadQuery(title: String) {
        val dbManager = NoteDataManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections, "Title like?", selectionArgs, "Title")
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))
            } while (cursor.moveToNext())
        }
        //adapter
        val myNotesAdapter = MyNotesAdapter(this, listNotes)

        //set adapter
        notesLV.adapter = myNotesAdapter

        //get total number of task from list view
        val total = notesLV.count

        //action bar
        val mActionBar = supportActionBar

        if (mActionBar != null) {
            //set to actionbar as subtitle os actionbar
            mActionBar.subtitle = "You have $total list(s) in the list"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        //search View
        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                LoadQuery("% ${query} %")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                LoadQuery("% ${newText} %")
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            when (item.itemId) {
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
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter : BaseAdapter {
        var listNotesAdapter = ArrayList<Note>()
        var context: Context? = null

        constructor(context: Context, listNotesAdapter: ArrayList<Note>) {
            this.listNotesAdapter = listNotesAdapter
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //inflate layout row.xml
            var myView = layoutInflater.inflate(R.layout.row, null)
            var myNote = listNotesAdapter[position]
            myView.titleTV.text = myNote.nodeTitle
            myView.descTV.text = myNote.nodeDesc

            //delete button click
            deleteBtn.setOnClickListener {
                var dbManager =
                    NoteDataManager(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeId.toString())
                dbManager.delete("ID=?", selectionArgs)
                LoadQuery("%")
            }

            //edit,update button click
            myView.editBtn.setOnClickListener {
                GoToUpdateFunction(myNote)
            }

            //copy button
            myView.copyBtn.setOnClickListener {
                //get title
                val title = myView.titleTV.text.toString()
                //get desciption
                val desc = myView.descTV.text.toString()
                //concatination
                val s = title + "\n" + desc
                val cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cb.text = s // add to clipboard
                Toast.makeText(this@DashboardActivity, "Copied....", Toast.LENGTH_SHORT).show()
            }

            //share button click
            myView.shareBtn.setOnClickListener {
                //get title
                val title = myView.titleTV.text.toString()
                //get desciption
                val desc = myView.descTV.text.toString()
                //concatination
                val s = title + "\n" + desc
                //share intent
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))
            }
            return myView
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

    }

    private fun GoToUpdateFunction(myNote: Note) {
        var intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("ID", myNote.nodeId) //put id
        intent.putExtra("Title", myNote.nodeTitle) //put title
        intent.putExtra("Description", myNote.nodeDesc) //put description
        startActivity(intent)
    }

}
