package com.bridgelabz.myfundooapp.note_module

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bridgelabz.myfundooapp.DashboardActivity
import com.bridgelabz.myfundooapp.R

class Notifications {
    val NOTIFYTAG = "new request"

    fun notify(context: Context,message : String,number : Int){
        val intent = Intent(context,DashboardActivity::class.java)
        val builder = NotificationCompat.Builder(context,"MyNotifications")
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentTitle("Notification")
            .setContentText(message)
            .setNumber(number)
            .setSmallIcon(R.drawable.keepicon)
            .setContentIntent(PendingIntent.getActivity(context,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT))
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        notificationManager.notify(NOTIFYTAG,0,builder.build())

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
//        {
//            notificationManager.notify(NOTIFYTAG,0,builder.build())
//        }else{
//            notificationManager.notify(NOTIFYTAG.hashCode(),builder.build())
//        }
    }
}