package com.example.ugd9_c_10772_project2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView

    private val Channel = "Notification"
    private val notif = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        square = findViewById(R.id.tv_square)
        setUpSensorStuff()

        createChannelNotif()
    }
    fun setUpSensorStuff() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
                accelerometer -> sensorManager.registerListener(
            this, accelerometer,
            SensorManager.SENSOR_DELAY_FASTEST,
            SensorManager.SENSOR_DELAY_FASTEST
        )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = event.values[0]
            val upDown = event.values[1]

            square.apply {
                rotationX = upDown *3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }

            val color = if (upDown.toInt() == 0 && sides.toInt() == 0)
                Color.GREEN else Color.RED
            square.setBackgroundColor(color)
            if(upDown.toInt() != 0 && sides.toInt() != 0){
                sendNotif()
            }
            square.text = "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}"
        }
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    private fun createChannelNotif(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Title"
            val descriptionText = "Description"

            val channel1 = NotificationChannel(Channel, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)

        }
    }

    private fun sendNotif(){

        val builder = NotificationCompat.Builder(this, Channel)
            .setSmallIcon(R.drawable.ic_android_black)
            .setContentText("Selamat Anda sudah berhasil Modul 8 dan 9")
            .setContentTitle("Modul89_C_10772_PROJECT2 ")
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)){
            notify(notif, builder.build())
        }
    }

}