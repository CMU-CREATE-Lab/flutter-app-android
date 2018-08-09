Flutter For Android Devices
===========================


Overview
--------
**Flutter** Working from our successful Arts & Bots project, we are developing a new creativity-oriented educational technology targeting elementary education. This application is designed for Android OS (minimum SDK version 18).


Development Environment
-----------------------
To grab the repository, be sure to perform a ```git clone --recursive``` as this project utilizes the [MelodySmart Module for Android](https://github.com/CMU-CREATE-Lab/melodysmart-module-android). Android Studio should link the module properly upon first launching. The library requires ```minSdkVersion``` of at least 18.

This document was last written for Android Studio version 2.2.0 using Gradle version 2.14.1.


Using UI/Application Exerciser Monkey
-------------------------------------
This is a great way to test the user interface of Flutter for rigorous testing.  The best way to get started is to connect the tablet to Android Studio, and then run this adb command: ```adb shell monkey -p org.cmucreatelab.flutter_android -v 5000```.  This runs 5000 various events and displays if the application throws an error.  Please go to the [Android Developers page for Monkey](https://developer.android.com/studio/test/monkey) for more information.
