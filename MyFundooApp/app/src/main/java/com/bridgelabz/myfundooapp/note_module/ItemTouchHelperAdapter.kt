package com.bridgelabz.myfundooapp.note_module

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition : Int, toPosition : Int)
    fun onItemSwiped(position : Int)
}