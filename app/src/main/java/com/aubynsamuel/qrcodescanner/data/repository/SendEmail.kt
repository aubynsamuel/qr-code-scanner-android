package com.aubynsamuel.qrcodescanner.data.repository

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

fun sendEmail(context: Context, emailData: String) {
    try {
        val email = if (emailData.startsWith("mailto:")) {
            emailData.substring(7)
        } else emailData

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$email".toUri()
        }
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
    }
}