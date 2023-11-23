package be.ap.padelpro.model

class Reservation(
    var time: String,
    var users: List<User> = listOf(),
    var field: Field
)
