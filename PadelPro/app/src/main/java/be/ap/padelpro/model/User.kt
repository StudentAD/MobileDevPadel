package be.ap.padelpro.model

class User(
    var firstname: String,
    var lastname: String,
    var reservation: List<Reservation> = listOf(),
)