package com.android.course.animations.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.course.animations.R
import com.android.course.animations.model.PhoneContact

class ContactsRecyclerView :
    RecyclerView.Adapter<ContactsRecyclerView.ContactsRecyclerViewHolder>() {

    var phoneContacts: List<PhoneContact> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsRecyclerViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)

        return ContactsRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = phoneContacts.size

    override fun onBindViewHolder(holder: ContactsRecyclerViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setData(newList: List<PhoneContact>) {
        val diffUtil = ConverterDiffUtil(phoneContacts, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil, true)
        diffResult.dispatchUpdatesTo(this)
        phoneContacts = newList
    }

    fun onSwipe(viewHolder: RecyclerView.ViewHolder) {
        val newList = phoneContacts.toMutableList()
        newList.removeAt(viewHolder.bindingAdapterPosition)
        setData(newList)
    }

    fun onMove(fromPos: Int, toPos: Int) {
        val newList = phoneContacts.toMutableList()
        val element = newList.removeAt(fromPos)
        newList.add(toPos, element)
        setData(newList)
    }

    inner class ContactsRecyclerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView by lazy { itemView.findViewById(R.id.name) }
        private val phoneTextView: TextView by lazy { itemView.findViewById(R.id.phone) }
        private val photoImageView: ImageView by lazy { itemView.findViewById(R.id.image) }

        fun bind(position: Int) {
            nameTextView.text = phoneContacts[position].name
            phoneTextView.text = phoneContacts[position].phoneNumber
            phoneContacts[position].contactPhoto?.let {
                photoImageView.setImageBitmap(phoneContacts[position].contactPhoto)
            }
        }
    }

    private class ConverterDiffUtil(
        private val oldList: List<PhoneContact>,
        private val newList: List<PhoneContact>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].name == newList[newItemPosition].name

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].name == newList[newItemPosition].name &&
                    oldList[oldItemPosition].phoneNumber == newList[newItemPosition].phoneNumber
    }
}