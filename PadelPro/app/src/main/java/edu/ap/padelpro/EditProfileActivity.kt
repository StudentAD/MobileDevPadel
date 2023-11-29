package edu.ap.padelpro

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlin.math.log

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    /*private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText*/
    private lateinit var cityEditText: EditText
    private lateinit var saveChangesButton: Button

    //region profilePicture
    private lateinit var profileImageView: ImageView
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri ->
            profileImageView.setImageURI(imageUri)
            uploadProfilePicture(imageUri)
        }
    }
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_edit_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        firstNameEditText = findViewById(R.id.first_name)
        lastNameEditText = findViewById(R.id.last_name)
        /*emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)*/
        cityEditText = findViewById(R.id.city)
        saveChangesButton = findViewById(R.id.save_changes_button)

        profileImageView = findViewById(R.id.profile_image_view)
        loadProfilePicture()
        profileImageView.setOnClickListener {
            pickImage.launch("image/*")
        }

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userID = user.uid
            val userRef = db.collection("users").document(userID)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userData = documentSnapshot.data
                        val firstName = userData?.get("firstName") as? String ?: ""
                        val lastName = userData?.get("lastName") as? String ?: ""
                        val city = userData?.get("city") as? String ?: ""

                        firstNameEditText.setText(firstName)
                        lastNameEditText.setText(lastName)
                        cityEditText.setText(city)
                    }
                }
        }

        /*val newPassword = passwordEditText.toString()
        currentUser?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                }
            }*/

        saveChangesButton.setOnClickListener {
            val updatedFirstName = firstNameEditText.text.toString()
            val updatedLastName = lastNameEditText.text.toString()
            val updatedCity = cityEditText.text.toString()

            if (updatedFirstName.isBlank() || updatedLastName.isBlank() || updatedCity.isBlank()) {
                Toast.makeText(this@EditProfileActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = auth.currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName("$updatedFirstName $updatedLastName")
                .build()

            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userID = user.uid
                        val userRef = db.collection("users").document(userID)

                        val updatedUserData = hashMapOf(
                            "firstName" to updatedFirstName,
                            "lastName" to updatedLastName,
                            "city" to updatedCity
                        )

                        userRef.get().addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val existingGender = documentSnapshot.getString("gender")
                                if (existingGender != null) {
                                    updatedUserData["gender"] = existingGender
                                }
                            }

                            userRef.set(updatedUserData)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@EditProfileActivity,
                                        "Profile updated successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(applicationContext, Profile::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this@EditProfileActivity,
                                        "Failed to update profile: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Error updating profile", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun loadProfilePicture() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val photoUrl = user.photoUrl
            if (photoUrl != null) {
                Glide.with(this@EditProfileActivity)
                    .load(photoUrl)
                    .placeholder(R.drawable.default_profile)
                    .into(profileImageView)
            }
        }
    }

    private fun uploadProfilePicture(imageUri: Uri) {
        val user = auth.currentUser
        user?.let { currentUser ->
            val storageReference = FirebaseStorage.getInstance().reference
            val profileImagesRef = storageReference.child("profile_picture/${currentUser.uid}")
            val uploadTask = profileImagesRef.putFile(imageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                profileImagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri)
                        .build()

                    currentUser.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Profile picture updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Failed to update profile picture",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@EditProfileActivity,
                    "Failed to upload profile picture: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UploadProfile", "Error uploading profile picture: ${e.message}")
            }
        }
    }
}