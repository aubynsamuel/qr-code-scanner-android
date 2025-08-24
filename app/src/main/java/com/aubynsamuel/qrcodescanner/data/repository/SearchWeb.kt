package com.aubynsamuel.qrcodescanner.data.repository

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun searchWeb(context: Context, query: String) {
    try {
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra("query", query)
        }
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        openUrl(context, "https://www.google.com/search?q=${Uri.encode(query)}")
    }
}