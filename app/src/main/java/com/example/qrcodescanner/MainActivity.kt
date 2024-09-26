package com.example.qrcodescanner

import android.Manifest
import android.content.pm.PackageManager
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class MainActivity : AppCompatActivity() {
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var resultText: TextView
    private val CAMERA_PERMISSION_REQUEST = 101
    private var lastScannedText = ""
    private var isFlashOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        barcodeView = findViewById(R.id.barcode_scanner)
        resultText = findViewById(R.id.result_text)

        // Check if the device has a camera
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera detected on this device", Toast.LENGTH_LONG).show()
            finish() // Exit the app if no camera is found
        }

        // Request camera permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermissionWithRationale()
        } else {
            startScanning()
        }

        // Copy scanned result to clipboard
        resultText.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Copied Text", resultText.text)
            clipboard.setPrimaryClip(clip)
//            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestCameraPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            AlertDialog.Builder(this)
                .setTitle("Camera Permission Required")
                .setMessage("This app needs camera access to scan QR codes. Please grant the permission.")
                .setPositiveButton("OK") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_REQUEST
                    )
                }
                .setNegativeButton("Cancel") { _, _ -> finish() }
                .create()
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }
    }

    private fun startScanning() {
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                val scannedText = result.text
                if (scannedText == lastScannedText) {
                    // Avoid processing the same scan result multiple times
                    return
                }
                lastScannedText = scannedText
                resultText.text = "$scannedText"

//                when {
//                    scannedText.startsWith("http") -> {
//                        resultText.text = "URL: $scannedText"
//                        // You could add logic to open the URL in a browser
//                    }
//                    scannedText.matches(Regex("^[0-9]+$")) -> {
//                        resultText.text = "Phone Number: $scannedText"
//                        // You could add logic to dial the phone number
//                    }
//                    else -> {
//                        resultText.text = "Text: $scannedText"
//                    }
//                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        })
    }

    private fun toggleFlashlight() {
        isFlashOn = if (isFlashOn) {
            barcodeView.setTorchOff()
            false
        } else {
            barcodeView.setTorchOn()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning()
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
