package com.mopius.nearspeaklibrary.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mopius.nearspeaklibrary.Model.TagModel;
import com.mopius.nearspeaklibrary.interfaces.IWSRShowTagListener;
import com.mopius.nearspeaklibrary.utils.Constants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Appaya on 01.07.2015.
 */
public class WSR_ShowTagByHardwareID extends AsyncTask<String, Integer, TagModel> {

    public final static String CLASSNAME = "WSR_ShowTagByHardwareID";
    static IWSRShowTagListener mListener = null;

    public WSR_ShowTagByHardwareID(IWSRShowTagListener listener) {
        mListener = listener;
    }

    protected TagModel doInBackground(String... _request) {
        Log.i(CLASSNAME, "doInBackground");

        TagModel nearspeakTag = null;
        try {
            OkHttpClient client = new OkHttpClient();
            String url = Constants.URL_CORESERVER + Constants.REQUEST_GETTEXT_BY_HARDWARE;
            url += "?id=" + _request[0] + "&major=" + _request[1] + "&minor=" + _request[2] + "&type=ble-beacon" + "&lang=" + _request[3];

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            String fullJSONString = response.body().string();

            //parse now the result
            JSONObject json = null;
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            try {
                json = new JSONObject(fullJSONString);
                String tagJSONString = json.getJSONArray("tags").get(0).toString();
                try {
                    nearspeakTag = gson.fromJson(tagJSONString, TagModel.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            nearspeakTag = null;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nearspeakTag;
    }

    protected void onPostExecute(TagModel _result) {
        Log.i(CLASSNAME, "onPostExecute");
        mListener.onTagReceived(_result);
    }
}
