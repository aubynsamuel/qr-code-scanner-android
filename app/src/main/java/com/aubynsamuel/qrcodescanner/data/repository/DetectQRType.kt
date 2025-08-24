package com.aubynsamuel.qrcodescanner.data.repository

import com.aubynsamuel.qrcodescanner.data.model.QRType

fun detectQRType(data: String): QRType {
    return when {
        data.startsWith("http://", ignoreCase = true) ||
                data.startsWith("https://", ignoreCase = true) -> QRType.URL

        data.startsWith("mailto:", ignoreCase = true) -> QRType.EMAIL

        data.startsWith("tel:", ignoreCase = true) ||
                data.matches(Regex("^[+]?[0-9\\-\\s()]+$")) -> QRType.PHONE

        data.startsWith("sms:", ignoreCase = true) ||
                data.startsWith("smsto:", ignoreCase = true) -> QRType.SMS

        data.startsWith("BEGIN:VCARD", ignoreCase = true) -> QRType.VCARD

        data.startsWith("WIFI:", ignoreCase = true) -> QRType.WIFI

        data.contains("@") && data.contains(".") -> QRType.EMAIL

        else -> QRType.TEXT
    }
}