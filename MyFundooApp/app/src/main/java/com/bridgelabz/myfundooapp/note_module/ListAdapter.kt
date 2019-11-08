package com.bridgelabz.myfundooapp.note_module

import android.app.AlertDialog
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.myfundooapp.R


class ListAdapter : RecyclerView.Adapter<NoteViewHolder>, Filterable {

    private var notes = mutableListOf<Note>() // Cached copy of notes
    private var notesFull = mutableListOf<Note>()
    private var context: Context
    private var onNoteListener: OnNoteListener

    constructor(
        context: Context,
        notes: MutableList<Note>,
        onNoteListener: OnNoteListener
    ) : super() {
        this.context = context
        this.notes = notes
        this.onNoteListener = onNoteListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.row,
            parent, false
        )
        return NoteViewHolder(itemView, onNoteListener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        holder.noteItemViewTitle.text = current.nodeTitle
        holder.noteItemViewDesciption.text = current.nodeDesc
        holder.noteItemViewDate.text = current.nodeDate
        holder.cardView.setCardBackgroundColor(current.nodeColor)

        holder.deleteBtn.setOnClickListener {
            val title = current.nodeTitle
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("warning")
                .setMessage("Are you sure you want to delete $title?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    val dbManager = NoteDataManager(this.context!!)
                    val selectionArgs = arrayOf(current.nodeId.toString())
                    dbManager.delete("ID=?", selectionArgs)
                    LoadQuery("%")
                    if (dbManager.delete("ID=?", selectionArgs) == 0) {
                        notes.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, notes.size)
                        Toast.makeText(context, "Title $title is deleted", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "Error while deleting", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
                .setIcon(R.drawable.ic_action_warning)
                .show()
        }

        holder.editBtn.setOnClickListener {
            GoToUpdateFunction(current)
        }

        holder.shareBtn.setOnClickListener {
            //get title
            val title = holder.noteItemViewTitle.text.toString()
            //get desciption
            val desc = holder.noteItemViewDesciption.text.toString()
            //concatination
            val s = title + "\n" + desc
            //share intent
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, s)
            startActivity(context, Intent.createChooser(shareIntent, s), Bundle())
        }

        holder.copyBtn.setOnClickListener {
            var clipboardManager: ClipboardManager? = null
            //get title
            val title = holder.noteItemViewTitle.text.trim()
            //get desciption
            val desc = holder.noteItemViewDesciption.text.trim()
            val text = "$title + $desc"

            val clipData = ClipData.newPlainText("text", text)
            clipboardManager?.setPrimaryClip(clipData)
            Log.d("copy", "$clipData")
            Toast.makeText(context, "Copied....", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = notes.size

    fun LoadQuery(title: String) {
        var dbManager = NoteDataManager(context!!)
        val projections = arrayOf("ID", "Title", "Description", "Date","Color")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(
            projections, "Title like ?", selectionArgs,
            "Title"
        )
        notes.clear()
        if (cursor.moveToFirst()) {
            do {
                var ID = cursor.getInt(cursor.getColumnIndex("ID"))
                var Title = cursor.getString(cursor.getColumnIndex("Title"))
                var Description = cursor.getString(cursor.getColumnIndex("Description"))
                var Date = cursor.getString(cursor.getColumnIndex("Date"))
                var Color = cursor.getInt(cursor.getColumnIndex("Color"))
                notes.add(Note(ID, Title, Description, Date, Color))
                //notesFull.add(Note(ID, Title, Description, Date))
            } while (cursor.moveToNext())
        }
    }

    private fun GoToUpdateFunction(notes: Note) {
        var intent = Intent(context, AddNoteActivity::class.java)
        intent.putExtra("ID", notes.nodeId) //put id
        intent.putExtra("Title", notes.nodeTitle) //put title
        intent.putExtra("Description", notes.nodeDesc) //put description
        intent.putExtra("Date", notes.nodeDate)
        startActivity(context, intent, Bundle())
    }

    private val exampleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Note>()
            val results = FilterResults()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(notesFull)
                results.values = filteredList
                return results
            }
            filterList(constraint, filteredList)
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notes.clear()
            notes.addAll(results!!.values as List<Note>)
            notifyDataSetChanged()
        }
    }

    private fun filterList(constraint: CharSequence, filteredList: MutableList<Note>) {
        val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }

        for (item in notesFull) {
            if (item.nodeTitle.toLowerCase().contains(filterPattern)) {
                filteredList.add(item)
            }
        }
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }
}

