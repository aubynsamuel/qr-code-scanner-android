package com.aubynsamuel.qrcodescanner.data.model

enum class QRType(val displayName: String) {
    URL("Website URL"),
    EMAIL("Email Address"),
    PHONE("Phone Number"),
    SMS("SMS Message"),
    VCARD("Contact Card"),
    WIFI("WiFi Network"),
    TEXT("Plain Text")
}