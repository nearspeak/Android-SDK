package com.mopius.nearspeaklibrary.interfaces;

import com.mopius.nearspeaklibrary.Model.TagModel;

import java.util.List;

/**
 * Created by Appaya on 01.07.2015.
 */
public interface IWSRLoginUserListener {
    /**
     * this is a callback method which gets called from a webrequest async task after sending login credentials to the NearSpeak backend
     *
     * @param _authToken The received authentication token
     */
    public abstract void onUserLoginFinshed(String _authToken);
}
