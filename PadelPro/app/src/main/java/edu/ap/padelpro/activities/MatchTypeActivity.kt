package edu.ap.padelpro.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import edu.ap.padelpro.Constants
import edu.ap.padelpro.databinding.ActivityMatchTypeBinding
import edu.ap.padelpro.model.UserReservedMatches

class MatchTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchTypeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDone.setOnClickListener {
            uploadSelectedData()
        }
    }

    private fun uploadSelectedData() {
        val fireStore = FirebaseFirestore.getInstance()
        val docID = intent.getStringExtra("docID")!!
        val ref = fireStore.collection("Fields").document(docID)

        val time1 = intent.getLongExtra("time1", 0L)
        val time2 = intent.getLongExtra("time2", 0L)
        val time3 = intent.getLongExtra("time3", 0L)
        ref.update("reservations", FieldValue.arrayUnion(time1))
        ref.update("reservations", FieldValue.arrayUnion(time2))
        ref.update("reservations", FieldValue.arrayUnion(time3))


        updateProfile()

    }

    private fun updateProfile() {
        val name = intent.getStringExtra("name")!!
        val time = intent.getLongExtra("time1", 0L)
        val userReservedModel = UserReservedMatches(name, time)
        val currentUser = Constants.firebaseUser
        currentUser?.let {
            val userID = it.uid
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(userID)

            userRef.update("reservedMatches", FieldValue.arrayUnion(userReservedModel))
        }
        Toast.makeText(this, "Time reserved successfully", Toast.LENGTH_SHORT).show()
        uploadToPublic(userReservedModel)
    }

    private fun uploadToPublic(userReservedModel: UserReservedMatches) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("allMatches").get()
            .addOnSuccessListener { documents ->
                val docId = documents.documents[0].id
                val ref = fireStore.collection("allMatches").document(docId)
                ref.update("reservedMatches", FieldValue.arrayUnion(userReservedModel))

            }
            .addOnFailureListener { exception ->
                Log.e("firestore", "Error getting data", exception)
            }

    }
}