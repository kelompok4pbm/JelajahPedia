package com.example.jelajapedi.ui.screen

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
