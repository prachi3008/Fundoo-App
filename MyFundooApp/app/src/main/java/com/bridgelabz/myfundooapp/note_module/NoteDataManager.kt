package com.bridgelabz.myfundooapp.note_module

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import com.bridgelabz.myfundooapp.Data.DatabaseHandler


class NoteDataManager(context: Context) : INoteDataManager {
    companion object {
        val CREATE_NOTE_QUERY = "CREATE TABLE IF NOT EXISTS notes (ID integer PRIMARY KEY AUTOINCREMENT, " +
                "Title varchar(30),Description varchar (100))";
    }

    val handler = DatabaseHandler(context = context)

//    var sqlDB: SQLiteDatabase? = null
//
//    constructor(context: Context) {
//        var db = DatabaseHandler(context)
//        sqlDB = db.writableDatabase
//    }

//    inner class DatabaseHelperNotes : SQLiteOpenHelper {
//        var context: Context? = null
//
//        constructor(context: Context) : super(context, dbName, null, dbVersion) {
//            this.context = context
//            val databaseHandler = DatabaseHandler(context)
//            sqlDB = databaseHandler.writableDatabase
//        }
//
//        override fun onCreate(db: SQLiteDatabase?) {
//            db!!.execSQL(sqlCreateTable)
//            Toast.makeText(this.context, "Database created...", Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//            db!!.execSQL("DROP TABLE IF EXISTS" + dbTable)
//        }
//    }

    override fun insertNote(values: ContentValues) : Long {
        val sqlDB = handler.openDB()
        val id = sqlDB.insert("notes", null, values)
        handler.close()
        return id
    }

    override fun Query(
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        soOrder: String
    ): Cursor {
        val sqlDB = handler.openDB()
        val qb = SQLiteQueryBuilder()
        qb.tables = "notes"
        val cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, soOrder)
        return cursor
    }

    override fun delete(selection: String, selectionArgs: Array<String>): Int {
        val sqlDB = handler.openDB()
        val count = sqlDB!!.delete("notes", selection, selectionArgs)
        handler.closeDB()
        return count
    }

    override fun update(
        values: ContentValues,
        selection: String,
        selectionArgs: Array<String>
    ): Int {
        val sqlDB = handler.openDB()
        var count = sqlDB!!.update("notes", values, selection, selectionArgs)
        handler.closeDB()
        return count
    }

fun getList(){
    val sqlDB = handler.openDB()
    val cursor = sqlDB.rawQuery("select * from notes",null)
    cursor.use {
        while (it.moveToNext())
        {
            with(cursor){
                val id = getInt(0)
                val title = getString(1)
                val description = getString(2)
                val result = "Id : $id, title : $title, description : $description"
            }
        }
    }
}

}