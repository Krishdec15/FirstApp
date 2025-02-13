package com.example.myapplication

import com.example.myapplication.JourneyStop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val stops = readStopsFromFile() // Initialize stops properly

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StopsList(stops, Modifier.padding(innerPadding))
                }
            }
        }
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

}


@Composable
fun StopsList(stops: List<JourneyStop>, modifier: Modifier = Modifier) {
    var isMiles by remember { mutableStateOf(false) }
    var currentStopIndex by remember { mutableStateOf(0) }
    val listState = rememberLazyListState() // Track scroll state

    val totalDistanceKm = stops.sumOf { it.distanceKm }
    val coveredDistanceKm = stops.take(currentStopIndex).sumOf { it.distanceKm }

    val totalDistance = if (isMiles) totalDistanceKm * 0.621371 else totalDistanceKm
    val coveredDistance = if (isMiles) coveredDistanceKm * 0.621371 else coveredDistanceKm
    val unit = if (isMiles) "miles" else "km"

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        AppTitle()
        DistanceToggleButton(isMiles) { isMiles = !isMiles }
        Spacer(modifier = Modifier.height(8.dp))

        // Move to next stop and scroll down
        NextStopButton(currentStopIndex, stops.size) {
            currentStopIndex++
        }

        Spacer(modifier = Modifier.height(8.dp))
        JourneyProgress(coveredDistance, totalDistance, unit)
        Spacer(modifier = Modifier.height(8.dp))
        JourneyStopsList(stops, currentStopIndex, isMiles, unit, listState)
    }
}



@Composable
fun AppTitle() {
    Text(
        text = "Journey Tracker",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}


@Composable
fun DistanceToggleButton(isMiles: Boolean, onToggle: () -> Unit) {
    Button(
        onClick = onToggle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp), // Rounded corners
        elevation = ButtonDefaults.buttonElevation(6.dp) // Adds shadow
    ) {
        Text(
            text = if (isMiles) "Switch to KM" else "Switch to Miles",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun NextStopButton(currentStopIndex: Int, totalStops: Int, onNextStop: () -> Unit) {
    Button(
        onClick = { if (currentStopIndex < totalStops) onNextStop() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp), // Rounded corners
        elevation = ButtonDefaults.buttonElevation(6.dp) // Adds shadow
    ) {
        Text(
            text = "Next Stop Reached",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun JourneyProgress(coveredDistance: Double, totalDistance: Double, unit: String) {
    val progress = if (totalDistance > 0) (coveredDistance / totalDistance).toFloat() else 0f
    val progressPercentage = (progress * 100).toInt()

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text("Total Distance: ${"%.1f".format(totalDistance)} $unit", style = MaterialTheme.typography.bodyMedium)
        Text("Distance Covered: ${"%.1f".format(coveredDistance)} $unit", style = MaterialTheme.typography.bodyMedium)
        Text("Distance Left: ${"%.1f".format(totalDistance - coveredDistance)} $unit", style = MaterialTheme.typography.bodyMedium)
        Text("Progress: $progressPercentage%", style = MaterialTheme.typography.bodyMedium)

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun JourneyStopsList(
    stops: List<JourneyStop>,
    currentStopIndex: Int,
    isMiles: Boolean,
    unit: String,
    listState: LazyListState
) {
    LaunchedEffect(currentStopIndex) {
        listState.animateScrollToItem(currentStopIndex)
    }

    LazyColumn(state = listState) {
        items(stops.size) { index ->
            val stop = stops[index]
            val distance = if (isMiles) stop.distanceKm * 0.621371 else stop.distanceKm

            val backgroundColor =
                when {
                    index < currentStopIndex -> MaterialTheme.colorScheme.primaryContainer // Completed stops
                    index == currentStopIndex -> MaterialTheme.colorScheme.secondaryContainer // Current stop
                    else -> MaterialTheme.colorScheme.surfaceVariant // Future stops
                }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                elevation = CardDefaults.cardElevation(4.dp) // Adds shadow effect
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = stop.name,
                        style = if (index == currentStopIndex) MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        else MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "${"%.1f".format(distance)} $unit - Visa: ${if (stop.visaRequired) "Required" else "Not Required"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}



