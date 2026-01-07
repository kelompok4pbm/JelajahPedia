data class Payment(
    val id: String,
    val bookingId: String,
    val amount: Double,
    val paymentMethod: String, // "CREDIT_CARD", "DEBIT_CARD", "E_WALLET", "BANK_TRANSFER"
    val status: String, // "PENDING", "SUCCESS", "FAILED"
    val transactionDate: String,
    val orderNumber: String
)
