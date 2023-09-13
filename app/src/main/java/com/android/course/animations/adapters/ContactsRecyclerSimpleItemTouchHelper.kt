package com.android.course.animations.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.course.animations.model.PhoneContact
import java.util.Collections

class ContactsRecyclerSimpleItemTouchHelper(
    private val data: MutableList<PhoneContact>,
) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.DOWN or ItemTouchHelper.UP,
        ItemTouchHelper.LEFT
    ) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.bindingAdapterPosition
        val toPosition = target.bindingAdapterPosition
        Collections.swap(data, fromPosition, toPosition)
        recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        data.remove(data[viewHolder.bindingAdapterPosition])
        (viewHolder.bindingAdapter as ContactsRecyclerView).setData(data)
    }
}