package com.aubynsamuel.qrcodescanner.data.repository

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

fun sendSMS(context: Context, smsData: String) {
    try {
        val intent = if (smsData.startsWith("sms:") || smsData.startsWith("smsto:")) {
            Intent(Intent.ACTION_SENDTO, smsData.toUri())
        } else {
            Intent(Intent.ACTION_SENDTO, "smsto:$smsData".toUri())
        }
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "No SMS app found", Toast.LENGTH_SHORT).show()
    }
}