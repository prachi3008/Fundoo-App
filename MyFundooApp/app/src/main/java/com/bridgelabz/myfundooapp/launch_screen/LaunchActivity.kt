package com.bridgelabz.myfundooapp.launch_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.bridgelabz.myfundooapp.user_module.MainActivity
import com.bridgelabz.myfundooapp.R

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hiding title bar of this activity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //making this activity full screen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_launch)

        //4second splash time
        Handler().postDelayed({
            //start main activity
            startActivity(Intent(this@LaunchActivity, MainActivity::class.java))
            //finish this activity
            finish()
        },4000)
    }
}
