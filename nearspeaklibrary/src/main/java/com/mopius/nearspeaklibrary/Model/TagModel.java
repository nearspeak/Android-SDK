package com.mopius.nearspeaklibrary.Model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Appaya on 01.07.2015.
 */
public class TagModel implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("tag_identifier")
    private String tagIdentifier;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("text_url")
    private String textUrl;

    @SerializedName("translation")
    private TranslationModel translation;

    @SerializedName("name")
    private TagNameModel name;

    public String getId() {
        return id;
    }

    public String getTagIdentifier() {
        return tagIdentifier;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTextUrl() {
        return textUrl;
    }

    public TranslationModel getTranslation() {
        return translation;
    }

    public TagNameModel getName() {
        return name;
    }
}
