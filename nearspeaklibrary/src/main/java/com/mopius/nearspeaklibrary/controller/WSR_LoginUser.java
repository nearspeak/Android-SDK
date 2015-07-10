package com.mopius.nearspeaklibrary.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mopius.nearspeaklibrary.Model.TagModel;
import com.mopius.nearspeaklibrary.interfaces.IWSRLoginUserListener;
import com.mopius.nearspeaklibrary.interfaces.IWSRShowTagListener;
import com.mopius.nearspeaklibrary.utils.Constants;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Appaya on 01.07.2015.
 */
public class WSR_LoginUser extends AsyncTask<String, Integer, String> {

    public final static String CLASSNAME = "WSR_LoginUser";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static IWSRLoginUserListener mListener = null;

    public WSR_LoginUser(IWSRLoginUserListener listener) {
        mListener = listener;
    }

    protected String doInBackground(String... _request) {
        Log.i(CLASSNAME, "doInBackground");

        String authToken = "";
        try {
            OkHttpClient client = new OkHttpClient();

            String url = Constants.URL_CORESERVER + Constants.REQUEST_LOGIN;

            JSONObject requestJson = new JSONObject();
            try {
                requestJson.put("email", _request[0]);
                requestJson.put("password", _request[1]);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(JSON, requestJson.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String fullJSONString = response.body().string();

            //parse now the result
            JSONObject resultJson = null;
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            try {
                resultJson = new JSONObject(fullJSONString);
                authToken = resultJson.getString("auth_token");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authToken;
    }

    protected void onPostExecute(String _result) {
        Log.i(CLASSNAME, "onPostExecute");
        mListener.onUserLoginFinshed(_result);
    }
}
