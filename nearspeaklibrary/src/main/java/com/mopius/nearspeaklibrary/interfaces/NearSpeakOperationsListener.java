package com.mopius.nearspeaklibrary.interfaces;

import com.mopius.nearspeaklibrary.Model.TagModel;

import java.util.List;

/**
 * Created by Appaya on 06.07.2015.
 */
public interface NearSpeakOperationsListener {
    /**
     * callback method used for notifying when tag received from CMS
     *
     * @param _tag the received tag or null if an error happened
     */
    public void onNearSpeakTagReceived(TagModel _tag);

    /**
     * callback method used for notifying when tags received from CMS
     *
     * @param _tags the received list of tags or null if an error happened
     */
    public void onNearSpeakTagListReceived(List<TagModel> _tags);

    /**
     * callback methode used for notifying when authentication token was received from backend
     *
     * @param _authToken
     */
    public void onUserLoginFinished(String _authToken);
}
