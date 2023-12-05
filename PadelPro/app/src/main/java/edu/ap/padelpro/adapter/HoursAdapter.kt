package edu.ap.padelpro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ap.padelpro.R

class HoursAdapter(private val availableHours: List<String>) :
    RecyclerView.Adapter<HoursAdapter.HourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hour_card, parent, false)
        return HourViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        val hour = availableHours[position]
        holder.bind(hour)
    }

    override fun getItemCount(): Int {
        return availableHours.size
    }

    inner class HourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hour: String) {
            val hourTextView: TextView = itemView.findViewById(R.id.hourTextView)
            hourTextView.text = hour
        }
    }
}


