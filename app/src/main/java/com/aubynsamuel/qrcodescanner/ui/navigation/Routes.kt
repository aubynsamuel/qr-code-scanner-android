package com.aubynsamuel.qrcodescanner.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object ScannerScreenRoute

@Serializable
data class ResultScreenRoute(val scannedData: String)

@Serializable
object QRScannerScreen