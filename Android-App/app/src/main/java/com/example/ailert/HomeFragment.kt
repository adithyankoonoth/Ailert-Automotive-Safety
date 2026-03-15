package com.example.ailert

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.google.firebase.database.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var database: DatabaseReference? = null
    private val channelId = "CO_ALERT_CHANNEL"
    private var isAlertActive = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvPpmValue = view.findViewById<TextView>(R.id.tvPpmValue)
        val mainLayout = view.findViewById<ConstraintLayout>(R.id.main_layout)

        // Initialize Firebase at the root level
        database = FirebaseDatabase.getInstance("https://ailert-a55ca-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        // Listen for PPM data and the dynamic threshold
        database?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded || view == null) return

                val ppm = snapshot.child("current_ppm").getValue(Int::class.java) ?: 0
                val threshold = snapshot.child("trigger_threshold").getValue(Int::class.java) ?: 50

                activity?.runOnUiThread {
                    tvPpmValue?.text = ppm.toString()

                    if (ppm >= threshold) {
                        mainLayout?.setBackgroundColor(Color.parseColor("#E35D4E")) // Danger Red
                        if (!isAlertActive) {
                            sendPushNotification("⚠️ DANGER", "CO level ($ppm) exceeded threshold ($threshold)")
                            isAlertActive = true
                        }
                    } else {
                        isAlertActive = false
                        mainLayout?.setBackgroundColor(if (ppm >= threshold / 2) Color.parseColor("#FFB300") else Color.parseColor("#4CAF50"))
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun sendPushNotification(title: String, message: String) {
        val context = context ?: return
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "CO Alerts", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        notificationManager.notify(1, builder.build())
    }
}