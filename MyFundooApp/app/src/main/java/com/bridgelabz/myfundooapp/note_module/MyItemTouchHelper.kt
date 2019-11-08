package com.bridgelabz.myfundooapp.note_module

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MyItemTouchHelper(
    itemTouchCallback: ItemTouchHelperAdapter
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or
            ItemTouchHelper.START or ItemTouchHelper.END
    , ItemTouchHelper.START or ItemTouchHelper.END
) {
    var touchHelperCallback = itemTouchCallback

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        touchHelperCallback.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        touchHelperCallback.onItemSwiped(viewHolder.adapterPosition)
    }
}
//
// ItemTouchHelper.Callback {
//
//    private var adapter : ItemTouchHelperAdapter
//
//    constructor(adapter: ItemTouchHelperAdapter) : super() {
//        this.adapter = adapter
//    }
//
//    override fun isLongPressDragEnabled(): Boolean {
//        return true
//    }
//
//    override fun isItemViewSwipeEnabled(): Boolean {
//        return true
//    }
//
//    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//        super.clearView(recyclerView, viewHolder)
//        viewHolder.itemView.setBackgroundColor(
//            ContextCompat.getColor(viewHolder.itemView.context, R.color.gray)
//        )
//    }
//
//    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
//        super.onSelectedChanged(viewHolder, actionState)
//        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG)
//        {
//            viewHolder?.itemView?.setBackgroundColor(
//                ContextCompat.getColor(viewHolder.itemView.context, R.color.gray)
//            )
//        }
//    }
//
//    override fun getMovementFlags(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder
//    ): Int {
//        val dragFlag: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
//        val swipeFlag : Int = ItemTouchHelper.START or ItemTouchHelper.END
//        return makeMovementFlags(dragFlag,swipeFlag)
//    }
//
//    override fun onMove(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        target: RecyclerView.ViewHolder
//    ): Boolean {
//        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
//        return true
//    }
//
//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        adapter.onItemSwiped(viewHolder.adapterPosition)
//    }
//
//}