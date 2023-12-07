package edu.ap.padelpro

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
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

    private lateinit var profileImageView: ImageView
    private val defaultProfileImage = R.drawable.default_profile

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        //region redirectToEditProfileActivity
        val editProfileButton: Button = view.findViewById(R.id.edit_profile_button)

        editProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        //endregion

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        buttonLogout = view.findViewById(R.id.logout_button)
        textViewUserName = view.findViewById(R.id.user_name_text)
        textViewCity = view.findViewById(R.id.city_text)

        profileImageView = view.findViewById(R.id.profile_image_view)
        loadProfilePicture()

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
                    textViewUserName.text = "Failed to retrieve user details"
                    textViewCity.text = "Failed to retrieve city details"
                }
        }

        buttonLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    private fun loadProfilePicture() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val photoUrl = user.photoUrl
            if (photoUrl != null) {
                Glide.with(this@Profile)
                    .load(photoUrl)
                    .placeholder(R.drawable.default_profile)
                    .into(profileImageView)
            }
        }
    }
}