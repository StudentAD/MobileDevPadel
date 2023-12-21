package edu.ap.padelpro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ap.padelpro.R
import edu.ap.padelpro.model.UserReservedMatches
import java.text.SimpleDateFormat
import java.util.Date

class UserMatchesAdapter(
    private val context: Context,
    private var timeList: List<UserReservedMatches>,
    private val dateClickCallBack: ClickCallBack
) :
    RecyclerView.Adapter<UserMatchesAdapter.HourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemview_my_matches, parent, false)
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
        fun bind(model: UserReservedMatches) {
            val tvName: TextView = itemView.findViewById(R.id.tv_name)
            tvName.text = model.stadiumName
            val tvTime: TextView = itemView.findViewById(R.id.tv_time)
            tvTime.text = convertLongToTime(model.time)
        }
    }

    public fun updateList(timeList: List<UserReservedMatches>)
    {
        this.timeList = timeList
        notifyDataSetChanged()
    }
    interface ClickCallBack {
        fun onItemClick(position: Int)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }
}