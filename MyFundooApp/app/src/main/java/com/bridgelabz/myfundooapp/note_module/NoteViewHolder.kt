package com.bridgelabz.myfundooapp.note_module

import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.myfundooapp.R
import kotlinx.android.synthetic.main.row.view.*

class NoteViewHolder : RecyclerView.ViewHolder, View.OnTouchListener,
    GestureDetector.OnGestureListener {

    var noteItemViewTitle: TextView
    var noteItemViewDesciption: TextView
    var noteItemViewDate : TextView
    var gestureDetector: GestureDetector
    var showDate : TextView
    var deleteBtn: ImageButton
    var editBtn: ImageButton
    var copyBtn: ImageButton
    var shareBtn: ImageButton
    var itemTouchHelper: ItemTouchHelper? = null
    var cardView : CardView
    private var onNoteListener : OnNoteListener


    constructor(itemView: View,onNoteListener : OnNoteListener) : super(itemView) {

        noteItemViewTitle = itemView.findViewById(R.id.titleTV)
        noteItemViewDesciption = itemView.findViewById(R.id.descTV)
        noteItemViewDate = itemView.findViewById(R.id.showDate)
        cardView = itemView.findViewById(R.id.row_card)
        gestureDetector = GestureDetector(itemView.context, this)
        this.onNoteListener = onNoteListener
        //this.itemTouchHelper = ItemTouchHelper()
        showDate = itemView.showDate
        deleteBtn = itemView.deleteBtn
        editBtn = itemView.editBtn
        copyBtn = itemView.copyBtn
        shareBtn = itemView.shareBtn

        itemView.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.d("prachi", "onShowPress: $e")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        onNoteListener.onNoteClick(getAdapterPosition())
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        itemTouchHelper?.startDrag(this)
    }

}