package com.example.jelajapedi.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jelajapedi.ui.viewmodel.PaymentState
import com.example.jelajapedi.ui.viewmodel.PaymentViewModel

@Composable
fun PaymentScreen(
    bookingId: String,
    totalAmount: Double,
    onPaymentSuccess: (String) -> Unit,
    viewModel: PaymentViewModel = viewModel()
) {
    val paymentState by viewModel.paymentState.collectAsState()
    val selectedMethod by viewModel.selectedPaymentMethod.collectAsState()
    
    var showConfirm by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Checkout & Pembayaran",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Total Amount Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total Pembayaran")
                Text(
                    "Rp ${totalAmount.toLong()}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Payment Methods
        Text(
            "Pilih Metode Pembayaran",
            style = MaterialTheme.typography.titleMedium
        )
        
        listOf(
            "Kartu Kredit",
            "Kartu Debit",
            "E-Wallet",
            "Transfer Bank"
        ).forEach { method ->
            PaymentMethodCard(
                method = method,
                isSelected = selectedMethod == method,
                onClick = { viewModel.selectPaymentMethod(method) }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Confirm Button
        Button(
            onClick = { showConfirm = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedMethod.isNotEmpty() && paymentState !is PaymentState.Loading
        ) {
            Text("Lanjutkan ke Pembayaran")
        }
        
        // Show confirmation dialog
        if (showConfirm) {
            PaymentConfirmDialog(
                method = selectedMethod,
                amount = totalAmount,
                onConfirm = {
                    viewModel.processPayment(bookingId, totalAmount, selectedMethod)
                    showConfirm = false
                },
                onDismiss = { showConfirm = false }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Handle state
        when (paymentState) {
            is PaymentState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is PaymentState.Success -> {
                val success = paymentState as PaymentState.Success
                onPaymentSuccess(success.orderId)
            }
            is PaymentState.Error -> {
                Text(
                    "Error: ${(paymentState as PaymentState.Error).message}",
                    color = Color.Red
                )
            }
            else -> {}
        }
    }
}

@Composable
fun PaymentMethodCard(
    method: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(method)
            if (isSelected) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Selected",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun PaymentConfirmDialog(
    method: String,
    amount: Double,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Konfirmasi Pembayaran") },
        text = {
            Column {
                Text("Metode: $method")
                Text("Jumlah: Rp ${amount.toLong()}")
                Text("Lanjutkan pembayaran?")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text("Bayar") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Batal") }
        }
    )
}
File 7: ETicketScreen.kt
kotlinpackage com.example.jelajapedi.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jelajapedi.data.model.ETicket
import com.example.jelajapedi.ui.viewmodel.ETicketState
import com.example.jelajapedi.ui.viewmodel.ETicketViewModel

@Composable
fun ETicketScreen(
    viewModel: ETicketViewModel = viewModel()
) {
    val eTicketState by viewModel.eTicketState.collectAsState()
    val ticketList by viewModel.eTicketList.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "E-Tiket Digital",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (eTicketState) {
            is ETicketState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is ETicketState.Success -> {
                val ticket = (eTicketState as ETicketState.Success).ticket
                ETicketCard(ticket)
            }
            is ETicketState.Error -> {
                Text(
                    "Error: ${(eTicketState as ETicketState.Error).message}",
                    color = Color.Red
                )
            }
            else -> {
                Text(
                    "Belum ada tiket",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ETicketCard(ticket: ETicket) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nomor Tiket: ${ticket.ticketNumber}")
            Text("Penumpang: ${ticket.passengerName}")
            Text("Destinasi: ${ticket.destinationName}")
            Text("Tanggal: ${ticket.departureDate}")
            Text("Jam: ${ticket.departureTime}")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // QR Code Display
            Text(
                "QR Code",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                ticket.qrCode,
                style = MaterialTheme.typography.bodySmall
            )
            // TODO: Display actual QR Code image using library seperti ZXing
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* Download */ }) {
                    Text("Download")
                }
                Button(onClick = { /* Share */ }) {
                    Text("Bagikan")
                }
            }
        }
    }
}
