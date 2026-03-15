package com.example.ailert

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class GuideFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCallEmergency = view.findViewById<Button>(R.id.btnCallEmergency)
        val btnCallPersonal = view.findViewById<Button>(R.id.btnCallPersonal)

        // Standard Emergency 112
        btnCallEmergency.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")
            startActivity(intent)
        }

        // Call the Personal Emergency Contact saved in Settings
        btnCallPersonal.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("AilertPrefs", Context.MODE_PRIVATE)
            val savedNumber = sharedPref.getString("emergency_number", "")

            if (!savedNumber.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$savedNumber")
                startActivity(intent)
            } else {
                Toast.makeText(context, "Please set a number in Settings first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}