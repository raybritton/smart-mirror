package app.raybritton.smartmirror.ui.settings

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.raybritton.smartmirror.R
import app.raybritton.smartmirror.data.models.Event
import app.raybritton.smartmirror.ext.setTextColorRes
import kotlinx.android.synthetic.main.element_event.view.*
import java.util.*

class LogAdapter(context: Context) : RecyclerView.Adapter<LogAdapter.EventViewHolder>() {
    private val timeFormatter = DateFormat.getTimeFormat(context)

    var data: List<Event> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.element_event, parent, false)
        return EventViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.itemView.event_important.visibility = if (data[position].important) View.VISIBLE else View.GONE
        holder.itemView.event_message.text = data[position].message
        holder.itemView.event_type.setText(data[position].type.display)
        holder.itemView.event_type.setTextColorRes(data[position].type.color)
        holder.itemView.event_time.text = timeFormatter.format(Date(data[position].time.millis))
    }

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view)
}