package com.aubynsamuel.qrcodescanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aubynsamuel.qrcodescanner.ui.screen.QRScannerScreen
import com.aubynsamuel.qrcodescanner.ui.screen.ResultScreen
import com.aubynsamuel.qrcodescanner.ui.screen.ScannerScreen
import java.util.Scanner

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Scanner
    ) {
        composable<Scanner> {
            ScannerScreen { scannedData ->
                navController.navigate(ResultScreen(scannedData))
            }
        }
        composable<ResultScreen> {
            val args = it.toRoute<ResultScreen>()
            val scannedData = args.scannedData
            ResultScreen(
                scannedData = scannedData,
                onBackToScanner = {
                    navController.popBackStack()
                }
            )
        }
        composable<QRScannerScreen> {
            QRScannerScreen(onQrCodeScanned = { scannedData ->
                navController.navigate(ResultScreen(scannedData))
            })
        }
    }
}