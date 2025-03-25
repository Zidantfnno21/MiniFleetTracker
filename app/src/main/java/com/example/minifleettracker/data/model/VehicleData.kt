package com.example.minifleettracker.data.model

data class VehicleData(
    val latitude: Double,
    val longitude: Double,
    val speed: Int,
    val engineOn: Boolean,
    val doorOpen: Boolean,
)

//mosquitto_pub -h broker.hivemq.com -p 1883 -t fleet/sensor -m "{\"latitude\": $((51.505 + RANDOM % 10 * 0.001)), \"longitude\": $((-0.09 + RANDOM % 10 * 0.001)), \"speed\": $((RANDOM % 100)), \"engineOn\": $(($RANDOM % 2 == 1)), \"doorOpen\": $(($RANDOM % 2 == 1))}"