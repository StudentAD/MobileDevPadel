package edu.ap.padelpro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class Profile : Fragment() {

    private lateinit var buttonLogout: Button
    private lateinit var textViewUserName: TextView
    private lateinit var textViewCity: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        buttonLogout = view.findViewById(R.id.logout_button)
        textViewUserName = view.findViewById(R.id.user_name_text)
        textViewCity = view.findViewById(R.id.city_text)

        // Retrieve the current user from MainActivity (assuming it's already authenticated)
        currentUser = (requireActivity() as MainActivity).getCurrentUser()

        // Display user details if the currentUser is not null
        currentUser?.let { user ->
            val userID = user.uid
            val userRef = db.collection("users").document(userID)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userData = documentSnapshot.data
                        val firstName = userData?.get("firstName") as? String ?: "No first name"
                        val lastName = userData?.get("lastName") as? String ?: "No last name"
                        val city = userData?.get("city") as? String ?: "No city"

                        val userName = "$firstName $lastName"
                        textViewUserName.text = userName
                        textViewCity.text = "City: $city"
                    } else {
                        textViewUserName.text = "User details not available"
                        textViewCity.text = "City details not available"
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failures
                    textViewUserName.text = "Failed to retrieve user details"
                    textViewCity.text = "Failed to retrieve city details"
                }
        }

        buttonLogout.setOnClickListener {
            // Logout functionality
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }
}