package com.mopius.nearspeaklibrary.interfaces;

import com.mopius.nearspeaklibrary.Model.TagModel;

import java.util.List;

/**
 * Created by Appaya on 01.07.2015.
 */
public interface IWSRShowTagListener {
    /**
     * this is a callback method which gets called from a webrequest async task when fetching a NearSpeak tag from backend
     *
     * @param _tag
     */
    public abstract void onTagReceived(TagModel _tag);

    /**
     * this is a callback method which gets called from a webrequest async task when fetching a list of NearSpeak tags from backend
     *
     * @param _tags
     */
    public abstract void onMyTagsReceived(List<TagModel> _tags);
}
