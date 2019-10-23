package com.bridgelabz.myfundooapp.user_module

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.bridgelabz.myfundooapp.Data.DatabaseHandler

class UserDataManager(context: Context) : IUserDataManager {
    companion object {
        val CREATE_USER_QUERY = "CREATE TABLE user (id integer PRIMARY KEY AUTOINCREMENT, name varchar(30)," +
                " email varchar (100),password varchar(20))";
    }

    val handler = DatabaseHandler(context = context)
    val db = handler.writableDatabase

    override fun insertUserData(name: String, email: String, password: String) {
        val values: ContentValues = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }
        Log.d("insertNote data", "$values")
        db.insert("user", null, values)
        db.close()
    }

    override fun isUserPresent(email: String, password: String): Boolean {
        val query = "SELECT * FROM user WHERE email = ? and password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        if (cursor.count <= 0) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }
}