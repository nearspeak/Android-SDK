# Nearspeak SDK for Android

The Android SDK for Nearspeak.

## Features

* Scanning for Nearspeak NFC tags
* Query for Nearspeak iBeacons
* Fetch Nearspeak tag informations from the Nearspeak server

## Requirements
- Android 4.3+
- Android Studio

## Integration

#### Required permissions

Add the following permissions to your `AndroidManifest.xml`:
```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.NFC" />
```
Additionally we recommend to add the feature for BluetoothLE
```xml
<uses-feature android:name="android.hardware.bluetooth_le" />
```

## Usage

To interact with the Nearspeak SDK you have to instantiate the `NearSpeakManager` in your launch activity:

```java
NearSpeakManager mNearSpeakManager = new NearSpeakManager(this, this, true);
```

In order to receive Nearspeak tags from the Nearspeak CMS your activity/class has to implement the `NearSpeakOperationsListener` interface.


### Start discovering for Nearspeak NFC tags

Simply derive your launch Activity from `NearSpeakActivty` and implement the `NearSpeakOperationsListener` interface.


### Start discovering for Nearspeak beacons

Simply instantiate the `NearSpeakManager` with `true` as third parameter and implement the `NearSpeakOperationsListener` interface.