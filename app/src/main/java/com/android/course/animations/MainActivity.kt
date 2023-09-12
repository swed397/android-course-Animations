package com.android.course.animations

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.course.animations.adapters.ContactsRecyclerView
import com.android.course.animations.model.PhoneContact
import com.android.course.animations.repo.ContactsRepository


class MainActivity : AppCompatActivity() {

    private lateinit var phoneContacts: List<PhoneContact>
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactsRecyclerViewAdapter: ContactsRecyclerView
    private val contactsRepo: ContactsRepository by lazy { ContactsRepository(contentResolver) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()
        phoneContacts = contactsRepo.getContacts()
        setComponents()
    }

    private fun setComponents() {
        contactsRecyclerViewAdapter = ContactsRecyclerView(phoneContacts)
        recyclerView = findViewById(R.id.main_recycler)
        recyclerView.adapter = contactsRecyclerViewAdapter
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                listOf(Manifest.permission.READ_CONTACTS).toTypedArray(), 0
            )
        }
    }
}