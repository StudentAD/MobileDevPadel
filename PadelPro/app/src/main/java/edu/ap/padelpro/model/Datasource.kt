package edu.ap.padelpro.model

import android.util.Log

import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.ap.padelpro.R

class Datasource(var database: DatabaseReference) {
    fun loadFields(fieldsDetailsCallback: FieldsDetailsCallback) {
        // Assuming you have already initialized FirebaseApp and Firestore
        val firestore = FirebaseFirestore.getInstance()

        val mList: ArrayList<Field> = ArrayList()
        val list = firestore.collection("Fields").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    Log.i("firestore", "Got value ${document.id}: ${document.data}")
                    mList.add(Field(document,R.drawable.image1))
                }
                mList[0].imageResourceId = R.drawable.image1
                mList[1].imageResourceId = R.drawable.image2
                mList[2].imageResourceId = R.drawable.image3
                mList[3].imageResourceId = R.drawable.image4
                mList[4].imageResourceId = R.drawable.image5
                mList[5].imageResourceId = R.drawable.image6
                mList[6].imageResourceId = R.drawable.image7
                mList[7].imageResourceId = R.drawable.image8
                mList[8].imageResourceId = R.drawable.image9
                mList[9].imageResourceId = R.drawable.image10
                fieldsDetailsCallback.onSuccess(mList)
            }
            .addOnFailureListener { exception ->
                Log.e("firestore", "Error getting data", exception)
            }
    }

    interface FieldsDetailsCallback {
        fun onSuccess(mArrayList: ArrayList<Field>)

        fun onFailure(error: String)
    }
}
