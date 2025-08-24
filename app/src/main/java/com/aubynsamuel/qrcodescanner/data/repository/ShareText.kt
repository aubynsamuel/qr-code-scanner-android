package com.aubynsamuel.qrcodescanner.data.repository

import android.content.Context
import android.content.Intent

fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share QR Code Content"))
}