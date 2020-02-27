package com.joinroot.kotlindemo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
// https://kotlinlang.org/docs/tutorials/android-plugin.html

class MainActivity : AppCompatActivity() {

    val clientId = "d2ca8c3d33b7985c4b8d0fc8f"
    var registeredDriverId: String?
        get() {
            return preferences.getString(registeredDriverPreferencesKey, null)
        }
        set(value) {
            val editor = preferences.edit()
            editor.putString(registeredDriverPreferencesKey, value)
            editor.apply()
        }

    private val registeredDriverPreferencesKey = "REGISTERED_DRIVER_ID"
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = getSharedPreferences("DriveScienceDemo", Context.MODE_PRIVATE)

        setupTripTracker()
        setupInitialState()
        resumeIfConfigured()
    }

    private fun setupInitialState() {
        showVersionText()
        if (registeredDriverId != null) {
            // https://kotlinlang.org/docs/reference/null-safety.html#safe-calls
            registeredDriverId?.let { showRegisteredDriver(it) }
        } else {
            hideRegisteredDriver()
        }
    }

    fun showRegisteredDriver(driverId: String) {
        isDriverRegisteredTextView.text = getString(R.string.driver_registered_affirmitive)
        registeredDriverTextView.text = getString(R.string.registered_driver_format, driverId)
        registeredDriverTextView.visibility = View.VISIBLE
        activatedSwitch.visibility = View.VISIBLE
    }

    fun hideRegisteredDriver() {
        isDriverRegisteredTextView.text = getString(R.string.driver_registered_negative)
        registeredDriverTextView.text = ""
        registeredDriverTextView.visibility = View.INVISIBLE
        activatedSwitch.visibility = View.INVISIBLE
    }

    fun appendLog(message: String) {
        logText.append("\n" + message)
    }

    /*************************/
    // UI Actions
    /*************************/

    @Suppress("UNUSED_PARAMETER")
    fun registerDriverButtonPressed(button: View) {
        // create driver
        if (driverIdInput.text.isNotEmpty()) {
            createDriver(driverIdInput.text.toString())
            hideKeyboard(this)
        } else {
            Toast.makeText(this, "Unable to register driver without ID!", Toast.LENGTH_SHORT).show()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun copyButtonPressed(button: View) {
        copyToClipboard(this, logText.text.toString())
        Toast.makeText(this, "Coped!", Toast.LENGTH_SHORT).show()
    }

    @Suppress("UNUSED_PARAMETER")
    fun clearButtonPressed(button: View) {
        logText.text.clear()
    }

    fun activationSwitchToggled(switch: View) {
        if (switch is Switch) {
             if (switch.isChecked) activate() else deactivate()
        }
    }
}
