package com.mopius.nearspeaklibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mopius.nearspeaklibrary.Model.TagModel;
import com.mopius.nearspeaklibrary.controller.WSR_LoginUser;
import com.mopius.nearspeaklibrary.controller.WSR_ShowMyTags;
import com.mopius.nearspeaklibrary.controller.WSR_ShowTagByNearSpeakID;
import com.mopius.nearspeaklibrary.interfaces.IWSRLoginUserListener;
import com.mopius.nearspeaklibrary.interfaces.IWSRShowTagListener;
import com.mopius.nearspeaklibrary.interfaces.NearSpeakOperationsListener;
import com.mopius.nearspeaklibrary.utils.Utils;

import java.util.List;
import java.util.Locale;

/**
 * This is the main controller/manager from the nearspeak library. Instantiate this in your main activity.
 */
public class NearSpeakManager implements IWSRShowTagListener, IWSRLoginUserListener {

    public static final String NEARSPEAK_INTENT_EXTRA_TAG = "NEARSPEAK_BRAODCAST_BEACON_FOUND_EXTRA";
    public static final String NEARSPEAK_BRAODCAST_TAG_RECEIVED = "NEARSPEAK_BRAODCAST_BEACON_FOUND";

    private Context mContext;
    private NearSpeakTagBroadcastReceiver mNearSpeakTagBroadcastReceiver;
    private Intent mBeaconServiceIntent;
    private NearSpeakOperationsListener mListener;

    private String mAuthToken;

    /**
     * This is the main controller/manager from the nearspeak library. Instantiate this in your main activity.
     * @param _context Context of the application
     * @param _listener Add a listener to receive nearspeak tags
     * @param _startBeaconScan Choose if bluetooth le aka beacon scanning should start right away
     */
    public NearSpeakManager(Context _context, NearSpeakOperationsListener _listener, boolean _startBeaconScan) {
        mContext = _context;
        mBeaconServiceIntent = new Intent(_context, NearSpeakBeaconService.class);
        mListener = _listener;

        if (_startBeaconScan)
            _context.startService(mBeaconServiceIntent);

        // register receiver
        if (mNearSpeakTagBroadcastReceiver == null)
            mNearSpeakTagBroadcastReceiver = new NearSpeakTagBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(NearSpeakManager.NEARSPEAK_BRAODCAST_TAG_RECEIVED);
        mContext.registerReceiver(mNearSpeakTagBroadcastReceiver, intentFilter);
    }

    /**
     * starts the scanning for bluetooth beacons
     */
    public void startBeaconScanning() {
        //start beacon scan services
        if (!Utils.isMyServiceRunning(mContext, NearSpeakBeaconService.class) && mBeaconServiceIntent != null) {
            mBeaconServiceIntent = new Intent(mContext, NearSpeakBeaconService.class);
            mContext.startService(mBeaconServiceIntent);
        }
    }

    /**
     * stops the scanning for bluetooth beacons
     */
    public void stopBeaconScanning() {
        //stop beacon scan services
        if (Utils.isMyServiceRunning(mContext, NearSpeakBeaconService.class) && mBeaconServiceIntent != null) {
            mContext.stopService(mBeaconServiceIntent);
        }
    }

    /**
     * shuts down the manager and all services
     */
    public void shutDownManager() {
        //stop beacon scan services if started
        stopBeaconScanning();

        // unregister for broadcasts
        mContext.unregisterReceiver(mNearSpeakTagBroadcastReceiver);
    }

    /**
     * method for fetching the authentication token from backend (for webservice calls like "my tags)
     *
     * @param _email    email address of the user
     * @param _password password of the user
     */
    public void getAuthToken(String _email, String _password) {
        new WSR_LoginUser(NearSpeakManager.this).execute(_email, _password);
    }

    public String getMyTags() {
        if (mAuthToken != null && mAuthToken.length() > 0) {
            new WSR_ShowMyTags(NearSpeakManager.this).execute(mAuthToken);
            return "";
        } else {
            return "please login and receiver auth token first!";
        }
    }

    /**
     * With this methode you can query for a NearSpeak tag by a NearSpeak tag ID
     *
     * @param _id the nearspeak tag id to search for
     */
    public void getTagByNearSpeakID(String _id) {
        new WSR_ShowTagByNearSpeakID(NearSpeakManager.this).execute(_id, Locale.getDefault().getLanguage());
    }

    @Override
    public void onTagReceived(TagModel _tag) {
        try {
            if (_tag != null && _tag.getTranslation() != null && _tag.getTranslation().getText().length() > 0) {
                mListener.onNearSpeakTagReceived(_tag);
            }
        } catch (Exception _e) {
            _e.printStackTrace();
        }
    }

    /**
     * this is a callback method which gets called from a webrequest async task
     *
     * @param _tags list of found tags
     */
    @Override
    public void onMyTagsReceived(List<TagModel> _tags) {
        mListener.onNearSpeakTagListReceived(_tags);
    }

    /**
     * this is a callback method which gets called from a webrequest async task
     *
     * @param _authToken the received token from the nearspeak backend
     */
    @Override
    public void onUserLoginFinshed(String _authToken) {
        mAuthToken = _authToken;
        mListener.onUserLoginFinished(_authToken);
    }

    /**
     * this broadcast receiver is used for receiving nearspeak tags sent by the service
     */
    private class NearSpeakTagBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NearSpeakManager.NEARSPEAK_BRAODCAST_TAG_RECEIVED)) {
                // Do stuff - maybe update my view based on the changed DB contents
                TagModel tag = (TagModel) intent.getSerializableExtra(NearSpeakManager.NEARSPEAK_INTENT_EXTRA_TAG);
                mListener.onNearSpeakTagReceived(tag);
            }
        }
    }
}
