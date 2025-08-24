package com.aubynsamuel.qrcodescanner.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.aubynsamuel.qrcodescanner.data.model.QRType
import com.aubynsamuel.qrcodescanner.data.repository.addContactFromVCard
import com.aubynsamuel.qrcodescanner.data.repository.addToContacts
import com.aubynsamuel.qrcodescanner.data.repository.callNumber
import com.aubynsamuel.qrcodescanner.data.repository.detectQRType
import com.aubynsamuel.qrcodescanner.data.repository.openUrl
import com.aubynsamuel.qrcodescanner.data.repository.searchWeb
import com.aubynsamuel.qrcodescanner.data.repository.sendEmail
import com.aubynsamuel.qrcodescanner.data.repository.sendSMS
import com.aubynsamuel.qrcodescanner.data.repository.shareText
import com.aubynsamuel.qrcodescanner.ui.components.ActionButton
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    scannedData: String,
    onBackToScanner: () -> Unit,
) {
    val context = LocalContext.current
    val decodedData = URLDecoder.decode(scannedData, "UTF-8")
    val qrType = detectQRType(decodedData)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Result") },
                navigationIcon = {
                    IconButton(onClick = onBackToScanner) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // QR Content Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Scanned Content:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = decodedData,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Type Detection Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Detected Type: ${qrType.displayName}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Button
            Text(
                text = "Available Actions:",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when (qrType) {
                QRType.URL -> {
                    ActionButton(
                        text = "Open in Browser",
                        icon = Icons.Default.Share,
                        onClick = { openUrl(context, decodedData) }
                    )
                }

                QRType.EMAIL -> {
                    ActionButton(
                        text = "Send Email",
                        icon = Icons.Default.Email,
                        onClick = { sendEmail(context, decodedData) }
                    )
                }

                QRType.PHONE -> {
                    ActionButton(
                        text = "Call Number",
                        icon = Icons.Default.Phone,
                        onClick = { callNumber(context, decodedData) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ActionButton(
                        text = "Add to Contacts",
                        icon = Icons.Default.Phone,
                        onClick = { addToContacts(context, decodedData) }
                    )
                }

                QRType.SMS -> {
                    ActionButton(
                        text = "Send SMS",
                        icon = Icons.Default.Phone,
                        onClick = { sendSMS(context, decodedData) }
                    )
                }

                QRType.VCARD -> {
                    ActionButton(
                        text = "Add Contact",
                        icon = Icons.Default.Phone,
                        onClick = { addContactFromVCard(context, decodedData) }
                    )
                }

                else -> {
                    // Generic actions for unknown types
                    ActionButton(
                        text = "Search on Web",
                        icon = Icons.Default.Share,
                        onClick = { searchWeb(context, decodedData) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Always show share option
            ActionButton(
                text = "Share Text",
                icon = Icons.Default.Share,
                onClick = { shareText(context, decodedData) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Scan Again Button
            Button(
                onClick = onBackToScanner,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Scan Another QR Code")
            }
        }
    }
}

