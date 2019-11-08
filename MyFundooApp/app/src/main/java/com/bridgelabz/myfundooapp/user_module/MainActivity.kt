package com.bridgelabz.myfundooapp.user_module

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.bridgelabz.myfundooapp.DashboardActivity
import com.bridgelabz.myfundooapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val token = getSharedPreferences("userEmail", Context.MODE_PRIVATE)

        var loginButton = findViewById<Button>(R.id.loginButton)
        var createAccountButton = findViewById<Button>(R.id.createAccountButton)

        if(token.getString("userEmail", " ")!= " "){
            var intent = Intent(this,DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        loginButton.setOnClickListener{
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        createAccountButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
