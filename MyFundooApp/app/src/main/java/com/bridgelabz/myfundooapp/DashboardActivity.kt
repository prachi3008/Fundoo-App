package com.bridgelabz.myfundooapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import org.json.JSONObject

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnNoteListener {

    private var notes = mutableListOf<Note>()
    var listadapter = ListAdapter(this, notes, this)
    var toolbar: Toolbar? = null
    var drawer: DrawerLayout? = null
    var navigationView: NavigationView? = null
    val dbManager = NoteDataManager(this)
    lateinit var image: ImageView
    lateinit var emailId: TextView
    lateinit var username: TextView
    var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)
        findView()
        image = navigationView!!.getHeaderView(0).findViewById(R.id.drawerPhoto)
        emailId = navigationView!!.getHeaderView(0).findViewById(R.id.drawerEmail)
        username = navigationView!!.getHeaderView(0).findViewById(R.id.drawerName)
        sharedPref()
        //fbLogOut()
        navigationDrawer()
        //checkLoginStatus()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    NotesFragment()
                ).commit()
            navigationView!!.setCheckedItem(R.id.nav_notes)
        }
        googleSign()
    }

//    fun loadUserProfile(newAccessToken: AccessToken) {
//        val request = GraphRequest.newMeRequest(newAccessToken,
//            object : GraphRequest.GraphJSONObjectCallback {
//                override fun onCompleted(jsonObject: JSONObject?, response: GraphResponse?) {
//                    try {
//                        val first_name = jsonObject!!.getString("first_name")
//                        val last_name = jsonObject.getString("last_name")
//                        //val email = jsonObject.getString("email")
//                        //val id = jsonObject.getString("id")
//                        //val image_url = "https://graph.facebook.com/$id/picture?type=normal"
//
//                        username.setText("$first_name $last_name")
//                        //emailId.setText(email)
////                        val requestOptions = RequestOptions()
////                        requestOptions.dontAnimate()
//                        //Glide.with(this@DashboardActivity).load(image_url).into(image)
//
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//            })
//
//        val parameters = Bundle()
//        parameters.putString("fields", "first_name, last_name")
//        request.parameters = parameters
//        request.executeAsync()
//
//    }

//    private fun checkLoginStatus() {
//        if (AccessToken.getCurrentAccessToken() != null) {
//            loadUserProfile(AccessToken.getCurrentAccessToken())
//        }
//    }

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

            R.id.nav_important -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                ImportantFragment()
            ).commit()

            R.id.nav_archive -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                ArchievedFragment()
            ).commit()

            R.id.nav_delete -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                TrashFragment()
            ).commit()

            R.id.nav_logout -> {
                googleSignOut()
                simpleLogout()
                //fbLogOut()
            }
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
                dbManager.LoadQuery("% {$query} %")
                listadapter.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dbManager.LoadQuery("% {$newText} %")
                //listadapter.notifyDataSetChanged()
                //listadapter.getFilter().filter(newText)
                Log.d("prachi", newText)
                listadapter.notifyDataSetChanged()
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.app_bar_grid -> {
                    val mrecyleView = findViewById<RecyclerView>(R.id.recyclerviewNotes)
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
                    googleSignOut()
                    simpleLogout()
                    //fbLogOut()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun findView() {
        toolbar = findViewById(R.id.toolbar)
        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
    }

    private fun navigationDrawer() {
        setSupportActionBar(toolbar)
        navigationView!!.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
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

    private fun googleSign() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val acct: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (acct != null) {
            val personName = acct.getDisplayName()
            val personEmail = acct.getEmail()
            val personPhoto = acct.getPhotoUrl()

            username.setText(personName)
            emailId.setText(personEmail)
            Glide.with(this).load(personPhoto).into(image)
        }
    }

    private fun googleSignOut() {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this, OnCompleteListener<Void> {
                Toast.makeText(this, "Logout successfully", Toast.LENGTH_LONG).show()
                finish()
            })
    }

    private fun simpleLogout() {
        val token = getSharedPreferences("userEmail", Context.MODE_PRIVATE)
        val editor = token.edit()
        editor.putString("loginUserEmail", " ")
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "Logout successfully", Toast.LENGTH_LONG).show()
        finish()
    }

//    private fun fbLogOut() {
//        var tokenTracker: AccessTokenTracker = object : AccessTokenTracker() {
//            override fun onCurrentAccessTokenChanged(
//                oldAccessToken: AccessToken,
//                currentAccessToken: AccessToken?
//            ) {
//                if (currentAccessToken == null) {
//                    username.setText("")
//                    //emailId.setText("")
//                    //image.setImageResource(0)
//                    Toast.makeText(this@DashboardActivity, "User Logged out", Toast.LENGTH_LONG)
//                        .show()
//                } else
//                    loadUserProfile(currentAccessToken)
//            }
//        }
//    }
}
