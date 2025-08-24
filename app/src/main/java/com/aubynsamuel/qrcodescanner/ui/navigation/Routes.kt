package com.aubynsamuel.qrcodescanner.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Scanner

@Serializable
data class ResultScreen(val scannedData: String)

@Serializable
object QRScannerScreen