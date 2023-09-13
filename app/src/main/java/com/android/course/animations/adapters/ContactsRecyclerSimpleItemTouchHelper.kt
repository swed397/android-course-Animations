package com.android.course.animations.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ContactsRecyclerSimpleItemTouchHelper(
    private val onMove: (fromPos: Int, toPos: Int) -> Unit,
    private val onSwipe: (viewHolder: RecyclerView.ViewHolder) -> Unit
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.LEFT
) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        onMove.invoke(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipe.invoke(viewHolder)
    }
}