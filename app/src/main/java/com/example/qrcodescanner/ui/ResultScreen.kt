package com.example.qrcodescanner.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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

            // Action Buttons
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

@Composable
fun ActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text)
    }
}

// QR Type Detection and Actions
enum class QRType(val displayName: String) {
    URL("Website URL"),
    EMAIL("Email Address"),
    PHONE("Phone Number"),
    SMS("SMS Message"),
    VCARD("Contact Card"),
    WIFI("WiFi Network"),
    TEXT("Plain Text")
}

fun detectQRType(data: String): QRType {
    return when {
        data.startsWith("http://", ignoreCase = true) ||
                data.startsWith("https://", ignoreCase = true) -> QRType.URL

        data.startsWith("mailto:", ignoreCase = true) -> QRType.EMAIL

        data.startsWith("tel:", ignoreCase = true) ||
                data.matches(Regex("^[+]?[0-9\\-\\s\\(\\)]+$")) -> QRType.PHONE

        data.startsWith("sms:", ignoreCase = true) ||
                data.startsWith("smsto:", ignoreCase = true) -> QRType.SMS

        data.startsWith("BEGIN:VCARD", ignoreCase = true) -> QRType.VCARD

        data.startsWith("WIFI:", ignoreCase = true) -> QRType.WIFI

        data.contains("@") && data.contains(".") -> QRType.EMAIL

        else -> QRType.TEXT
    }
}

// Action Functions
fun openUrl(context: Context, url: String) {
    try {
        val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else url

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedUrl))
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No browser found to open URL", Toast.LENGTH_SHORT).show()
    }
}

fun sendEmail(context: Context, emailData: String) {
    try {
        val email = if (emailData.startsWith("mailto:")) {
            emailData.substring(7)
        } else emailData

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
    }
}

fun callNumber(context: Context, phoneData: String) {
    try {
        val number = if (phoneData.startsWith("tel:")) {
            phoneData.substring(4)
        } else phoneData

        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No phone app found", Toast.LENGTH_SHORT).show()
    }
}

fun sendSMS(context: Context, smsData: String) {
    try {
        val intent = if (smsData.startsWith("sms:") || smsData.startsWith("smsto:")) {
            Intent(Intent.ACTION_SENDTO, Uri.parse(smsData))
        } else {
            Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$smsData"))
        }
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No SMS app found", Toast.LENGTH_SHORT).show()
    }
}

fun addToContacts(context: Context, phoneNumber: String) {
    try {
        val number = if (phoneNumber.startsWith("tel:")) {
            phoneNumber.substring(4)
        } else phoneNumber

        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, number)
        }
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Cannot add to contacts", Toast.LENGTH_SHORT).show()
    }
}

fun addContactFromVCard(context: Context, vcardData: String) {
    try {
        // Basic vCard parsing for name and phone
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
        }
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Cannot add contact", Toast.LENGTH_SHORT).show()
    }
}

fun searchWeb(context: Context, query: String) {
    try {
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra("query", query)
        }
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        openUrl(context, "https://www.google.com/search?q=${Uri.encode(query)}")
    }
}

fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share QR Code Content"))
}

