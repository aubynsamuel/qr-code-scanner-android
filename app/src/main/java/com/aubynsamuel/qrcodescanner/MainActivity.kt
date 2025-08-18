package com.aubynsamuel.qrcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aubynsamuel.qrcodescanner.ui.ResultScreen
import com.aubynsamuel.qrcodescanner.ui.ScannerScreen
import com.aubynsamuel.qrcodescanner.ui.theme.QRScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRScannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "scanner"
                    ) {
                        composable("scanner") {
                            ScannerScreen(
                                onQrCodeScanned = { scannedData ->
                                    navController.navigate("result/$scannedData")
                                }
                            )
                        }
                        composable("result/{scannedData}") { backStackEntry ->
                            val scannedData =
                                backStackEntry.arguments?.getString("scannedData") ?: ""
                            ResultScreen(
                                scannedData = scannedData,
                                onBackToScanner = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}