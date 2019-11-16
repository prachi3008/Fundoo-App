package com.bridgelabz.myfundooapp.user_module

import android.app.Activity
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
import android.widget.Toast.LENGTH_SHORT
import com.bridgelabz.myfundooapp.DashboardActivity
import com.bridgelabz.myfundooapp.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    var PASSWORD_PATTERN: Pattern? = null
    lateinit var handler: UserDataManager
    var mailET: EditText? = null
    var passwordET: EditText? = null
    var loginBtn: Button? = null
    var createAccountTextView: TextView? = null
    var googleSignIn: Button? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    var RC_SIGN_IN: Int = 9001
    var fbLoginBtn: LoginButton? = null
    var callbackManager: CallbackManager? = null
    private lateinit var auth: FirebaseAuth
    var emailValidationTV: TextView? = null
    var passwordValidationTV: TextView? = null
    var TAG = "LoginActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        callbackManager = CallbackManager.Factory.create()
        objects()
        findViews()
        onClickListeners()
        googleSignIn()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
                return
            }
            Toast.makeText(this, "Something went wrong: $resultCode", LENGTH_SHORT).show()
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.result
            // completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("username", account!!.displayName)
            intent.putExtra("email", account.email)
            intent.putExtra("image", account.photoUrl)
            startActivity(intent)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.statusCode)
        }

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun validateEmail(): Boolean {
        if (TextUtils.isEmpty(mailET!!.text.toString())) {
            emailValidationTV!!.text = "Fields cant be empty."
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mailET!!.text.toString()).matches()) {
            emailValidationTV!!.text = "Please enter a valid email address."
            return false
        } else {
            return true
        }
    }

    fun validatePassword(): Boolean {
        if (TextUtils.isEmpty(passwordET!!.text.toString())) {
            passwordValidationTV!!.text = "Fields cant be empty."
            return false
        } else if (!PASSWORD_PATTERN!!.matcher(passwordET!!.text.toString()).matches()) {
            passwordValidationTV!!.text = "Password is too weak."
            return false
        } else {
            return true
        }
    }

    private fun sharedPref() {
        val token = getSharedPreferences("userEmail", Context.MODE_PRIVATE)
        val userEmail = loginEmailET.text.toString()
        val editor = token.edit()
        editor.putString("userEmail", userEmail)
        editor.commit()
    }

    fun findViews() {
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
        passwordValidationTV = findViewById(R.id.loginTextInputPassword)
        emailValidationTV = findViewById(R.id.loginTextInputEmail)
        mailET = findViewById(R.id.loginEmailET)
        passwordET = findViewById(R.id.loginPasswordET)
        loginBtn = findViewById(R.id.loginButtonId)
        createAccountTextView = findViewById(R.id.createAccountTextLink)
        googleSignIn = findViewById(R.id.googleSignIn)
        fbLoginBtn = findViewById(R.id.fb_login_button)
    }

    fun fbLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(
                this,
                Arrays.asList("email", "public_profile", "user_friends")
            )

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d("Success", "$result")
                    var intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    startActivity(intent)
                }

                override fun onError(error: FacebookException?) {
                    Log.e("Facebook login:", "$error")
                }

                override fun onCancel() {
                }
            })
    }

    fun login() {
        val userEmail = loginEmailET.text.toString()
        if (validateEmail() && validatePassword()) {
            if (handler.isUserPresent(mailET!!.text.toString(), passwordET!!.text.toString())) {
                Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("userEmail", userEmail)
                sharedPref()
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid user", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun signUp() {
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }

    fun updateUI(currentUser: FirebaseUser?) {
        val userEmail = loginEmailET.text.toString()
        if (currentUser != null) {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("userEmail", userEmail)
            sharedPref()
            startActivity(intent)
        } else {
            Toast.makeText(baseContext, "Sign In failed.", LENGTH_SHORT).show()
        }
    }

    fun firebaseLogin() {
        if (validateEmail() && validatePassword()) {
            auth.signInWithEmailAndPassword(mailET!!.text.toString(), passwordET!!.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        updateUI(null)
                    }
                }
        }
    }

    fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun objects() {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        handler = UserDataManager(this)
    }

    fun onClickListeners() {
        fbLoginBtn!!.setOnClickListener {
            fbLogin()

        }

        loginBtn!!.setOnClickListener {
            //login()
            firebaseLogin()
        }

        createAccountTextView!!.setOnClickListener {
            signUp()
        }

        googleSignIn!!.setOnClickListener {
            signIn()
        }
    }
}
