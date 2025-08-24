package com.aubynsamuel.qrcodescanner.data.repository

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

fun callNumber(context: Context, phoneData: String) {
    try {
        val number = if (phoneData.startsWith("tel:")) {
            phoneData.substring(4)
        } else phoneData

        val intent = Intent(Intent.ACTION_DIAL, "tel:$number".toUri())
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "No phone app found", Toast.LENGTH_SHORT).show()
    }
}