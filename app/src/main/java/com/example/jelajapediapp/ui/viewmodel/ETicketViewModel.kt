package com.example.jelajapedi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jelajapedi.data.model.ETicket
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ETicketViewModel : ViewModel() {
    
    private val _eTicketState = MutableStateFlow<ETicketState>(ETicketState.Idle)
    val eTicketState: StateFlow<ETicketState> = _eTicketState
    
    private val _eTicketList = MutableStateFlow<List<ETicket>>(emptyList())
    val eTicketList: StateFlow<List<ETicket>> = _eTicketList
    
    fun generateETicket(
        bookingId: String,
        passengerName: String,
        destination: String,
        departureDate: String,
        departureTime: String
    ) {
        viewModelScope.launch {
            _eTicketState.value = ETicketState.Loading
            try {
                delay(1500) 
                val newTicket = ETicket(
                    ticketId = "TKT-${System.currentTimeMillis()}",
                    bookingId = bookingId,
                    passengerName = passengerName,
                    destinationName = destination,
                    departureDate = departureDate,
                    departureTime = departureTime,
                    qrCode = generateQRCode(bookingId), // TODO: Implementasi QR
                    ticketStatus = "VALID",
                    ticketNumber = "TKT-2024-${System.currentTimeMillis()}",
                    seatNumber = null,
                    generatedDate = getCurrentDate()
                )
                _eTicketState.value = ETicketState.Success(newTicket)
            } catch (e: Exception) {
                _eTicketState.value = ETicketState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun fetchMyTickets(userId: String) {
        viewModelScope.launch {
            try {
                // TODO: Fetch dari database
                delay(1000)
                _eTicketList.value = emptyList()
            } catch (e: Exception) {
                _eTicketState.value = ETicketState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    private fun generateQRCode(data: String): String {
        // TODO: Implementasi QR Code generation 
        return "qr_$data"
    }
    
    private fun getCurrentDate(): String = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).format(Date())
}

sealed class ETicketState {
    object Idle : ETicketState()
    object Loading : ETicketState()
    data class Success(val ticket: ETicket) : ETicketState()
    data class Error(val message: String) : ETicketState()
}
