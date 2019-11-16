package com.bridgelabz.myfundooapp.note_module


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bridgelabz.myfundooapp.DashboardActivity
import com.bridgelabz.myfundooapp.R
import com.thebluealliance.spectrum.SpectrumPalette
import kotlinx.android.synthetic.main.activity_add_note.*
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    val position: Int
    init {
        position = nextPosition++
    }
    companion object {
        var nextPosition = 0
    }
    var id: Int? = null
    var format = SimpleDateFormat("dd MMM, YYYY", Locale.US)
    var color: Int = 0
    var dateEditText: EditText? = null
    var isReminderSet = false
    var isArchieved = false
    var isImportant = false
    var isRemove = false
    var menu: Menu? = null
    val notifyme = Notifications()
    var number: Int = 0
    var palette :SpectrumPalette? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        findViews()
        defaultColorSet()
        setData()
        onClickListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_Note_reminder -> {
                dateEditText!!.isVisible = true
                datePicker()
                isReminderSet = true
            }

            R.id.add_Note_archieve -> {
                if (isArchieved) {
                    menu!!.findItem(R.id.add_Note_archieve)
                        .setIcon(R.drawable.ic_action_archive)
                        .setTitle("Not Archieve")
                    isArchieved = false
                } else {
                    menu!!.findItem(R.id.add_Note_archieve)
                        .setIcon(R.drawable.ic_action_unarchive)
                        .setTitle("Archieve")
                    isArchieved = true
                }
            }

            R.id.add_note_important -> {
                if (isImportant) {
                    menu!!.findItem(R.id.add_note_important)
                        .setIcon(R.drawable.ic_action_star_border)
                        .setTitle("Not Important")
                    isImportant = false
                } else {
                    menu!!.findItem(R.id.add_note_important)
                        .setIcon(R.drawable.ic_star_black_24dp)
                        .setTitle("Important")
                    isImportant = true
                }
            }

            R.id.add_note_delete -> {
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("warning")
                    .setMessage("Are you sure you want to delete ?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                        isRemove = true
                        addFunc(View(this))
                        Toast.makeText(this, "Note is deleted", Toast.LENGTH_SHORT)
                            .show()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                        isRemove = false
                        addFunc(View(this))
                    })
                    .setIcon(R.drawable.ic_action_warning)
                    .show()
            }

            R.id.palette -> {
                color = palette!!.resources.getColor(R.color.white)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClickListeners(){
        palette!!.setOnColorSelectedListener { clr -> color = clr }
        dateEditText!!.setOnClickListener { datePicker() }
    }

    private fun defaultColorSet(){
        //default color
        palette!!.setSelectedColor(Color.parseColor("#FFFFFF"))
        color = Color.parseColor("#FFFFFF")
    }

    private fun setData(){
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

    private fun findViews(){
        palette = findViewById<SpectrumPalette>(R.id.palette)
        dateEditText = findViewById(R.id.datepicker)
    }

    private fun datePicker() {
        val now = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this, OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = format.format(selectedDate.time)
                dateEditText!!.setText("$date")
            },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()

    }

    override fun onBackPressed() {
        addFunc(View(this))
        super.onBackPressed()
    }

    fun addFunc(view: View) {
        val dbManager = NoteDataManager(this)
        val values = ContentValues()

        val title = addNoteTitleET.text.toString()
        val description = addNoteDescET.text.toString()
        val date = datepicker.text.toString()
        val color = color
        val reminder = isReminderSet
        val archieve = isArchieved
        val important = isImportant
        val remove = isRemove
        val position = nextPosition

        values.put("Title", title)
        values.put("Description", description)
        values.put("Date", date)
        values.put("Color", color)
        values.put("Reminder", reminder)
        values.put("Archieve", archieve)
        values.put("Important", important)
        values.put("Remove", remove)
        values.put("Position", position)

        if (id == null) {
            val ID = dbManager.insertNote(values)
            if (ID > 0) {
                Log.e(
                    "AddNoteActivity",
                    "inside being inserted method $title  &  $description & $ID"
                );
                //Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                notifyme.notify(this, "$title &  $description", number)
                number++
                finish()
            } else {
                //Toast.makeText(this, "Error in adding note", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            val selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID=?", selectionArgs)
            if (ID > 0) {
                //Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                notifyme.notify(this, "$title &  $description", number)
                number++
                finish()
            } else {
                //Toast.makeText(this, "Error in adding note", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}
