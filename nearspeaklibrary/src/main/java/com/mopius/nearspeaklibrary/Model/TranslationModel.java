package com.mopius.nearspeaklibrary.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Appaya on 01.07.2015.
 */
public class TranslationModel implements Serializable {

    @SerializedName("text")
    private String text;

    @SerializedName("lang")
    private String lang;

    public String getText() {
        return text;
    }

    public String getLang() {
        return lang;
    }
}
