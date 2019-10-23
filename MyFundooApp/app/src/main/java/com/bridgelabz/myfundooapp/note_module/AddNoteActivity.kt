package com.bridgelabz.myfundooapp.note_module


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.bridgelabz.myfundooapp.R
import kotlinx.android.synthetic.main.activity_add_note.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    var id : Int? = null
    var format = SimpleDateFormat("dd MMM, YYYY",Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        var dateEditText= findViewById<EditText>(R.id.datepicker)
        dateEditText.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                val date = format.format(selectedDate.time)
                dateEditText.setText("$date")
            },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        try {
            val bundle: Bundle? = intent.extras
            id = bundle?.getInt("ID", 0)
            if (id != null) {
                addNoteTitleET.setText(bundle!!.getString("Title"))
                addNoteDescET.setText(bundle.getString("Description"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addFunc(view: View) {
        var dbManager = NoteDataManager(this)
        var values = ContentValues()

        val title = addNoteTitleET.text.toString()
        var description = addNoteDescET.text.toString()

        values.put("Title", title)
        values.put("Description", description)
        if (id == null) {
            Log.e("AddNoteActivity", "inside insert method" +
                    "$title  &  $description");

            val ID = dbManager.insertNote(values)
            if (ID > 0) {
                Log.e("AddNoteActivity", "inside being inserted method $title  &  $description & $ID");
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error in adding note", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID=?", selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error in adding note", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}
