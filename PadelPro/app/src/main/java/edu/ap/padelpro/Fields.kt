package edu.ap.padelpro

import android.os.Bundle
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
import edu.ap.padelpro.model.Field
import edu.ap.padelpro.ui.theme.FieldDetailFragment

class FieldsFragment : Fragment(), ItemAdapter.FieldListener {

    private lateinit var binding: ActivityFieldsBinding
    private var mList = ArrayList<Field>()
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
        Datasource(database).loadFields(object : Datasource.FieldsDetailsCallback {
            override fun onSuccess(mArrayList: ArrayList<Field>) {
                mList = mArrayList
                val recyclerView = binding.recyclerView
                recyclerView.adapter = ItemAdapter(requireContext(), mList, this@FieldsFragment)

                recyclerView.setHasFixedSize(true)
            }

            override fun onFailure(error: String) {

            }

        })
    }

    override fun OnFieldClick(position: Int) {
        val paddleFieldFragment = FieldDetailFragment(mList[position])

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, paddleFieldFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}
