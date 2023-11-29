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

class Profile : Fragment() {

    private lateinit var buttonLogout: Button
    private lateinit var textViewUserDetails: TextView
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        buttonLogout = view.findViewById(R.id.logout_button)
        textViewUserDetails = view.findViewById(R.id.user_details_text)

        // Retrieve the current user from MainActivity (assuming it's already authenticated)
        currentUser = (requireActivity() as MainActivity).getCurrentUser()

        // Display user details if the currentUser is not null
        currentUser?.let { user ->
            textViewUserDetails.text = "User Email: ${user.email}"
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