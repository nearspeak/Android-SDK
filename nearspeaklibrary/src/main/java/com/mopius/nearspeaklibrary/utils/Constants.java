package com.mopius.nearspeaklibrary.utils;

/**
 * Created by Appaya on 02.07.2015.
 */
public class Constants {

    public static final String URL_CORESERVER = "http://nearspeak.cloudapp.net/api/v1/";

    // Request types
    public static final String REQUEST_GETTEXT_BY_NEARSPEAKID = "tags/show";
    public static final String REQUEST_GETTEXT_BY_HARDWARE = "tags/showByHardwareId";
    public static final String REQUEST_CREATETEXT = "tags/create";
    public static final String REQUEST_LOGIN = "login/get_auth_token";
    public static final String REQUEST_FACEBOOK_LOGIN = "login/sign_in_with_facebook";
    public static final String REQUEST_ADDHARDWAREID = "tags/addHardwareIdToTag";
    public static final String REQUEST_MYTAGS = "tags/showMyTags";

    public static final String ERROR_NO_INTERNET_CONNECTION_AVAILABLE = "ERROR_NO_INTERNET_CONNECTION_AVAILABLE";
}
