package edu.ap.padelpro.ui.theme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import edu.ap.padelpro.MainActivity
import edu.ap.padelpro.activities.MatchTypeActivity
import edu.ap.padelpro.adapter.DateAdapter
import edu.ap.padelpro.adapter.HoursAdapter
import edu.ap.padelpro.adapter.TimeAdapter
import edu.ap.padelpro.databinding.FragmentFieldDetailBinding
import edu.ap.padelpro.model.DateModel
import edu.ap.padelpro.model.Field
import edu.ap.padelpro.model.TimeModel
import edu.ap.padelpro.model.UserReservedMatches
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

class FieldDetailFragment(val paddleField: Field) : Fragment() {
    private lateinit var binding:FragmentFieldDetailBinding
    private lateinit var dateAdapter: DateAdapter
    private lateinit var timeAdapter: TimeAdapter
    private var daysList = ArrayList<DateModel>()
    private var previousDateSelectedPosition = -1
    private var previousTimeSelectedPosition = -1
    private var firebaseListOfTime: ArrayList<Long> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFieldDetailBinding.inflate(inflater, container, false)
        binding.fieldTitle.text = paddleField.document.data["Name"].toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load data for the paddle field
        lateinit var database: DatabaseReference
        database = Firebase.database.reference

        val binding = FragmentFieldDetailBinding.bind(view)
        val recyclerView: RecyclerView = binding.hoursRecyclerView

        // Update UI components
        // binding.fieldTitle.text =resources.getString(paddleField.name)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val availableHours = paddleField.hours
        recyclerView.adapter = HoursAdapter(availableHours)

        if (paddleField.document.contains("reservations")) {
            val listOfReservations = paddleField.document.data["reservations"] as ArrayList<Long>
            for (i in listOfReservations.indices) {
                firebaseListOfTime.add(listOfReservations[i])
                Log.e("TAG", "onViewCreated: ${listOfReservations[i]}")
            }
        }
        getDays()

