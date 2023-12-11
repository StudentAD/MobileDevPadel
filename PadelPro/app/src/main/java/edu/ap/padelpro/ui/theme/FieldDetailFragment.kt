package edu.ap.padelpro.ui.theme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.ap.padelpro.R
import edu.ap.padelpro.adapter.HoursAdapter
import edu.ap.padelpro.databinding.FragmentFieldDetailBinding
import edu.ap.padelpro.model.Datasource

class FieldDetailFragment(val position: Int) : Fragment() {
    private lateinit var binding:FragmentFieldDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFieldDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load data for the paddle field
        lateinit var database: DatabaseReference
        database = Firebase.database.reference
        val paddleField =
            Datasource(database).loadFields()[position] // Implement this method to retrieve paddle field data

        val binding= FragmentFieldDetailBinding.bind(view)
        val recyclerView: RecyclerView = binding.hoursRecyclerView
        // Update UI components
        binding.fieldTitle.text =resources.getString(paddleField.name)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val availableHours = paddleField.hours
        recyclerView.adapter = HoursAdapter(availableHours)


        // Additional setup if needed
    }
}