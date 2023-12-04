package edu.ap.padelpro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.ap.padelpro.adapter.ItemAdapter
import edu.ap.padelpro.databinding.ActivityFieldsBinding
import edu.ap.padelpro.model.Datasource
import kotlin.math.log

class FieldsFragment : Fragment(), ItemAdapter.FieldListener {

    private lateinit var binding: ActivityFieldsBinding // Adjust the binding class name based on your layout file name

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityFieldsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize data.
        lateinit var database: DatabaseReference
        database = Firebase.database.reference
        val myDataset = Datasource(database).loadFields()

        val recyclerView = binding.recyclerView
        recyclerView.adapter = ItemAdapter(requireContext(), myDataset,this)

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
    }

    override fun OnFieldClick(position: Int) {
        Log.v("FieldClick", "item $position clicked")
        /*
        lateinit var database: DatabaseReference
        database = Firebase.database.reference
        val field:Field= Datasource(database).loadFields().get(position)*/
    }
}
