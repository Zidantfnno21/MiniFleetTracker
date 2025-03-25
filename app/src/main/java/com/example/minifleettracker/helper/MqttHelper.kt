package com.example.minifleettracker.helper

import android.util.Log
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttHelper(
    private val brokerUrl: String = "tcp://broker.hivemq.com:1883",
    private val topic: String = "fleet/sensor",
    private val onMessageReceived: (String) -> Unit
) {
    private lateinit var mqttClient: MqttClient

    fun connect() {
        try {
            mqttClient = MqttClient(brokerUrl, MqttClient.generateClientId(), MemoryPersistence())
            val options = MqttConnectOptions().apply { isCleanSession = true }

            mqttClient.connect(options)
            mqttClient.subscribe(topic) { _, message ->
                onMessageReceived(String(message.payload))
            }

            Log.d("MQTT", "Connected to $brokerUrl and subscribed to $topic")

        } catch (e: MqttException) {
            Log.e("MQTT", "Error connecting to MQTT broker: ${e.message}")
        }
    }

    fun disconnect() {
        if (mqttClient.isConnected) mqttClient.disconnect()
    }
}