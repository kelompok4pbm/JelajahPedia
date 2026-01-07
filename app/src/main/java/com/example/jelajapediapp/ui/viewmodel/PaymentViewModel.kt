package com.example.jelajapedi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jelajapedi.data.model.Payment
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {
    
    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState
    
    private val _selectedPaymentMethod = MutableStateFlow("")
    val selectedPaymentMethod: StateFlow<String> = _selectedPaymentMethod
    
    fun selectPaymentMethod(method: String) {
        _selectedPaymentMethod.value = method
    }
    
    fun processPayment(bookingId: String, amount: Double, method: String) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading
            try {
                delay(2000) 
                _paymentState.value = PaymentState.Success(
                    orderId = "ORD-${System.currentTimeMillis()}",
                    amount = amount
                )
            } catch (e: Exception) {
                _paymentState.value = PaymentState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class Success(val orderId: String, val amount: Double) : PaymentState()
    data class Error(val message: String) : PaymentState()
}
