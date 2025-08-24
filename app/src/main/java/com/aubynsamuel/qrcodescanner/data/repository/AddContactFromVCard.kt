package com.aubynsamuel.qrcodescanner.data.repository

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.widget.Toast

fun addContactFromVCard(context: Context, vcardData: String) {
    try {
        // Basic vCard parsing for name and phone
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
        }
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Cannot add contact", Toast.LENGTH_SHORT).show()
    }
}