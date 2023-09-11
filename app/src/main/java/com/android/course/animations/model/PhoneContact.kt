package com.android.course.animations.model

import java.io.InputStream

data class PhoneContact(
    val name: String,
    val phoneNumber: String,
    val contactPhoto: InputStream?
)
