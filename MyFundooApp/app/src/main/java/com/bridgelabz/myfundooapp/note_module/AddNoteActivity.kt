package com.bridgelabz.myfundooapp.note_module


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import com.bridgelabz.myfundooapp.DashboardActivity
import com.bridgelabz.myfundooapp.R
import com.thebluealliance.spectrum.SpectrumPalette
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.row.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    var id: Int? = null
    var format = SimpleDateFormat("dd MMM, YYYY", Locale.US)
    var color : Int? = null

    override fun onBackPressed() {
        addFunc(view = View(this))
        Log.d("prachi","onBackPressed")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        var dateEditText = findViewById<EditText>(R.id.datepicker)
        var palette = findViewById<SpectrumPalette>(R.id.palette)
        palette.setOnColorSelectedListener { clr -> color = clr }

        //default color
        palette.setSelectedColor(resources.getColor(R.color.white))
        color = resources.getColor(R.color.white)

        dateEditText.setOnClickListener {
            datePicker()
        }

        try {
            val bundle: Bundle? = intent.extras
            id = bundle?.getInt("ID", 0)
            if (id != null) {
                addNoteTitleET.setText(bundle!!.getString("Title"))
                addNoteDescET.setText(bundle.getString("Description"))
                datepicker.setText(bundle.getString("Date"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.add_Note_reminder -> {
                    datePicker()
                }

                R.id.add_Note_archieve -> {

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun datePicker() {
        val dateEditText = findViewById<EditText>(R.id.datepicker)
        val now = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this, OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = format.format(selectedDate.time)
                dateEditText.setText("$date")
            },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()

    }

    fun addFunc(view: View) {
        val dbManager = NoteDataManager(this)
        val values = ContentValues()

        val title = addNoteTitleET.text.toString()
        val description = addNoteDescET.text.toString()
        val date = datepicker.text.toString()
        val color = palette.resources.getColor(R.color.white)

        values.put("Title", title)
        values.put("Description", description)
        values.put("Date", date)
        values.put("Color",color)
        if (id == null) {
            Log.e(
                "AddNoteActivity", "inside insert method" +
                        "$title  &  $description"
            );

            val ID = dbManager.insertNote(values)
            if (ID > 0) {
                Log.e(
                    "AddNoteActivity",
                    "inside being inserted method $title  &  $description & $ID"
                );
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error in adding note", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            val selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID=?", selectionArgs)
            if (ID > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error in adding note", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}
