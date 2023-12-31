package edu.ap.padelpro.model

import androidx.annotation.DrawableRes
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Field(
    val document: QueryDocumentSnapshot,
    @DrawableRes var imageResourceId: Int
){

    var hours = mutableListOf<String>(
        "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00"
    )

}