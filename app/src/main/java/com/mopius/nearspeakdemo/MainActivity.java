package com.mopius.nearspeakdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mopius.nearspeaklibrary.Model.TagModel;
import com.mopius.nearspeaklibrary.NearSpeakAktivity;
import com.mopius.nearspeaklibrary.NearSpeakManager;
import com.mopius.nearspeaklibrary.interfaces.NearSpeakOperationsListener;

import java.util.List;


public class MainActivity extends NearSpeakAktivity implements View.OnClickListener, NearSpeakOperationsListener {

    private TextView mTextView;
    private EditText mEditNearSpeakID, mEditUsername, mEditPassword;
    private Button mButtonLogin, mButtonShowMyTags;
    private ImageButton mButtonSearch;
    private Switch mSwitchBeaconScanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNearSpeakManager = new NearSpeakManager(this, this, true);

        mTextView = (TextView) findViewById(R.id.textview);
        mEditNearSpeakID = (EditText) findViewById(R.id.edit_nearspeakid);
        mEditUsername = (EditText) findViewById(R.id.edit_username);
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        mButtonSearch = (ImageButton) findViewById(R.id.button_search);
        mButtonLogin = (Button) findViewById(R.id.button_login);
        mButtonShowMyTags = (Button) findViewById(R.id.button_showmytags);
        mSwitchBeaconScanning = (Switch) findViewById(R.id.switch_beaconscanning);

        mButtonSearch.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
        mButtonShowMyTags.setOnClickListener(this);

        mSwitchBeaconScanning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    mNearSpeakManager.startBeaconScanning();
                    mTextView.setText("scanning for beacons...");
                } else {
                    mNearSpeakManager.stopBeaconScanning();
                    mTextView.setText("beacon scan stopped");
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            mNearSpeakManager.getTagByNearSpeakID("ab1b78cd26a8");
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onDestroy() {

        // shut down the nearspeak manager properly
        if (mNearSpeakManager != null)
            mNearSpeakManager.shutDownManager();

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search:
                mTextView.setText("searching...");
                mNearSpeakManager.getTagByNearSpeakID(mEditNearSpeakID.getText().toString());
                break;
            case R.id.button_login:
                mTextView.setText("login...");
                mNearSpeakManager.getAuthToken(mEditUsername.getText().toString(), mEditPassword.getText().toString());
                break;
            case R.id.button_showmytags:
                mTextView.setText("loading tags...");
                String res = mNearSpeakManager.getMyTags();
                if (res.length() > 0) {
                    mTextView.setText(mTextView.getText().toString() + "\n" + res);
                }
                break;
        }
    }

    @Override
    public void onNearSpeakTagReceived(TagModel _tag) {
        if (_tag != null) {
            mTextView.setText(mTextView.getText().toString() + "\n" + System.currentTimeMillis() + ": " + _tag.getName().getText());
        } else {
            mTextView.setText(mTextView.getText().toString() + "\nRequest failed");
        }
    }

    @Override
    public void onNearSpeakTagListReceived(List<TagModel> _tags) {
        if (_tags != null) {
            for (TagModel tm : _tags) {
                mTextView.setText(mTextView.getText().toString() + "\n" + System.currentTimeMillis() + ": " + tm.getName().getText());
            }
        } else {
            mTextView.setText(mTextView.getText().toString() + "\nRequest failed");
        }
    }

    @Override
    public void onUserLoginFinished(String _authToken) {
        if (_authToken != null && _authToken.length() > 0) {
            mTextView.setText(mTextView.getText().toString() + "\n" + System.currentTimeMillis() + ": " + _authToken);
        }
    }
}
