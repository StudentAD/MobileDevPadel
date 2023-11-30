package edu.ap.padelpro.model

import android.util.Log

import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.ap.padelpro.R

class Datasource (var database: DatabaseReference){
    fun loadFields():List<Field>{
        // Assuming you have already initialized FirebaseApp and Firestore
        val firestore = FirebaseFirestore.getInstance()

      val list=  firestore.collection("Fields").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.i("firestore", "Got value ${document.id}: ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("firestore", "Error getting data", exception)
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
