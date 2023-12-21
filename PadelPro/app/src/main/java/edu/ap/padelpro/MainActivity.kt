package edu.ap.padelpro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.ap.padelpro.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //region EditProfileActivity needs this tho redirect to profile page
        if (intent.hasExtra("fragmentToLoad")) {
            val fragmentToLoad = intent.getStringExtra("fragmentToLoad")
            when (fragmentToLoad) {
                "profile" -> replaceFragment(Profile())
            }
        } else {
            replaceFragment(Profile())
        }
        //endregion

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.play -> replaceFragment(FieldsFragment())
                        R.id.profile -> replaceFragment(Profile())
                R.id.settings -> replaceFragment(MyMatchesFragment())
                else -> {
                }
            }
            true
        }

        auth = FirebaseAuth.getInstance()
        user = getCurrentUser()

        Constants.firebaseUser = user
        if (user == null) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
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

