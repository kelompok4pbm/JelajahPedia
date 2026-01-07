data class ETicket(
    val ticketId: String,
    val bookingId: String,
    val passengerName: String,
    val destinationName: String,
    val departureDate: String,
    val departureTime: String,
    val qrCode: String, // URL atau encoded data QR Code
    val ticketStatus: String, // "VALID", "USED", "EXPIRED"
    val ticketNumber: String,
    val seatNumber: String?,
    val generatedDate: String
)
