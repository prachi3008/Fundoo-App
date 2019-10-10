package com.bridgelabz.myfundooapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    var mAuth : FirebaseAuth? = null
    var mDatabase : DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        mAuth = FirebaseAuth.getInstance()
        accountCreateActBtn.setOnClickListener{
            var email = accountEmailET.text.toString().trim()
            var password = accountPasswordET.text.toString().trim()
            var name = accountNameET.text.toString().trim()

            if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(name)){
                createAccount(email,password,name)
            }else{
                Toast.makeText(this,"Please fill out all the fields",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun createAccount(email : String,password : String,name : String)
    {
        mAuth!!.createUserWithEmailAndPassword(email,password)

    }
}
