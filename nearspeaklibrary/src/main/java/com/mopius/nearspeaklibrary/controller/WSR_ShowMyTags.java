package com.mopius.nearspeaklibrary.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mopius.nearspeaklibrary.Model.TagModel;
import com.mopius.nearspeaklibrary.interfaces.IWSRShowTagListener;
import com.mopius.nearspeaklibrary.utils.Constants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Appaya on 01.07.2015.
 */
public class WSR_ShowMyTags extends AsyncTask<String, Integer, List<TagModel>> {

    public final static String CLASSNAME = "WSR_ShowMyTags";
    static IWSRShowTagListener mListener = null;

    public WSR_ShowMyTags(IWSRShowTagListener listener) {
        mListener = listener;
    }

    protected List<TagModel> doInBackground(String... _request) {
        Log.i(CLASSNAME, "doInBackground");

        List<TagModel> nearspeakTag = new ArrayList<TagModel>();
        try {
            Type listOfTestObject = new TypeToken<List<TagModel>>() {
            }.getType();

            OkHttpClient client = new OkHttpClient();
            String url = Constants.URL_CORESERVER + Constants.REQUEST_MYTAGS;
            url += "?auth_token=" + _request[0];

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
                String tagJSONString = json.getJSONArray("tags").toString();
                try {
                    nearspeakTag = (List<TagModel>) gson.fromJson(tagJSONString, listOfTestObject);
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

    protected void onPostExecute(List<TagModel> _result) {
        Log.i(CLASSNAME, "onPostExecute");
        mListener.onMyTagsReceived(_result);
    }
}
