package com.example.taskmanagement

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingActivity : AppCompatActivity() {

    private lateinit var nightSwitch: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)

        nightSwitch = findViewById(R.id.nightSwitch)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Retrieve current night mode setting
        val isNightMode = sharedPreferences.getBoolean(getString(R.string.night_mode_key), false)
        nightSwitch.isChecked = isNightMode

        // Set the initial night mode based on the saved preference
        setNightMode(isNightMode)

        nightSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Update the night mode based on the switch state
            setNightMode(isChecked)
            // Save the state of the switch
            sharedPreferences.edit().putBoolean(getString(R.string.night_mode_key), isChecked).apply()
        }
    }

    private fun setNightMode(isNightMode: Boolean) {
        if (isNightMode) {
            // Enable dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            // Disable dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}