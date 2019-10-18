package com.bridgelabz.myfundooapp.Data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.bridgelabz.myfundooapp.note_module.NoteDataManager.Companion.CREATE_NOTE_QUERY
import com.bridgelabz.myfundooapp.user_module.UserDataManager.Companion.CREATE_USER_QUERY

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, "Application.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_USER_QUERY)
        db?.execSQL(CREATE_NOTE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        when (oldVersion) {
            1 -> upgradeFromVer1(db)
        }
    }

    private fun upgradeFromVer1(db: SQLiteDatabase?) = db?.execSQL(CREATE_NOTE_QUERY)
}