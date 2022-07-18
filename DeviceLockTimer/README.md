# Steps to include the Device Lock Timer module
In order to utilise the device lock timer component within your project you will need to utilise the `DeviceLockTimer` Composable that is included in this library module

To include the Device Lock Timer library in your project add the following to your project's dependencies:

`implementation project(path: ':DeviceLockTimer')`

# Setup guide for the Device Lock Timer module
**The following steps assumes your application is written using Jetpack Compose and are intending to use the Device Lock Timer Composable within a screen Composable you are writing. It is also assumed that you are utilising the Koin framework for DI**

1. You must include the `androidx.compose.runtime:runtime-livedata` dependency as the view model exposes the timer state as live data. Using this library will take care of updating the timer state
2. In your `startKoin` call within your App class, be sure to add the `timerModules` module to use the Device Lock Timer component.
3. Within your screen composable, call the `observeAsState()` live data extension function (provided by the `androidx.compose.runtime:runtime-livedata` library) on an instance of the DeviceTimerViewModel's `timerState` live data property and store it in a variable which you will then pass to the `DeviceLockTimer` composable 
4. Within your screen composable, call the `startTimer()` method of the view model. This will start the device lock timer business logic and update the live data which you have setup an observer for in the previous step

You're all setup and the Device Lock Timer will countdown to 11PM. Giving a warning status 3 hours before as per the problem statement