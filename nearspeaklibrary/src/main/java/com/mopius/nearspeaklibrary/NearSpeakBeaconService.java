package com.mopius.nearspeaklibrary;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.mopius.nearspeaklibrary.Model.TagModel;
import com.mopius.nearspeaklibrary.controller.WSR_ShowTagByHardwareID;
import com.mopius.nearspeaklibrary.interfaces.IWSRShowTagListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by Appaya on 01.07.2015.
 */
public class NearSpeakBeaconService extends Service implements BeaconConsumer, IWSRShowTagListener {

    protected static final String TAG = "BeaconService";
    protected static final long TIME_BETWEEN_SCANS = 5000;

    private BeaconManager beaconManager;
    private Region mRangingRegion;
    private List<String> mDiscoveredBeacons;

    @Override
    public void onCreate() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")); //Suppoert for Kontakt.io Beacons
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")); //Suppoert for Kontakt.io Beacons
        beaconManager.setForegroundBetweenScanPeriod(TIME_BETWEEN_SCANS);
        beaconManager.setBackgroundBetweenScanPeriod(30000);

        beaconManager.bind(NearSpeakBeaconService.this);
        mDiscoveredBeacons = new ArrayList<String>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
                    String UUID = ((Beacon) beacons.toArray()[0]).getId1().toString().replace("-", "").toUpperCase();
                    String majorID = ((Beacon) beacons.toArray()[0]).getId2().toString();
                    String minorID = ((Beacon) beacons.toArray()[0]).getId3().toString();

                    if (!mDiscoveredBeacons.contains(UUID + majorID + minorID)) {
                        // suitable beacon discovered, fetch complete tag infos from server now
                        new WSR_ShowTagByHardwareID(NearSpeakBeaconService.this).execute(UUID, majorID, minorID, Locale.getDefault().getLanguage());

                        // add beacon to list so that it wont get scanned again
                        mDiscoveredBeacons.add(UUID + majorID + minorID);
                    }
                }
            }
        });

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                // clear beacon list so they can be read again
                mDiscoveredBeacons.clear();
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            // declare region and start ranging and monitoring
            mRangingRegion = new Region("NearSpeak-Beacon-Search", Identifier.parse("CEFCC021-E45F-4520-A3AB-9D1EA22873AD"), null, null);
            beaconManager.startRangingBeaconsInRegion(mRangingRegion);
            beaconManager.startMonitoringBeaconsInRegion(mRangingRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the ranging for beacons
     */
    public void stopBeaconRanging() {
        try {
            beaconManager.stopRangingBeaconsInRegion(mRangingRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unbinds the beacon scan service
     */
    public void disconnectBeaconService() {
        try {
            beaconManager.unbind(NearSpeakBeaconService.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }

    @Override
    public void onDestroy() {
        stopBeaconRanging();
        disconnectBeaconService();
    }

    @Override
    public void onTagReceived(TagModel _tag) {
        if (_tag != null && _tag.getTranslation() != null && _tag.getTranslation().getText().length() > 0) {
            // shoot broadcast
            Intent bci = new Intent(NearSpeakManager.NEARSPEAK_BRAODCAST_TAG_RECEIVED);
            // put the received nearspeak tag as intent extra
            bci.putExtra(NearSpeakManager.NEARSPEAK_INTENT_EXTRA_TAG, _tag);
            sendBroadcast(bci);
        }
    }

    /**
     * This is a callback method which gets called from a webrequest async task
     *
     * @param _tags
     */
    @Override
    public void onMyTagsReceived(List<TagModel> _tags) {
        //nothing to do here, its handled in NearSpeakManager
    }
}
