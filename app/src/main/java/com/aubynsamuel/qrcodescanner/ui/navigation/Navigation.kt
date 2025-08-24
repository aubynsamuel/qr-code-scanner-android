package com.aubynsamuel.qrcodescanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aubynsamuel.qrcodescanner.ui.screen.ResultScreen
import com.aubynsamuel.qrcodescanner.ui.screen.ScannerScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScannerScreenRoute
    ) {
        composable<ScannerScreenRoute> {
            ScannerScreen { scannedData ->
                navController.navigate(ResultScreenRoute(scannedData))
            }
        }
        composable<ResultScreenRoute> {
            val args = it.toRoute<ResultScreenRoute>()
            val scannedData = args.scannedData
            ResultScreen(
                scannedData = scannedData,
                onBackToScanner = {
                    navController.popBackStack()
                }
            )
        }
    }
}