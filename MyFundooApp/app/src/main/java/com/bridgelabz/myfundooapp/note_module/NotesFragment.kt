package com.bridgelabz.myfundooapp.note_module


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import androidx.recyclerview.widget.*
import com.bridgelabz.myfundooapp.R
import java.util.*

class NotesFragment : Fragment(), OnNoteListener, ItemTouchHelperAdapter {

    var listNotes = mutableListOf<Note>()
    var mrecyleView: RecyclerView? = null
    var listAdapter: ListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(
            R.layout.fragment_notes,
            container,
            false
        )
        findViews(rootView)
        LoadQuery("%")

        return rootView
    }

    private fun findViews(rootView: View) {
        mrecyleView = rootView.findViewById<RecyclerView>(R.id.recyclerview)
    }

    fun initRecyclerView() {
        mrecyleView!!.layoutManager = LinearLayoutManager(context!!)
        listAdapter = ListAdapter(context!!, listNotes, this)

        val callback: ItemTouchHelper.Callback = MyItemTouchHelper(this)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(mrecyleView)
        mrecyleView!!.adapter = listAdapter
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(listNotes, fromPosition, toPosition)
        listAdapter!!.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSwiped(position: Int) {
        listNotes.remove(listNotes[position])
        listAdapter!!.notifyItemRemoved(position)
    }

    fun LoadQuery(title: String) {
        val dbManager = NoteDataManager(context!!)
        val projections = arrayOf("ID", "Title", "Description", "Date","Color")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(
            projections, "Title like ?", selectionArgs,
            "Title"
        )
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))
                val Date = cursor.getString(cursor.getColumnIndex("Date"))
                var Color = cursor.getInt(cursor.getColumnIndex("Color"))
                listNotes.add(Note(ID, Title, Description, Date,Color))
            } while (cursor.moveToNext())
        }
        initRecyclerView()
    }

    override fun onNoteClick(position: Int) {
        val intent = Intent(context, AddNoteActivity::class.java)
        //intent.putExtra("selected_note", (listNotes[position]))
        startActivity(intent)
    }
}