package edu.ap.padelpro

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.ap.padelpro.R.*
import edu.ap.padelpro.adapter.ItemAdapter
import edu.ap.padelpro.model.Datasource

class Fields : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_fields)

        // Initialize data.
         lateinit var database: DatabaseReference
        database = Firebase.database.reference
        val myDataset = Datasource(database).loadFields()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = ItemAdapter(this, myDataset)

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
    }
}