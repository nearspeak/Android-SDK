package com.mopius.nearspeaklibrary;
/**
 * This is the parent activity which implements the NFC functionality
 */
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.mopius.nearspeaklibrary.controller.NdefReaderTask;
import com.mopius.nearspeaklibrary.interfaces.INdefReaderListener;

/**
 * Created by Appaya on 08.07.2015.
 */
public class NearSpeakAktivity extends ActionBarActivity implements INdefReaderListener {

    private NfcAdapter mNfcAdapter;
    protected NearSpeakManager mNearSpeakManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed).
         * Otherwise an IllegalStateException is thrown.
         */
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            setupForegroundDispatch(this, mNfcAdapter);
        }
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is
         * thrown as well.
         */
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            stopForegroundDispatch(this, mNfcAdapter);
        }

        super.onPause();
    }

    /**
     * @param activity The corresponding {@link android.app.Activity} requesting the foreground
     *                 dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        // FILTER
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        IntentFilter[] mFilters = {ndef, tag, tech};

        // Setup a tech list for all NfcF tags
        String[][] mTechLists = new String[][]{new String[]{NfcF.class.getName(), NfcA.class.getName(), NfcB.class.getName(),
                NfcV.class.getName(), IsoDep.class.getName(), MifareUltralight.class.getName(), MifareClassic.class.getName(),
                Ndef.class.getName(), NdefFormatable.class.getName()}};

        // Start searching
        adapter.enableForegroundDispatch(activity, pendingIntent, mFilters, mTechLists);
    }

    /**
     * @param activity The corresponding requesting to stop the
     *                 foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the
         * current activity instance. Instead of creating a new activity,
         * onNewIntent will be called. For more information have a look at the
         * documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to
         * the device.
         */

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            new NdefReaderTask(this).execute(tag);

        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {

                    new NdefReaderTask(this).execute(tag);
                    break;
                }
            }
        } else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {

                    new NdefReaderTask(this).execute(tag);
                    break;
                }
            }
        }
    }

    @Override
    public void onNdefGotRead(String _message) {
        if (_message.startsWith("nearspeak:") && _message.length() > 20 && mNearSpeakManager != null) {
            // its an online tag, go get it
            mNearSpeakManager.getTagByNearSpeakID(_message.substring(12));
        }
    }
}
