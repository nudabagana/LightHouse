package com.a1104.lighthouse.Weather;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.a1104.lighthouse.Fragments.TaskScreenFragment;
import com.a1104.lighthouse.Fragments.WeatherScreenFragment;
import com.a1104.lighthouse.MainActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WeatherInfo {

    private static WeatherInfo self;
    private RequestQueue queue;
    private static final String URL = "http://api.openweathermap.org/data/2.5/";
    private static final String WEATHER = "weather";
    private static final String FORECAST = "forecast";
    private static final String API_KEY = "e60a12b1df00f703f2b6b32ebf387cc0";
    private static MainActivity mainActivity;

    private WeatherInfo(Context ctx)
    {
        this.queue = Volley.newRequestQueue(ctx);
    }

    private WeatherInfo()
    {
        RequestQueue queue = Volley.newRequestQueue(null);
    }

    public static WeatherInfo getInstance(Context ctx)
    {
        mainActivity=(MainActivity)ctx;
        if (self == null)
        {
            self = new WeatherInfo(ctx);
        }
        return self;
    }

    public void makeRequest(String cityName, final TaskScreenFragment fragment)
    {
        String requestURL = URL + WEATHER + "?q=" + cityName + "&appid=" + API_KEY + "&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.d("RESPONSE", response.toString());
//                        mainActivity.debugJsonResponse(response.toString());
                        fragment.SetWeather(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("ERROR", error.toString());
                       // mainActivity.debugJsonResponse(error.toString());
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void makeRequest(String cityName, final WeatherScreenFragment fragment)
    {
        String requestURL = URL + FORECAST + "?q=" + cityName + "&appid=" + API_KEY + "&units=metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, requestURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.d("RESPONSE", response.toString());
//                        mainActivity.debugJsonResponse(response.toString());
                        fragment.SetWeather(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("ERROR", error.toString());
                        // mainActivity.debugJsonResponse(error.toString());
                    }
                });
        queue.add(jsonObjectRequest);
    }


}
