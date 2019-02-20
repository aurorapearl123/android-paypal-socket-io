package com.example.ian.paypalpayment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.ian.paypalpayment.Adapter.ClientAdapter;
import com.example.ian.paypalpayment.Config.Config;
import com.example.ian.paypalpayment.Model.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class RICRest
{
    private static RICRest ourInstance;
    private AsyncHttpClient client;
    private String token;
    public static final String TOKEN = "token";


    public static RICRest getInstance()
    {

        if(ourInstance == null)
            ourInstance = new RICRest();
        return ourInstance;
    }

    private RICRest()
    {
        if(client == null) {
            client = new AsyncHttpClient();
            client.addHeader("accept", "application/json");
            int one_minute = 1 * 60 * 1000;
            client.setConnectTimeout(one_minute);
            client.setResponseTimeout(one_minute);
        }

    }

    public void addAuthHeaders (String token) {
        if(token != null)
            client.addHeader("Authorization", "Bearer " + token);
    }

    public AsyncHttpClient getClient()
    {
        return client;
    }

    public void login(String username, String password, final Context context) {
        Log.wtf("LOGIN-ME", "Please login me please");
        HashMap<String, String> param = new HashMap<>();
        param.put("email", username);
        param.put("password", password);

        RequestParams params = new RequestParams(param);
        client.post(Config.LOGIN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject authObject = new JSONObject(new String(responseBody));
                    Log.wtf("LOGIN-SUCCESS", authObject.toString()+"");
//                    user_name = (String) authObject.get("user_nicename");
                    JSONObject data = (JSONObject) authObject.get("data");
                    token = (String) data.get("token");
                    Log.wtf("TOKEN-STRING", token+"");
//                    email = (String) authObject.get("user_email");
//                    JSONObject user_details = (JSONObject) authObject.get("user_details");
//                    first_name = (String) user_details.get("first_name");
//                    last_name = (String) user_details.get("last_name");
//                    middle_name = (String) user_details.get("display_name");
//                    saveUser(user_name, email, token, first_name, last_name, middle_name);
                    //client.addAuthHeaders(token);
                    RICRest.getInstance().savePreferences(context, TOKEN, token);
                    Intent intent = new Intent(context, HomePageActivity.class);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.wtf("JSON-ERROR", "json error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                try {
                    JSONObject authObject = new JSONObject(new String(responseBody));
                    String message = (String) authObject.get("message");
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    Log.wtf("FAILED", message+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void clientList(final ArrayList<Client> clientArrayList, final ClientAdapter clientAdapter, int page)
    {
        client.get(Config.BASE_URL + "clients?page="+page, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Gson gson = new GsonBuilder().create();
                    JSONObject authObject = new JSONObject(new String(responseBody));
                    JSONArray data = authObject.getJSONArray("data");
                    List<Client> clients = Arrays.asList(gson.fromJson(new String(String.valueOf(data)), Client[].class));
                    String image = "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg";
                    Client client = new Client();
                    for(int i = 0; i < clients.size(); i++) {


                       client = new Client(clients.get(i).getId(), clients.get(i).getFirst_name(), clients.get(i).getMiddle_name(), clients.get(i).getLast_name(), clients.get(i).getPhone(), clients.get(i).getEmail(), image);
                        clientArrayList.add(client);
                    }
                    //clientArrayList.add(clients);
                    clientAdapter.notifyDataSetChanged();
                    //Arrays.asList(gson.fromJson(new String(responseBody), Product[].class));
                    Log.wtf("CLIENT-DATA", clients.toString()+"");
                    //clientArrayList = clients;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.wtf("LOGIN-FAIL", "Error this");
                    Log.wtf("LOGIN-FAIL", e.toString()+"");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.wtf("FAILED-FOR-CLIENT", responseBody.toString()+"");
            }
        });
    }

    /**
     * @param mContext
     * @param key
     * @param value
     */
    public void savePreferences(Context mContext, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    /**
     * @param context
     * @param keyValue
     * @return
     */
    public String getPreferences(Context context, String keyValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(keyValue, "");
    }

    /**
     * @param mContext
     */
    public void removeAllSharedPreferences(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    public void searchToServer(final ArrayList<Client> clientArrayList, final ClientAdapter clientAdapter, String search) {
        //clientArrayList.add([]);
        //clientAdapter.notifyDataSetChanged();
        client.get(Config.BASE_URL + "clients-search/"+search, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Gson gson = new GsonBuilder().create();
                    JSONObject object = new JSONObject(new String(responseBody));
                    JSONArray data = object.getJSONArray("data");
                    List<Client> clients = Arrays.asList(gson.fromJson(new String(String.valueOf(data)), Client[].class));
                    clientArrayList.clear();
                    String image = "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg";
                    Client client = new Client();
                    for(int i = 0; i < clients.size(); i++) {


                        client = new Client(clients.get(i).getId(), clients.get(i).getFirst_name(), clients.get(i).getMiddle_name(), clients.get(i).getLast_name(), clients.get(i).getPhone(), clients.get(i).getEmail(), image);
                        clientArrayList.add(client);
                    }
                    //clientArrayList.add(clients);
                    clientAdapter.notifyDataSetChanged();

                    Log.wtf("RESUTL-SEARCH", data.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.wtf("RESUTL-ERROR", responseBody.toString()+"");
            }
        });
    }

}
