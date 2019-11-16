package com.bridgelabz.myfundooapp.note_module

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bridgelabz.myfundooapp.Data.DatabaseHandler
import kotlin.collections.ArrayList


class NoteDataManager(context: Context) : INoteDataManager {
    companion object {
        val CREATE_NOTE_QUERY =
            "CREATE TABLE IF NOT EXISTS notes (ID integer PRIMARY KEY AUTOINCREMENT, " +
                    "Title varchar(30),Description varchar (100),Date varchar (50)," +
                    "Color integer, Archieve boolean, Important boolean, Reminder boolean, Remove boolean, " +
                    "Position INTEGER)";
    }

    val handler = DatabaseHandler(context)
    private var notes = mutableListOf<Note>()

    override fun insertNote(values: ContentValues): Long {
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
        val count = sqlDB.delete("notes", selection, selectionArgs)
        handler.closeDB()
        return count
    }

    override fun update(
        values: ContentValues,
        selection: String,
        selectionArgs: Array<String>
    ): Int {
        val sqlDB = handler.openDB()
        val count = sqlDB.update("notes", values, selection, selectionArgs)
        handler.closeDB()
        return count
    }

     fun LoadQuery(title: String) {
        //var dbManager = NoteDataManager(context = Context)
        val projections = arrayOf(
            "ID", "Title", "Description", "Date", "Color", "Reminder",
            "Archieve", "Important", "Remove","Position"
        )
        val selectionArgs = arrayOf(title)
        val cursor = Query(
            projections, "Title like ?", selectionArgs,
            "ID"
        )
        notes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))
                val Date = cursor.getString(cursor.getColumnIndex("Date"))
                val Color = cursor.getInt(cursor.getColumnIndex("Color"))
                val Reminder = cursor.getString(cursor.getColumnIndex("Reminder")).equals("false")
                val Archieve = cursor.getInt(cursor.getColumnIndex("Archieve")) == 1
                val Important = cursor.getInt(cursor.getColumnIndex("Important")) == 1
                val Remove = cursor.getInt(cursor.getColumnIndex("Remove")) == 1
                val Position = cursor.getInt(cursor.getColumnIndex("Position"))
                notes.add(
                    Note(
                        ID,
                        Title,
                        Description,
                        Date,
                        Color,
                        Reminder,
                        Archieve,
                        Important,
                        Remove,
                        Position
                    )
                )
                //notesFull.add(Note(ID, Title, Description, Date))
            } while (cursor.moveToNext())
        }
    }

//    fun getList(): LiveData<List<Note>> {
//        val mutableLiveData = MutableLiveData<List<Note>>()
//
//        val sqlDB = handler.openDB()
//        val cursor = sqlDB.rawQuery("select * from notes", null)
//        cursor.use {
//            while (it.moveToNext()) {
//                with(cursor) {
//                    val id = getInt(0)
//                    val title = getString(1)
//                    val description = getString(2)
//                    val date = getString(3)
//                    val color = getInt(4)
//                    //val result = "Id : $id, title : $title, description : $description"
//                    val noteList: ArrayList<Note> = arrayListOf(
//                        Note(id, title, description,date,color)
//                    )
//                    mutableLiveData.value = noteList
//                }
//            }
//        }
//        return mutableLiveData
//    }

}