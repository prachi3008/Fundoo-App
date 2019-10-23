package com.bridgelabz.myfundooapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.myfundooapp.note_module.AddNoteActivity
import com.bridgelabz.myfundooapp.note_module.NoteDataManager
import kotlinx.android.synthetic.main.row.*
import kotlinx.android.synthetic.main.row.view.*

class NotesFragment : Fragment() {
    var listNotes = ArrayList<Note>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


//        var recyclerView= findViewById<RecyclerView>(R.id.recyclerview)
//        val adapter = ListAdapter(context!!)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = ConstraintLayout(context!!)
        return inflater.inflate(R.layout.fragment_notes, container, false)
//        LoadQuery("%")
    }

    private fun LoadQuery(title: String) {
        var dbManager = NoteDataManager(context!!)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                var ID = cursor.getInt(cursor.getColumnIndex("ID"))
                var Title = cursor.getString(cursor.getColumnIndex("Title"))
                var Description = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID, Title, Description))
            } while (cursor.moveToNext())
        }

        //adapter
        //val myNotesAdapter = MyNotesAdapter(this, listNotes)

        //set adapter
        // notesLV.adapter = MyNotesAdapter(context!!, listNotes)

        //get total number of task from list view
        // val total = notesLV.count

        //action bar
        //val mActionBar = supportActionBar

//        if (mActionBar != null) {
//            //set to actionbar as subtitle os actionbar
//            mActionBar.subtitle = "You have $total list(s) in the list"
//        }
    }

    inner class MyNotesAdapter : BaseAdapter {
        var listNotesAdapter = ArrayList<Note>()
        var context: Context? = null

        constructor(context: Context, listNotesAdapter: ArrayList<Note>) : super() {
            this.listNotesAdapter = listNotesAdapter
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //inflate layout row.xml
            var myView = layoutInflater.inflate(R.layout.row, null)
            var myNote = listNotesAdapter[position]
            myView.titleTV.text = myNote.nodeTitle
            myView.descTV.text = myNote.nodeDesc

            //delete button click
            deleteBtn.setOnClickListener {
                var dbManager =
                    NoteDataManager(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeId.toString())
                dbManager.delete("ID=?", selectionArgs)
                LoadQuery("%")
            }

            //edit,update button click
            myView.editBtn.setOnClickListener {
                GoToUpdateFunction(myNote)
            }

//            //copy button
//            myView.copyBtn.setOnClickListener {
//                //get title
//                val title = myView.titleTV.text.toString()
//                //get desciption
//                val desc = myView.descTV.text.toString()
//                //concatination
//                val s = title + "\n" + desc
//                val cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                cb.text = s // add to clipboard
//                Toast.makeText(context!!@DashboardActivity, "Copied....", Toast.LENGTH_SHORT).show()
//            }

            //share button click
            myView.shareBtn.setOnClickListener {
                //get title
                val title = myView.titleTV.text.toString()
                //get desciption
                val desc = myView.descTV.text.toString()
                //concatination
                val s = title + "\n" + desc
                //share intent
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))
            }
            return myView
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

    }

    private fun GoToUpdateFunction(myNote: Note) {
        var intent = Intent(context!!, AddNoteActivity::class.java)
        intent.putExtra("ID", myNote.nodeId) //put id
        intent.putExtra("Title", myNote.nodeTitle) //put title
        intent.putExtra("Description", myNote.nodeDesc) //put description
        startActivity(intent)
    }
}