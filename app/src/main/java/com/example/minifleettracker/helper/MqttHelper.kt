package com.example.minifleettracker.helper

import android.util.Log
import com.example.minifleettracker.helper.Constant.BROKER_URL
import com.example.minifleettracker.helper.Constant.TOPIC_FLEET_SENSOR
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttHelper(
    private val onMessageReceived: (String) -> Unit,
    private val onConnectionStatusChanged: (Boolean) -> Unit
) {
    private lateinit var mqttClient: MqttClient

    fun connect() {
        try {
            mqttClient = MqttClient(BROKER_URL, MqttClient.generateClientId(), MemoryPersistence())

            val options = MqttConnectOptions().apply {
                isCleanSession = true
                connectionTimeout = 10
                keepAliveInterval = 20
            }

            mqttClient.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    message?.payload?.let {
                        val receivedMessage = String(it)
                        Log.d("MQTT", "Received: $receivedMessage")
                        onMessageReceived(receivedMessage)
                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.e("MQTT", "Connection lost: ${cause?.message}")
                    onConnectionStatusChanged(false)
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.d("MQTT", "Message delivery complete")
                }
            })

            mqttClient.connect(options)
            mqttClient.subscribe(TOPIC_FLEET_SENSOR)
            onConnectionStatusChanged(true)

            Log.d("MQTT", "Connected to $BROKER_URL and subscribed to $TOPIC_FLEET_SENSOR")

        } catch (e: MqttException) {
            onConnectionStatusChanged(false)
            Log.e("MQTT", "Error connecting to MQTT broker: ${e.message}")
        }
    }

    fun disconnect() {
        if (mqttClient.isConnected) {
            mqttClient.disconnect()
            onConnectionStatusChanged(false)
        }
    }
}
