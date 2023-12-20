package edu.ap.padelpro.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ap.padelpro.R
import edu.ap.padelpro.model.TimeModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.Date
import java.util.Locale

class TimeAdapter(
    private val context: Context,
    private var timeList: List<TimeModel>,
    private val dateClickCallBack: DateClickCallBack
) :
    RecyclerView.Adapter<TimeAdapter.HourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemview_days, parent, false)
        return HourViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        val hour = timeList[position]
        holder.bind(hour)
    }

    override fun getItemCount(): Int {
        return timeList.size
    }

    inner class HourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: TimeModel) {
            val hourTextView: TextView = itemView.findViewById(R.id.tv_days)
            hourTextView.text = convertLongToTime(model.time)

            if (model.isSelected) {
                hourTextView.setBackgroundColor(context.resources.getColor(R.color.purple_200))
                hourTextView.setTextColor(context.resources.getColor(R.color.white))
            } else {
                hourTextView.setBackgroundColor(context.resources.getColor(R.color.white))
                hourTextView.setTextColor(context.resources.getColor(R.color.black))
            }
            if (model.isReserved)
            {
                hourTextView.setBackgroundColor(context.resources.getColor(R.color.teal_700))
                hourTextView.setTextColor(context.resources.getColor(R.color.white))
            }
            hourTextView.setOnClickListener {
                Log.e("TAG", "bind: ")
                if (!model.isReserved)
                    dateClickCallBack.onDateClick(adapterPosition)
            }
        }
    }

    fun updateTimesList(timeList: ArrayList<TimeModel>) {
        this.timeList = timeList
        notifyDataSetChanged()
    }

    interface DateClickCallBack {
        fun onDateClick(position: Int)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    private fun convertToDate(time: Long): String {
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