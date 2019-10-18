package com.bridgelabz.myfundooapp.user_module

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bridgelabz.myfundooapp.DashboardActivity
import com.bridgelabz.myfundooapp.R
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

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

    lateinit var handler: UserDataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        handler = UserDataManager(this)

        var login = findViewById<Button>(R.id.loginButtonId)
        login.setOnClickListener {
            if (validateEmail() || validatePassword()) {
                if (handler.isUserPresent(
                        loginEmailET.text.toString(),
                        loginPasswordET.text.toString()
                    )
                ) {
                    Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid user", Toast.LENGTH_LONG).show()
                }
            }
        }

        var create = findViewById<TextView>(R.id.createAccountTextLink)
        create.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    fun validateEmail(): Boolean {
        if (TextUtils.isEmpty(loginEmailET.text.toString())) {
            loginTextInputEmail.text = "Fields cant be empty."
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(loginEmailET.text.toString()).matches()) {
            loginTextInputEmail.text = "Please enter a valid email address."
            return false
        } else {
            return true
        }
    }

    fun validatePassword():Boolean
    {
        if(TextUtils.isEmpty(loginPasswordET.text.toString())){
            loginTextInputPassword.text = "Fields cant be empty."
            return false
        }else if(!PASSWORD_PATTERN.matcher(loginPasswordET.text.toString()).matches()){
            loginTextInputPassword.text = "Password is too weak."
            return false
        }else{
            return true
        }
    }
}
