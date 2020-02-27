package com.joinroot.kotlindemo

import com.joinroot.roottriptracking.BuildConfig
import com.joinroot.roottriptracking.RootTripTracking
import com.joinroot.roottriptracking.environment.Environment
import com.joinroot.roottriptracking.services.ITripLifecycleHandler
import kotlinx.android.synthetic.main.activity_main.*

val MainActivity.tripTracker: RootTripTracking by lazy { RootTripTracking.getInstance() }

fun MainActivity.showVersionText() {
    versionText.text = getString(R.string.version_footer, BuildConfig.SDK_VERSION)
}

fun MainActivity.setupTripTracker() {
    tripTracker.initialize(this, clientId, Environment.STAGING)
    tripTracker.setTripLifecycleHandler(object: ITripLifecycleHandler {
        override fun onTripEnded(tripId: String?) {
            appendLog("Trip ended: $tripId")
        }

        override fun onTripStarted(tripId: String?) {
            appendLog("Trip started: $tripId")
        }
    })
}

fun MainActivity.resumeIfConfigured() {
    if (tripTracker.configuredToAutoActivate())  {
        activatedSwitch.isChecked = true
        appendLog("Resuming Activation")
    }
}

fun MainActivity.createDriver(driverId: String) {
    tripTracker.createDriver(driverId, null, null, object: RootTripTracking.ICreateDriverRequestHandler{
        override fun onFailure(error: String?) {
            appendLog("Error: ${error ?: "Unknown" }")
            hideRegisteredDriver()
            registeredDriverId = null
        }

        override fun onSuccess(driverId: String?) {
            if (driverId != null) {
                registeredDriverId = driverId
                showRegisteredDriver(driverId)
                appendLog("Driver registered!")
            } else {
                hideRegisteredDriver()
            }
        }
    })
}

fun MainActivity.activate() {
    tripTracker.activate(
        this,
        registeredDriverId,
        object: RootTripTracking.ITripTrackingActivateSuccessHandler {
            override fun onSuccess() {
                activatedSwitch.isChecked = true
                appendLog("Activated")
            }

            override fun onFailure(error: String?) {
                appendLog("Error: ${error ?: "Unknown" }")
                activatedSwitch.isChecked = false
            }
        }
    )
}

fun MainActivity.deactivate() {
    tripTracker.deactivate(this)
    appendLog("Deactivated")
}

