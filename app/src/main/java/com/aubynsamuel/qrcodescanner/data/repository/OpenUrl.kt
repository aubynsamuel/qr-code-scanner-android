package com.aubynsamuel.qrcodescanner.data.repository

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

fun openUrl(context: Context, url: String) {
    try {
        val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else url

        val intent = Intent(Intent.ACTION_VIEW, formattedUrl.toUri())
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "No browser found to open URL", Toast.LENGTH_SHORT).show()
    }
}