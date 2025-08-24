package com.aubynsamuel.qrcodescanner.data.repository

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.widget.Toast

fun addToContacts(context: Context, phoneNumber: String) {
    try {
        val number = if (phoneNumber.startsWith("tel:")) {
            phoneNumber.substring(4)
        } else phoneNumber

        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, number)
        }
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Cannot add to contacts", Toast.LENGTH_SHORT).show()
    }
}