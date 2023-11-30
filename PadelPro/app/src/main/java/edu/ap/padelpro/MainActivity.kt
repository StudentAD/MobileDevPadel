package edu.ap.padelpro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.fragment.app.FragmentManager
import edu.ap.padelpro.ui.theme.PadelProTheme
import edu.ap.padelpro.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.play -> replaceFragment(FieldsFragment())
                        R.id.profile -> replaceFragment(Profile())
                R.id.settings -> replaceFragment(Settings())
                else -> {
                    // Handle other cases if needed
                }
            }
            true
        }

        auth = FirebaseAuth.getInstance()
        user = getCurrentUser()

        if (user == null) {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}

