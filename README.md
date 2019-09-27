# Drive Science Testing Apps

## Run the app on a test Android device

https://developer.android.com/studio/debug

Once the device is running, you can attach a debugger to it with Android Studio:

`Run > Attach Debugger to Android Process`

It helps to recompile the trip tracker SDK locally:

```
cd ~/code/root-android-trip-tracker
./gradlew clean assmebleRelease
```

If you want to save yourself the effort of taking a real trip, there are a number of places where you can hack up the trip tracker SDK to make it behave differently:

- Modify `com.joinroot.roottriptracking.sensorintegration.RecognitionActivity.isDriving()` to return true for something other than `activityType == Activity.DRIVING`. For example, `activityType == Activity.OTHER` or `activityType != Activity.STILL` should enable you to start tracking a trip by shaking the device around a bit at your desk. 
- Modify `com.joinroot.roottriptracking.environment.DriveScienceClientEnvironmentParameters.getTripCompletedGracePeriodSeconds()` to reduce how long it takes for a trip to register as complete.
