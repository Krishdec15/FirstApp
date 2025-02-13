# ✈️ Plane Journey Tracker (Jetpack Compose Version)

## 📖 Overview
Plane Journey Tracker is an Android application designed to help users track their journey stops. The app displays a list of journey stops with details such as distance and visa requirements. Users can switch between kilometers and miles, track their progress with a progress bar and percentage display, and mark stops as reached. The app is built using Jetpack Compose and reads journey stops from a `stops.txt` file located in the `assets` folder.

## ✨ Features
- ✅ **Displays journey stops**: Shows the names, distances, and visa requirements for each stop.
- ✅ **Toggle button to switch units**: Allows users to switch between kilometers and miles.
- ✅ **Button to mark a stop as reached**: Enables users to mark a stop as reached, with auto-scroll functionality.
- ✅ **Progress tracking**: Updates the progress bar and percentage dynamically as stops are marked as reached.
- ✅ **Visited stops are highlighted**: Uses different colors to highlight visited, current, and future stops.
- ✅ **Reads data from `stops.txt`**: Dynamically updates the UI based on the data read from the `stops.txt` file.

## 📂 Project Structure
```plaintext
📦 Project_2 [MyApplication]
 ┣ 📂 app/src/main/java/com/example/myapplication
 ┃ ┣ 📜 MainActivity.kt   --> Main logic for the app
 ┃ ┣ 📜 JourneyStop.kt    --> Data class for journey stops
 ┣ 📂 app/src/main/assets
 ┃ ┣ 📜 stops.txt         --> Data file containing stops and distances
 ┣ 📜 README.md  --> Project documentation
```

## 🔍 Implementation Details

### Reading Data from `stops.txt`
- The app reads data from `assets/stops.txt`, where each line follows the format:
  ```plaintext
  StopName, DistanceInKM, VisaRequired
  ```
- **Example Data:**
  ```plaintext
  New York,5000,true
  London,3500,false
  Paris,1200,false
  ```
- The function `readStopsFromFile()` in `MainActivity.kt` loads this data and converts it into a list of journey stops. This function reads each line from the file, splits it into parts, and creates a `JourneyStop` object for each line.

### Displaying Stops Using `LazyColumn`
- The app uses **LazyColumn** to display the journey stops.
- The `JourneyStopsList()` function dynamically displays the stops with auto-scrolling.
- **Features in Stop Items:**
  - **City Name**: Displayed in a bold, larger font.
  - **Distance (KM/Miles)**: Displayed with the appropriate unit based on the user's selection.
  - **Visa Requirement**: Indicates whether a visa is required for the stop.
  - **Background color**: Uses different colors to highlight visited, current, and future stops.

### Toggling Distance Units (KM ↔ Miles)
- The `DistanceToggleButton()` allows users to switch between kilometers and miles. When the button is clicked, the `toggleDistanceUnit()` function is called, which updates the distances displayed in the LazyColumn and the progress information.
- The function `toggleDistanceUnit()` updates all distances dynamically by converting the distances from kilometers to miles or vice versa.

### Progress Tracking & Progress Bar
- The app calculates the total distance of the journey, the distance covered based on visited stops, and the remaining distance. It also calculates the progress percentage.
- The function `JourneyProgress()` updates the progress bar (`LinearProgressIndicator`) and the percentage display (`Text`). This function is called whenever a stop is marked as reached or the distance unit is toggled.

### Auto-Scrolling When a Stop is Reached
- When the `NextStopButton()` is clicked, the next stop is marked as visited, the progress bar is updated, and the LazyColumn auto-scrolls to the next stop.
- The LazyColumn auto-scrolls to the next stop using the following code:
  ```kotlin
  listState.animateScrollToItem(currentStopIndex)
  ```
- This ensures smooth scrolling.

## 📋 Assignment Compliance
| **Requirement** | **Implemented?** |
|----------------|----------------|
| Use **Jetpack Compose & Kotlin** | ✅ Yes |
| Use **LazyColumn** to display stops | ✅ Yes |
| Read stops from a **text file (`stops.txt`)** | ✅ Yes |
| Allow switching **between km & miles** | ✅ Yes |
| Implement **progress tracking & auto-scrolling** | ✅ Yes |
| Match **XML version’s functionality** | ✅ Yes |
| Keep both versions in **separate GitHub branches** | ✅ Yes |

## 🌿 GitHub Branch Information
- **`compose-version`** → This Jetpack Compose version.
- **`xml-version`** → The XML-based version.


## 🛠️ Installation & Setup

### Clone the Repository
```sh
git clone https://github.com/Krishdec15/FirstApp
cd FirstApp
```

### Open in Android Studio
1. Open **Android Studio**
2. Click **File > Open** and select the project folder
3. Allow **Gradle to sync automatically**

### Run the App
- **Using Emulator:** Click ▶️ to run it on an Android Emulator
- **Using Physical Device:** Connect a phone via USB and enable Developer Mode

## 📝 Final Notes
- `stops.txt` is placed inside the `assets` folder.
