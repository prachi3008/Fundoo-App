package com.bridgelabz.myfundooapp.note_module


import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bridgelabz.myfundooapp.R
import kotlinx.android.synthetic.main.activity_add_note.*
import java.lang.Exception

class AddNoteActivity : AppCompatActivity() {

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        try{
            val bundle : Bundle? = intent.extras
            id = bundle!!.getInt("ID",0)
            if(id != 0)
            {
                addNoteTitleET.setText(bundle.getString("Title"))
                addNoteDescET.setText(bundle.getString("Description"))
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun addFunc(view: View) {
        var dbManager = NoteDataManager(this)
        var values = ContentValues()
        values.put("Title",addNoteTitleET.text.toString())
        values.put("Description",addNoteDescET.text.toString())
        if(id == 0)
        {
            val ID = dbManager.insert(values)
            if(id > 0){
                Toast.makeText(this,"Note is added",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this,"Error in adding note",Toast.LENGTH_SHORT).show()
            }

        }else{
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values,"ID=?",selectionArgs)
            if(ID > 0)
            {
                Toast.makeText(this,"Note is added",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this,"Error in adding note",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
