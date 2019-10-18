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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        handler = UserDataManager(this)
        var create = findViewById<Button>(R.id.accountCreateActBtn)
        var login = findViewById<TextView>(R.id.loginTextLink)
        create.setOnClickListener {
            if (validateUsername() || validateEmail() || validatePassword()) {
                handler.insertUserData(
                    accountNameET.text.toString(),
                    accountEmailET.text.toString(),
                    accountPasswordET.text.toString()
                )
                Log.d("CreateAccountActivity", "Data inseretd ")
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun validateEmail(): Boolean {
        if (TextUtils.isEmpty(accountEmailET.text.toString())) {
            createAccountTextInputEmail.text = "Fields cant be empty."
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(accountEmailET.text.toString()).matches()) {
            createAccountTextInputEmail.text = "Please enter a valid email address."
            return false
        } else {
            return true
        }
    }

    fun validatePassword(): Boolean {
        if (TextUtils.isEmpty(accountPasswordET.text.toString())) {
            createAccountTextInputPassword.text = "Fields cant be empty."
            return false
        } else if (!PASSWORD_PATTERN.matcher(accountPasswordET.text.toString()).matches()) {
            createAccountTextInputPassword.text = "Password is too weak."
            return false
        } else {
            return true
        }
    }

    fun validateUsername(): Boolean {
        if (TextUtils.isEmpty(accountNameET.text.toString())) {
            createAccountTextInputName.text = "Fields cant be empty."
            return false
        } else if (accountNameET.text.toString().length > 15) {
            createAccountTextInputName.text = "Name is too long"
            return false
        } else {
            return true
        }
    }
}
