package com.bridgelabz.myfundooapp.user_module

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bridgelabz.myfundooapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*
import java.util.regex.Pattern

class CreateAccountActivity : AppCompatActivity() {

    internal lateinit var handler: UserDataManager
    var create: Button? = null
    var login: TextView? = null
    var PASSWORD_PATTERN: Pattern? = null
    private lateinit var auth: FirebaseAuth
    var emailValidationTV: TextView? = null
    var emailInputET: EditText? = null
    var passwordValidationTV: TextView? = null
    var passwordInputET: EditText? = null
    var usernameValidationTV: TextView? = null
    var usernameInputET: EditText? = null
    var TAG = "CreateAccountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        objects()
        findViews()
        onClickListeners()
    }

    private fun objects(){
        handler = UserDataManager(this)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    private fun validateEmail(): Boolean {
        if (TextUtils.isEmpty(emailInputET!!.text.toString())) {
            emailValidationTV!!.text = "Fields cant be empty."
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInputET!!.text.toString()).matches()) {
            emailValidationTV!!.text = "Please enter a valid email address."
            return false
        } else {
            return true
        }
    }

    private fun validatePassword(): Boolean {
        if (TextUtils.isEmpty(passwordInputET!!.text.toString())) {
            passwordValidationTV!!.text = "Fields cant be empty."
            return false
        } else if (!PASSWORD_PATTERN!!.matcher(passwordInputET!!.text.toString()).matches()) {
            passwordValidationTV!!.text = "Password is too weak."
            return false
        } else {
            return true
        }
    }

    private fun validateUsername(): Boolean {
        if (TextUtils.isEmpty(usernameInputET!!.text.toString())) {
            usernameValidationTV!!.text = "Fields cant be empty."
            return false
        } else if (usernameInputET!!.text.toString().length > 15) {
            usernameValidationTV!!.text = "Name is too long"
            return false
        } else {
            return true
        }
    }

    private fun findViews() {
        usernameInputET = findViewById(R.id.accountNameET)
        usernameValidationTV = findViewById(R.id.createAccountTextInputName)
        passwordInputET = findViewById(R.id.accountPasswordET)
        passwordValidationTV = findViewById(R.id.createAccountTextInputPassword)
        emailInputET = findViewById(R.id.accountEmailET)
        emailValidationTV = findViewById(R.id.createAccountTextInputEmail)
        create = findViewById(R.id.accountCreateActBtn)
        login = findViewById(R.id.loginTextLink)
        PASSWORD_PATTERN = Pattern.compile(
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
    }

    private fun signUp() {
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

    private fun login() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun firebaseSignUp() {
        if (validateUsername() || validateEmail() || validatePassword()) {
            auth.createUserWithEmailAndPassword(
                emailInputET!!.text.toString(),
                passwordInputET!!.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, " SignUp failed. Try again after some time.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun onClickListeners() {
        create!!.setOnClickListener {
            //signUp()
            firebaseSignUp()
        }

        login!!.setOnClickListener {
            login()
        }
    }
}
