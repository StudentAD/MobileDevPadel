package edu.ap.padelpro

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.app.DatePickerDialog
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.Spinner
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class Register : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextFirstName: TextInputEditText
    private lateinit var editTextLastName: TextInputEditText
    private lateinit var editTextCity: TextInputEditText
    private lateinit var spinnerGender: Spinner
    private lateinit var buttonReg: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private lateinit var dobLayout: TextInputLayout
    private lateinit var dobEditText: TextInputEditText
    private lateinit var selectDOBButton: ImageButton

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password)
        editTextFirstName = findViewById(R.id.first_name)
        editTextLastName = findViewById(R.id.last_name)
        editTextCity = findViewById(R.id.city)
        buttonReg = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progressBar)

        //region genderpicker
        spinnerGender = findViewById(R.id.spinner_gender)
        setupGenderSpinner()
        //endregion

        //region datepicker
        dobLayout = findViewById(R.id.dob_layout)
        dobEditText = findViewById(R.id.dob)
        selectDOBButton = findViewById(R.id.btn_select_dob)

        selectDOBButton.setOnClickListener {
            showDatePickerDialog()
        }
        //endregion

        auth = Firebase.auth

        //region redirectToLogin
        textView = findViewById(R.id.loginNow)
        textView.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
        //endregion

        buttonReg.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val firstName = editTextFirstName.text.toString()
            val lastName = editTextLastName.text.toString()
            val city = editTextCity.text.toString()
            val gender = spinnerGender.selectedItem.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(city) || TextUtils.isEmpty(gender)
            ) {
                Toast.makeText(
                    this@Register,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.uid?.let { userId ->
                            val userRef = db.collection("users").document(userId)

                            val userData = hashMapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "city" to city,
                                "gender" to gender
                                // Add more fields as needed
                            )

                            userRef.set(userData)
                                .addOnSuccessListener {
                                    // Profile created successfully
                                    val intent = Intent(applicationContext, Login::class.java)
                                    startActivity(intent)
                                    finish()
                                }.addOnFailureListener { e ->
                                    // Handle failure to create profile
                                    Toast.makeText(
                                        this@Register,
                                        "Failed to create profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        Toast.makeText(
                            this@Register,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dobEditText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis // Optional: Set max date
        datePickerDialog.show()
    }

    private fun setupGenderSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGender.adapter = adapter
        }
    }

}