package edu.ap.padelpro.model

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import edu.ap.padelpro.R

class Datasource (var database: DatabaseReference){
    fun loadFields():List<Field>{
        database = Firebase.database.reference
        database.child("Fields").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        return listOf(
            Field(R.string.court1, R.drawable.image1),
            Field(R.string.court2, R.drawable.image2),
            Field(R.string.court3, R.drawable.image3),
            Field(R.string.court4, R.drawable.image4),
            Field(R.string.court5, R.drawable.image5),
            Field(R.string.court6, R.drawable.image6),
            Field(R.string.court7, R.drawable.image7),
            Field(R.string.court8, R.drawable.image8),
            Field(R.string.court9, R.drawable.image9),
            Field(R.string.court10, R.drawable.image10)
        )
    }
}