        binding.btnDone.setOnClickListener {
            findSelectedDataAndTime()
        }
    }

    private fun findSelectedDataAndTime()
    {
        for (i in 0 until daysList.size)
        {
            if (daysList[i].isSelected)
            {
                for (j in 0 until daysList[i].timeList.size)
                {
                    if (daysList[i].timeList[j].isSelected)
                    {
                        uploadSelectedData(i,j)
                    }
                }
            }
        }
    }

    private fun uploadSelectedData(dayPosition: Int, timePosition: Int) {
        val fireStore = FirebaseFirestore.getInstance()
        val ref = fireStore.collection("Fields").document(paddleField.document.id)

        val timeModel = daysList[dayPosition].timeList[timePosition]
        //ref.update("reservations",FieldValue.arrayUnion(timeModel.time))
        val time1 = timeModel.time
        var time2 = 0L
        var time3 = 0L
        if (timePosition + 1 < daysList[dayPosition].timeList.size)
        {
            val timeModelSecond = daysList[dayPosition].timeList[timePosition + 1]
            //ref.update("reservations",FieldValue.arrayUnion(timeModelSecond.time))
            time2 = timeModelSecond.time
            if (timePosition + 2 < daysList[dayPosition].timeList.size)
            {
                val timeModelThird = daysList[dayPosition].timeList[timePosition + 2]
                //ref.update("reservations",FieldValue.arrayUnion(timeModelThird.time))
                time3 = timeModelThird.time
            }
        }
        val name = paddleField.document.data["Name"].toString()

        //updateProfile(timeModel.time)
        //Toast.makeText(requireContext(),"Time reserved successfully",Toast.LENGTH_SHORT).show()
        startActivity(
            Intent(requireContext(), MatchTypeActivity::class.java)
            .putExtra("time1",time1)
            .putExtra("time2",time2)
            .putExtra("time3",time3)
            .putExtra("name",name)
            .putExtra("docID",paddleField.document.id))

    }

    private fun updateProfile(time: Long) {
        val name = paddleField.document.data["Name"].toString()
        val userReservedModel = UserReservedMatches(name,time)
        val currentUser = (requireActivity() as MainActivity).getCurrentUser()
        currentUser?.let {
            val userID = it.uid
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(userID)

            userRef.update("reservedMatches", FieldValue.arrayUnion(userReservedModel))
        }
    }

    private fun getDays() {

        val currentDate = LocalDate.now()
        val zoneId = ZoneId.systemDefault() // Get system default timezone

        // List to store long values (milliseconds) of dates
        val datesInMillis: MutableList<Long> = ArrayList()

        // Get current date in milliseconds
        val currentInMillis = currentDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
        //datesInMillis.add(currentInMillis)

        // List to store dates in string format
        val datesAsString: MutableList<String> = ArrayList()
        datesAsString.add(currentDate.toString())

        // Get long values (milliseconds) and string formatted dates for the next 7 days
        for (i in 1..7) {
            val nextDate = currentDate.plusDays(i.toLong())
            val nextDateInMillis = nextDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
            datesInMillis.add(nextDateInMillis)
            datesAsString.add(nextDate.toString())
        }

        // Print the list of long values (milliseconds) of dates and their corresponding dates
        for (i in datesInMillis.indices) {
            println("Date in milliseconds: " + datesInMillis[i] + ", Date: " + datesAsString[i])

            val calendar = Calendar.getInstance()
            calendar.time = Date(datesInMillis[i])
            calendar[Calendar.HOUR_OF_DAY] = 8

            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            // Create a list to store times in milliseconds
            val timeList: ArrayList<TimeModel> = ArrayList()

            // Loop from 8 AM to 6 PM with a 30-minute interval
            while (calendar[Calendar.HOUR_OF_DAY] < 18) {
                val isReserved = firebaseListOfTime.contains(calendar.timeInMillis)
                Log.e("TAG", "getDays: $isReserved" )
                Log.e("TAG", "getDays: ${calendar.timeInMillis}" )
                timeList.add(TimeModel(calendar.timeInMillis,false,isReserved))
                calendar.add(Calendar.MINUTE, 30) // Increment by 30 minutes
            }
            daysList.add(DateModel(datesInMillis[i],timeList,false))

        }
        daysList[0].isSelected = true
        previousDateSelectedPosition = 0
        dateAdapter = DateAdapter(requireContext(),daysList,object: DateAdapter.DateClickCallBack
        {
            override fun onDateClick(position: Int) {

                //unSelect the selected time of the previous date when selects a new date
                if (previousTimeSelectedPosition > -1)
                {
                    daysList[previousDateSelectedPosition]
                        .timeList[previousTimeSelectedPosition].isSelected = false
                }

                if (previousDateSelectedPosition > -1)
                {
                    daysList[previousDateSelectedPosition].isSelected = false
                }
                daysList[position].isSelected = true

                //daysList[position].isSelected = !daysList[position].isSelected
                dateAdapter.notifyItemChanged(previousDateSelectedPosition)
                dateAdapter.notifyItemChanged(position)
                previousDateSelectedPosition = position
                timeAdapter.updateTimesList(daysList[position].timeList)
            }
        })
        binding.daysRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        binding.daysRecyclerView.adapter = dateAdapter
        initTimeAdapter()
    }

    private fun initTimeAdapter()
    {
        timeAdapter = TimeAdapter(requireContext(),daysList[0].timeList,object : TimeAdapter.DateClickCallBack
        {
            override fun onDateClick(position: Int) {
                if (previousTimeSelectedPosition > -1)
                {
                    daysList[previousDateSelectedPosition]
                        .timeList[previousTimeSelectedPosition].isSelected = false
                }
                daysList[previousDateSelectedPosition].timeList[position].isSelected = true

                //daysList[position].isSelected = !daysList[position].isSelected
                timeAdapter.notifyItemChanged(previousTimeSelectedPosition)
                timeAdapter.notifyItemChanged(position)
                previousTimeSelectedPosition = position
            }
        })
        binding.hoursRecyclerView.layoutManager = GridLayoutManager(requireContext(),5)
        binding.hoursRecyclerView.adapter = timeAdapter
    }
}