package edu.ap.padelpro

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import edu.ap.padelpro.adapter.UserMatchesAdapter
import edu.ap.padelpro.databinding.FragmentMyMatchesBinding
import edu.ap.padelpro.model.UserReservedMatches

class MyMatchesFragment : Fragment() {
    private lateinit var binding: FragmentMyMatchesBinding
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private var userReservedMatches: ArrayList<UserReservedMatches> = ArrayList()
    private var allmatchesList: ArrayList<UserReservedMatches> = ArrayList()
    private lateinit var myMatchesAdapter: UserMatchesAdapter
    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyMatchesBinding.inflate(layoutInflater, container, false)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        currentUser = (requireActivity() as MainActivity).getCurrentUser()

        binding.btnMyMatches.setOnClickListener {
//            binding.rvMyMatches.visibility = View.VISIBLE
//            binding.rvAvailable.visibility = View.GONE
            myMatchesAdapter.updateList(userReservedMatches)
            binding.btnMyMatches.backgroundTintList = resources.getColorStateList(R.color.green);
            binding.btnAvailable.backgroundTintList = resources.getColorStateList(R.color.gray);
        }
        binding.btnAvailable.setOnClickListener {
//            binding.rvMyMatches.visibility = View.GONE
//            binding.rvAvailable.visibility = View.VISIBLE
            myMatchesAdapter.updateList(allmatchesList)
            binding.btnAvailable.backgroundTintList = resources.getColorStateList(R.color.green);
            binding.btnMyMatches.backgroundTintList = resources.getColorStateList(R.color.gray);

        }
        getDocIDOfAvailableMatches()
        getMyMatchesList()
        return binding.root
    }

    private fun getMyMatchesList() {
        myMatchesAdapter = UserMatchesAdapter(
            requireContext(),
            userReservedMatches,
            object : UserMatchesAdapter.ClickCallBack {
                override fun onItemClick(position: Int) {

                }

            })
        binding.rvMyMatches.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMyMatches.adapter = myMatchesAdapter
        currentUser?.let { user ->
            val userID = user.uid
            val userRef = db.collection("users").document(userID)
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userData = documentSnapshot.data
                        val firstName = userData?.get("firstName") as? String ?: "No first name"
                        if (documentSnapshot.data?.containsKey("reservedMatches")!!) {
                            val listOfReservations =
                                documentSnapshot.data?.get("reservedMatches") as ArrayList<HashMap<*,*>>

                            for (i in 0 until listOfReservations.size) {
                                Log.e(
                                    "TAG",
                                    "getMyMatchesList: ${listOfReservations[i]}"
                                )
                                userReservedMatches.add(
                                    UserReservedMatches(listOfReservations[i]["stadiumName"].toString()
                                    ,listOfReservations[i]["time"] as Long)
                                )
                            }

//                            userReservedMatches.addAll(listOfReservations)
                            myMatchesAdapter.notifyDataSetChanged()
                        }

                    }
                }
                .addOnFailureListener { exception ->

                }
        }

    }
    private fun getDocIDOfAvailableMatches() {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("allMatches").get()
            .addOnSuccessListener { documents ->
                val docId = documents.documents[0].id
                getListOfAvailableMatches(docId)
            }
            .addOnFailureListener { exception ->
                Log.e("firestore", "Error getting data", exception)
            }
    }

    private fun getListOfAvailableMatches(docId: String) {

        val userRef = db.collection("allMatches").document(docId)
        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.data?.containsKey("reservedMatches")!!) {
                        val listOfReservations =
                            documentSnapshot.data?.get("reservedMatches") as ArrayList<HashMap<*,*>>

                        for (i in 0 until listOfReservations.size) {
                            Log.e(
                                "TAG",
                                "getMyMatchesList: ${listOfReservations[i]}"
                            )
                            allmatchesList.add(
                                UserReservedMatches(listOfReservations[i]["stadiumName"].toString()
                                ,listOfReservations[i]["time"] as Long)
                            )
                        }

//                            userReservedMatches.addAll(listOfReservations)
                        myMatchesAdapter.notifyDataSetChanged()
                    }

                }
            }
            .addOnFailureListener { exception ->

            }
    }
}