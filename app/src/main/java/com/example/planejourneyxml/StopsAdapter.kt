package com.example.planejourneyxml

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import android.widget.BaseAdapter

class StopsAdapter(
    private val context: Context,
    private val stops: List<JourneyStop>,
    private val currentStopIndex: Int,
    private val isMiles: Boolean
) : BaseAdapter() {

    override fun getCount(): Int = stops.size
    override fun getItem(position: Int): Any = stops[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_stop, parent, false)
        val stop = stops[position]

        val stopName = view.findViewById<TextView>(R.id.tvStopName)
        val stopDetails = view.findViewById<TextView>(R.id.tvStopDetails)
        val cardView = view.findViewById<CardView>(R.id.cardView)

        val distance = if (isMiles) stop.distanceKm * 0.621371 else stop.distanceKm
        stopName.text = stop.name
        stopDetails.text = "${"%.1f".format(distance)} ${if (isMiles) "miles" else "km"} - Visa: ${if (stop.visaRequired) "Required" else "Not Required"}"

        // Highlighting Visited & Current Stops
        when {
            position < currentStopIndex -> cardView.setCardBackgroundColor(context.getColor(android.R.color.holo_blue_light)) // Visited
            position == currentStopIndex -> cardView.setCardBackgroundColor(context.getColor(android.R.color.holo_green_light)) // Current
            else -> cardView.setCardBackgroundColor(context.getColor(android.R.color.white)) // Future
        }

        return view
    }
}
