package com.android.course.animations

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.course.animations.model.PhoneContact
import java.io.ByteArrayInputStream
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    private lateinit var phoneContacts: MutableList<PhoneContact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getContacts()
        println(phoneContacts)

        //ToDo IS -> ImageView
//        val bitmap = BitmapFactory.decodeStream(phoneContacts[0].contactPhoto)
    }

    @SuppressLint("Range")
    private fun getContacts() {
        checkPermissions()
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        phoneContacts = mutableListOf()
        val cursor = contentResolver.query(phoneUri, null, null, null, null) ?: return

        phoneContacts = cursor.use {
            if (cursor.count >= 0) {
                while (cursor.moveToNext()) {
                    val id =
                        cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.CONTACT_ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val photoIS = getContactPhotoIS(id)
                    phoneContacts.add(
                        PhoneContact(
                            name = name,
                            phoneNumber = phoneNumber,
                            contactPhoto = photoIS
                        )
                    )
                }
            }
            phoneContacts
        }
    }

    private fun getContactPhotoIS(contactId: Long): InputStream? {
        val contactUri =
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        val photoUri =
            Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
        val cursor =
            contentResolver.query(
                photoUri,
                arrayOf(ContactsContract.Contacts.Photo.PHOTO),
                null,
                null,
                null
            )
                ?: return null
        cursor.use { cursor ->
            if (cursor.moveToFirst()) {
                val data = cursor.getBlob(0)
                if (data != null) {
                    return ByteArrayInputStream(data)
                }
            }
        }
        return null
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