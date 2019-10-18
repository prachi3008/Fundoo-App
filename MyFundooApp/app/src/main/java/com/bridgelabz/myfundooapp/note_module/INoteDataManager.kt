package com.bridgelabz.myfundooapp.note_module

import android.content.ContentValues
import android.database.Cursor

interface INoteDataManager {
    fun insert(values: ContentValues): Long
    fun Query(
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        soOrder: String
    ): Cursor
    fun delete(selection: String,selectionArgs: Array<String>):Int
    fun update(values: ContentValues,selection: String,selectionArgs: Array<String>):Int
}