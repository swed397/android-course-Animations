package com.android.course.animations.repo

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import com.android.course.animations.model.PhoneContact
import java.io.ByteArrayInputStream
import java.io.InputStream

class ContactsRepository(private val contentResolver: ContentResolver) {

    @SuppressLint("Range")
    fun getContacts(): List<PhoneContact> {
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val phoneContacts = mutableListOf<PhoneContact>()
        val cursor = contentResolver.query(phoneUri, null, null, null, null) ?: return phoneContacts

        return cursor.use {
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
                            contactPhoto = BitmapFactory.decodeStream(photoIS)
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
        cursor.use {
            if (cursor.moveToFirst()) {
                val data = cursor.getBlob(0)
                if (data != null) {
                    return ByteArrayInputStream(data)
                }
            }
        }
        return null
    }
}