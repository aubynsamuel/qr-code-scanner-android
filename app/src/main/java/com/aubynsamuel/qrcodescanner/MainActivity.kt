package com.aubynsamuel.qrcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aubynsamuel.qrcodescanner.ui.navigation.Navigation
import com.aubynsamuel.qrcodescanner.ui.theme.QRScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRScannerTheme {
                Navigation()
            }
        }
    }
}