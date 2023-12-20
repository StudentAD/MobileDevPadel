package edu.ap.padelpro.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ap.padelpro.R
import edu.ap.padelpro.model.DateModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.Locale

class DateAdapter(private val context: Context,
                  private val availableDays: List<DateModel>,
                  private val dateClickCallBack: DateClickCallBack) :
    RecyclerView.Adapter<DateAdapter.HourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemview_days, parent, false)
        return HourViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        val hour = availableDays[position]
        holder.bind(hour)
    }

    override fun getItemCount(): Int {
        return availableDays.size
    }

    inner class HourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: DateModel) {
            val hourTextView: TextView = itemView.findViewById(R.id.tv_days)
            hourTextView.text = convertToDate(model.date)

            if (model.isSelected)
            {
                hourTextView.setBackgroundColor(context.resources.getColor(R.color.purple_200))
                hourTextView.setTextColor(context.resources.getColor(R.color.white))
            }else
            {
                hourTextView.setBackgroundColor(context.resources.getColor(R.color.white))
                hourTextView.setTextColor(context.resources.getColor(R.color.black))
            }
            hourTextView.setOnClickListener {
                Log.e("TAG", "bind: " )
                dateClickCallBack.onDateClick(adapterPosition)
            }
        }
    }

    interface DateClickCallBack
    {
        fun onDateClick(position: Int)
    }
    private fun convertToDate(time: Long): String
    {
        val timeInMillis = time // Replace this with your long value

        // Convert milliseconds to LocalDateTime in UTC
        val dateTime: LocalDateTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMillis), ZoneOffset.UTC)

        // Format LocalDateTime to display only day and month name
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.ENGLISH)
        val temporalAccessor: TemporalAccessor = dateTime
        val formattedDate: String = formatter.format(temporalAccessor)
        return formattedDate
    }
}