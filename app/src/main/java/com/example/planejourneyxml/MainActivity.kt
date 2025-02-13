package com.example.planejourneyxml

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var listViewStops: ListView
    private lateinit var btnToggleUnit: Button
    private lateinit var btnNextStop: Button
    private lateinit var tvTotalDistance: TextView
    private lateinit var tvCoveredDistance: TextView
    private lateinit var tvRemainingDistance: TextView
    private lateinit var tvProgressPercentage: TextView
    private lateinit var progressBar: ProgressBar

    private var stops = mutableListOf<JourneyStop>()
    private var isMiles = false
    private var currentStopIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Views
        listViewStops = findViewById(R.id.listViewStops)
        btnToggleUnit = findViewById(R.id.btnToggleUnit)
        btnNextStop = findViewById(R.id.btnNextStop)
        tvTotalDistance = findViewById(R.id.tvTotalDistance)
        tvCoveredDistance = findViewById(R.id.tvCoveredDistance)
        tvRemainingDistance = findViewById(R.id.tvRemainingDistance)
        tvProgressPercentage = findViewById(R.id.tvProgressPercentage)
        progressBar = findViewById(R.id.progressBar)

        // Read stops from file
        stops = readStopsFromFile().toMutableList()

        // Display stops
        updateUI()

        // Toggle Distance Unit
        btnToggleUnit.setOnClickListener { toggleDistanceUnit() }

        // Move to next stop
        btnNextStop.setOnClickListener { moveToNextStop() }
    }

    private fun readStopsFromFile(): List<JourneyStop> {
        val stopsList = mutableListOf<JourneyStop>()
        try {
            val inputStream = assets.open("stops.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.forEachLine { line ->
                val parts = line.split(",")
                if (parts.size == 3) {
                    val name = parts[0].trim()
                    val distanceKm = parts[1].trim().toDoubleOrNull() ?: 0.0
                    val visaRequired = parts[2].trim().toBoolean()
                    stopsList.add(JourneyStop(name, distanceKm, visaRequired))
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stopsList
    }

    private fun updateUI() {
        val totalDistanceKm = stops.sumOf { it.distanceKm }
        val coveredDistanceKm = stops.take(currentStopIndex).sumOf { it.distanceKm }

        val totalDistance = if (isMiles) totalDistanceKm * 0.621371 else totalDistanceKm
        val coveredDistance = if (isMiles) coveredDistanceKm * 0.621371 else coveredDistanceKm
        val remainingDistance = totalDistance - coveredDistance
        val unit = if (isMiles) "miles" else "km"

        // Update Text Views
        tvTotalDistance.text = "Total Distance: ${"%.1f".format(totalDistance)} $unit"
        tvCoveredDistance.text = "Distance Covered: ${"%.1f".format(coveredDistance)} $unit"
        tvRemainingDistance.text = "Distance Left: ${"%.1f".format(remainingDistance)} $unit"

        // Update Progress Bar and Percentage
        val progress = if (totalDistance > 0) ((coveredDistance / totalDistance) * 100).toInt() else 0
        progressBar.progress = progress
        tvProgressPercentage.text = "Progress: $progress%"  // Ensure this updates correctly

        // Use Custom Adapter
        listViewStops.adapter = StopsAdapter(this, stops, currentStopIndex, isMiles)
    }

    private fun toggleDistanceUnit() {
        isMiles = !isMiles
        btnToggleUnit.text = if (isMiles) "Switch to KM" else "Switch to Miles"
        updateUI()
    }

    private fun moveToNextStop() {
        if (currentStopIndex < stops.size) {
            currentStopIndex++
            updateUI()

            // Ensure smooth scroll to the next stop without jumping to the top
            listViewStops.post {
                listViewStops.smoothScrollToPosition(currentStopIndex)
            }
        }
    }

}