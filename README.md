# Mini Fleet Tracker App

This application simulates vehicle data using MQTT and provides an interactive map along with vehicle status and trip logs.

## Prerequisites

Before running this app, ensure you have Mosquitto installed on your laptop for MQTT functionality. If not, follow these steps to install it:

1. **Download Mosquitto**:
   - Visit the [Mosquitto download page](https://mosquitto.org/download/) and follow the installation instructions for your platform.

2. **Start Mosquitto Broker**:
   - Open PowerShell and navigate to the Mosquitto directory. 
     ```bash
     cd "C:\Program Files\mosquitto"
     ```
   - Start the Mosquitto service using:
     ```bash
     mosquitto -v
     ```
   - Alternatively, you can start it using the following command:
     ```bash
     net start mosquitto
     ```

3. **Run the Mock MQTT Script**:
   - Open PowerShell and use the following script to simulate vehicle data and send it to the MQTT broker:
   
   ```powershell
   $startLatitude = -6.1754
   $startLongitude = 106.8272
   $endLatitude = -6.4025
   $endLongitude = 106.8186

   $steps = 100  # Total number of steps to reach Surabaya
   $delay = 3  # Delay in seconds between updates

   for ($i = 0; $i -lt $steps; $i++) {
       # Calculate incremental movement
       $progress = $i / $steps
       $latitude = [math]::Round(($startLatitude + ($endLatitude - $startLatitude) * $progress), 6)
       $longitude = [math]::Round(($startLongitude + ($endLongitude - $startLongitude) * $progress), 6)

       # Simulated vehicle data
       $speed = Get-Random -Minimum 50 -Maximum 80  # Random speed between 50 and 80 km/h

       # Engine state based on speed
       if ($speed -gt 0) {
           $engineOn = $true  # Engine is ON if speed > 0
       } else {
           $engineOn = (Get-Random -Minimum 0 -Maximum 2) -eq 1  # Engine can be ON or OFF if speed = 0
       }

       $doorOpen = (Get-Random -Minimum 0 -Maximum 2) -eq 1  # Random door status (true or false)

       # JSON message
       $jsonMessage = "{ `"latitude`": $latitude, `"longitude`": $longitude, `"speed`": $speed, `"engineOn`": $engineOn, `"doorOpen`": $doorOpen }"

       .\mosquitto_pub -h test.mosquitto.org -p 1883 -t fleet/sensor -m $jsonMessage
   
       Write-Output "Sent: $jsonMessage"

       Start-Sleep -Seconds $delay
   }
   ```
   - This script simulates vehicle movement and sends mock data (latitude, longitude, speed, engine status, and door status) to the MQTT broker.
4. **Turn Off the Broker(if Needed)**:
   - Stop the service by using:
     ```bash
     Ctrl + C / Cmd + C
     ```
     
## Getting Started

1. **Clone The Repository**:
   ```bash
   git clone https://github.com/Zidantfnno21/MiniFleetTracker.git
   ```
2. **Setup The Project**:
   - Open the project in Android Studio and run the app on your device/emulator.

## Features

1. **Leaflet.js Map**:
   - The main screen displays a map where you can track vehicle locations using Leaflet.js.
2. **Vehicle Status**:
   - The app shows real-time vehicle status (e.g., speed, fuel level, etc.) in a user-friendly format.
3. **Trip Log**:
   - Scroll a bit on the main screen to reveal a button that opens the trip log, displaying the history of trips taken.
     
## Tech Stack

- MQTT (Mocked): For simulating vehicle data transmission.
- Jetpack Compose: For building the UI.
- Leaflet.js: For interactive map functionality.
- Room Database: For saving and displaying vehicle trip logs.

## Screenshots

<img src="https://github.com/user-attachments/assets/c52f986d-f390-45b0-9148-0797b14f1ecb" width="300" />
<img src="https://github.com/user-attachments/assets/266aa494-7d7c-45b6-857f-7aed46a60f05" width="300" />
<img src="https://github.com/user-attachments/assets/f1694feb-54b8-4b5a-856f-af6a730b4255" width="300" />

