package com.android.course.animations.adapters

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.let {
                it.itemView
                    .animate()
                    .alpha(ALPHA_MIN_DRAG)
                    .scaleX(SCALE_UP)
                    .scaleY(SCALE_UP)
                    .start()
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val targetX = viewHolder.itemView.width.toFloat() / 2
            val translationX = abs(if (dX > 0) min(dX, targetX) else max(dX, -targetX))
            val ratio = translationX / targetX
            val alpha = ALPHA_NORMAL - ratio
            val resultAlpha = max(alpha, ALPHA_MIN_SWIPE)
            viewHolder.itemView.alpha = resultAlpha
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        viewHolder.itemView
            .animate()
            .alpha(ALPHA_NORMAL)
            .scaleX(SCALE_NORMAL)
            .scaleY(SCALE_NORMAL)
            .start()
    }

    private companion object {
        const val ALPHA_MIN_DRAG = 0.5f
        const val ALPHA_MIN_SWIPE = 0.2f
        const val ALPHA_NORMAL = 1f

        const val SCALE_UP = 1.2f
        const val SCALE_NORMAL = 1f
    }
}