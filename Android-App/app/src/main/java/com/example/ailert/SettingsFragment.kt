package com.example.ailert

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance("https://ailert-a55ca-default-rtdb.asia-southeast1.firebasedatabase.app")
            .reference

        val etEmergencyContact = view.findViewById<EditText>(R.id.etEmergencyContact)
        val btnWindowUp = view.findViewById<Button>(R.id.btnWindowUp)
        val btnWindowDown = view.findViewById<Button>(R.id.btnWindowDown)
        val btnEmergencyCall = view.findViewById<Button>(R.id.btnEmergencyCall)
        val seekBarThreshold = view.findViewById<SeekBar>(R.id.seekBarThreshold)
        val tvThresholdValue = view.findViewById<TextView>(R.id.tvThresholdValue)

        // Persistent Storage for Emergency Contact
        val sharedPref = requireActivity().getSharedPreferences("AilertPrefs", Context.MODE_PRIVATE)
        etEmergencyContact.setText(sharedPref.getString("emergency_number", ""))

        etEmergencyContact.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                sharedPref.edit().putString("emergency_number", s.toString()).apply()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 1. Dynamic Threshold Control
        seekBarThreshold.max = 100

        // Sync slider and text label with current Firebase value
        database.child("trigger_threshold").get().addOnSuccessListener {
            val current = it.getValue(Int::class.java) ?: 50
            seekBarThreshold.progress = current
            tvThresholdValue.text = "$current PPM"
        }

        seekBarThreshold.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, fromUser: Boolean) {
                // Update numerical label in real-time
                tvThresholdValue.text = "$p PPM"

                // Update Firebase if the user moved the slider
                if (fromUser) {
                    database.child("trigger_threshold").setValue(p)
                }
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {
                Toast.makeText(context, "Threshold set to ${s?.progress} PPM", Toast.LENGTH_SHORT).show()
            }
        })

        // 2. Manual Window Overrides
        btnWindowUp.setOnClickListener {
            database.child("manual_control").setValue("up")
                .addOnSuccessListener { Toast.makeText(context, "Closing Window", Toast.LENGTH_SHORT).show() }
        }

        btnWindowDown.setOnClickListener {
            database.child("manual_control").setValue("down")
                .addOnSuccessListener { Toast.makeText(context, "Opening Window", Toast.LENGTH_SHORT).show() }
        }

        btnEmergencyCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")
            startActivity(intent)
        }
    }
}