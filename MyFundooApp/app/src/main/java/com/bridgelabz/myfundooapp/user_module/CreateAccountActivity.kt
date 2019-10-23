package com.bridgelabz.myfundooapp.user_module

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bridgelabz.myfundooapp.R
import kotlinx.android.synthetic.main.activity_create_account.*
import java.util.regex.Pattern

class CreateAccountActivity : AppCompatActivity() {

    val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$"
    )

    internal lateinit var handler: UserDataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        var usernameET = findViewById<TextView>(R.id.accountNameET)
        var emailET = findViewById<TextView>(R.id.accountEmailET)
        var passwordET = findViewById<TextView>(R.id.accountPasswordET)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        handler = UserDataManager(this)

        var create = findViewById<Button>(R.id.accountCreateActBtn)
        create.setOnClickListener {
            if (validateUsername() || validateEmail() || validatePassword()) {
                handler.insertUserData(
                    usernameET.text.toString(),
                    emailET.text.toString(),
                    passwordET.text.toString()
                )
                Log.d("CreateAccountActivity", "Data inseretd ")
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        var login = findViewById<TextView>(R.id.loginTextLink)
        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun validateEmail(): Boolean {
        var email = findViewById<TextView>(R.id.createAccountTextInputEmail)
        var emailET = findViewById<TextView>(R.id.accountEmailET)
        if (TextUtils.isEmpty(emailET.text.toString())) {
            email.text = "Fields cant be empty."
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailET.text.toString()).matches()) {
            email.text = "Please enter a valid email address."
            return false
        } else {
            return true
        }
    }

    fun validatePassword(): Boolean {
        var password = findViewById<TextView>(R.id.createAccountTextInputPassword)
        var passwordET = findViewById<TextView>(R.id.accountPasswordET)
        if (TextUtils.isEmpty(passwordET.text.toString())) {
            password.text = "Fields cant be empty."
            return false
        } else if (!PASSWORD_PATTERN.matcher(passwordET.text.toString()).matches()) {
            password.text = "Password is too weak."
            return false
        } else {
            return true
        }
    }

    fun validateUsername(): Boolean {

        var username = findViewById<TextView>(R.id.createAccountTextInputName)
        var usernameET: TextView = findViewById(R.id.accountNameET)
        if (TextUtils.isEmpty(usernameET.text.toString())) {
            username.text = "Fields cant be empty."
            return false
        } else if (usernameET.text.toString().length > 15) {
            username.text = "Name is too long"
            return false
        } else {
            return true
        }
    }
}
