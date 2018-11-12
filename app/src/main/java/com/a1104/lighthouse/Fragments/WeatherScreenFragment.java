package com.a1104.lighthouse.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a1104.lighthouse.MainActivity;
import com.a1104.lighthouse.R;
import com.a1104.lighthouse.Weather.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherScreenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView tempCurrent;
    private TextView humidCurrent;
    private TextView feelsLikeCurrent;
    private TextView afterHours;
    private TextView afterHoursTemp;
    private TextView dateCurrent;
    private TextView datePlusOne;
    private TextView datePlusTwo;
    private TextView datePlusThree;
    private TextView tempPlusOne;
    private TextView tempPlusTwo;
    private TextView tempPlusThree;
    private ImageView imgCurrent;
    private ImageView imgAfterHours;
    private ImageView imgPlusOne;
    private ImageView imgPlusTwo;
    private ImageView imgPlusThree;

    private int hoursState = 1;
    private ArrayList<Integer> hoursTempList;
    private ArrayList<Integer> imgTempList;

    private SimpleDateFormat df;
    private static final String dateFormat = "dd-MMM-yyyy";

    public WeatherScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherScreenFragment newInstance(/*String param1, String param2*/) {
        WeatherScreenFragment fragment = new WeatherScreenFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_screen_layout, container, false);

        hoursTempList = new ArrayList<Integer>();
        imgTempList = new ArrayList<Integer>();

        tempCurrent = view.findViewById(R.id.weather_current_temp);
        humidCurrent = view.findViewById(R.id.weather_current_humid);
        feelsLikeCurrent = view.findViewById(R.id.weather_current_feels);
        afterHours = view.findViewById(R.id.weather_after_hours);
        afterHoursTemp = view.findViewById(R.id.weather_after_temp);
        dateCurrent = view.findViewById(R.id.weather_current_date);
        datePlusOne = view.findViewById(R.id.weather_date_plus_one);
        datePlusTwo = view.findViewById(R.id.weather_date_plus_two);
        datePlusThree = view.findViewById(R.id.weather_date_plus_three);
        tempPlusOne = view.findViewById(R.id.weather_temp_plus_one);
        tempPlusTwo = view.findViewById(R.id.weather_temp_plus_two);
        tempPlusThree = view.findViewById(R.id.weather_temp_plus_three);
        imgCurrent = view.findViewById(R.id.weather_current_image);
        imgAfterHours = view.findViewById(R.id.weather_after_image);
        imgPlusOne = view.findViewById(R.id.weather_image_plus_one);
        imgPlusTwo = view.findViewById(R.id.weather_image_plus_two);
        imgPlusThree = view.findViewById(R.id.weather_image_plus_three);


        Date d = Calendar.getInstance().getTime();

        df = new SimpleDateFormat(dateFormat);
        String formattedDate = df.format(d);
        dateCurrent.setText(formattedDate);

        Calendar c = Calendar.getInstance();
        try {
            c.setTime(df.parse(dateCurrent.getText().toString()));
            c.add(Calendar.DATE, 1);  // number of days to add
            datePlusOne.setText(df.format(c.getTime()));  // dt is now the new date
            c.add(Calendar.DATE, 1);  // number of days to add
            datePlusTwo.setText(df.format(c.getTime()));  // dt is now the new date
            c.add(Calendar.DATE, 1);  // number of days to add
            datePlusThree.setText(df.format(c.getTime()));  // dt is now the new date
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LinearLayout layout = view.findViewById(R.id.weather_click_layout);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ToggleAfterHours();
            }

        });

        String city = ((MainActivity)getContext()).GetCityName();
        if (city != null)
        {
            WeatherInfo.getInstance(getContext()).makeRequest(city,this);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            ((MainActivity)context).SetWeatherFragment(this);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void SetLocationInfo(String city, String country)
    {
        WeatherInfo.getInstance(getContext()).makeRequest(city,this);
    }

    public void SetWeather(JSONObject object)
    {
        int temp = 17; // << set temp
        try {
            JSONArray arr = object.getJSONArray("list");

            // current
            JSONObject obj = arr.getJSONObject(0);
            double temperature = obj.getJSONObject("main").getDouble("temp");
            temp = (int)temperature;
            int humid = (int)obj.getJSONObject("main").getDouble("humidity");
            int feelsLike = (int) (temperature +  obj.getJSONObject("main").getDouble("temp_kf"));
            this.tempCurrent.setText(Integer.toString(temp) + " °C");
            this.humidCurrent.setText(Integer.toString(humid) + "%");
            this.feelsLikeCurrent.setText(Integer.toString(feelsLike) + " °C");

            String sky = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            if (sky.equals("Clear") )
            {
                this.imgCurrent.setImageResource(R.drawable.ic_wb_sunny);
            }
            else if (sky.equals("Rain"))
            {
                this.imgCurrent.setImageResource(R.drawable.ic_icons8_rain_50);
            }
            else
            {
                this.imgCurrent.setImageResource(R.drawable.ic_cloud);
            }

            //One plus
            obj = arr.getJSONObject(8);
            temperature = obj.getJSONObject("main").getDouble("temp");
            temp = (int)temperature;

            this.tempPlusOne.setText(Integer.toString(temp) + " °C");

            sky = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            if (sky.equals("Clear") )
            {
                this.imgPlusOne.setImageResource(R.drawable.ic_wb_sunny);
            }
            else if (sky.equals("Rain"))
            {
                this.imgPlusOne.setImageResource(R.drawable.ic_icons8_rain_50);
            }
            else
            {
                this.imgPlusOne.setImageResource(R.drawable.ic_cloud);
            }

            //Two plus
            obj = arr.getJSONObject(16);
            temperature = obj.getJSONObject("main").getDouble("temp");
            temp = (int)temperature;

            this.tempPlusTwo.setText(Integer.toString(temp) + " °C");

            sky = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            if (sky.equals("Clear") )
            {
                this.imgPlusTwo.setImageResource(R.drawable.ic_wb_sunny);
            }
            else if (sky.equals("Rain"))
            {
                this.imgPlusTwo.setImageResource(R.drawable.ic_icons8_rain_50);
            }
            else
            {
                this.imgPlusTwo.setImageResource(R.drawable.ic_cloud);
            }

            //Three plus
            obj = arr.getJSONObject(16);
            temperature = obj.getJSONObject("main").getDouble("temp");
            temp = (int)temperature;

            this.tempPlusThree.setText(Integer.toString(temp) + " °C");

            sky = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            if (sky.equals("Clear") )
            {
                this.imgPlusThree.setImageResource(R.drawable.ic_wb_sunny);
            }
            else if (sky.equals("Rain"))
            {
                this.imgPlusThree.setImageResource(R.drawable.ic_icons8_rain_50);
            }
            else
            {
                this.imgPlusThree.setImageResource(R.drawable.ic_cloud);
            }

            // After 3 Hours
            obj = arr.getJSONObject(1);
            temperature = obj.getJSONObject("main").getDouble("temp");
            temp = (int)temperature;

            this.afterHoursTemp.setText(Integer.toString(temp) + " °C");

            sky = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            if (sky.equals("Clear") )
            {
                this.imgAfterHours.setImageResource(R.drawable.ic_wb_sunny);
                this.imgTempList.add(R.drawable.ic_wb_sunny);
            }
            else if (sky.equals("Rain"))
            {
                this.imgAfterHours.setImageResource(R.drawable.ic_icons8_rain_50);
                this.imgTempList.add(R.drawable.ic_icons8_rain_50);
            }
            else
            {
                this.imgAfterHours.setImageResource(R.drawable.ic_cloud);
                this.imgTempList.add(R.drawable.ic_cloud);
            }
            this.hoursTempList.add(temp);

            // After 6 Hours
            obj = arr.getJSONObject(2);
            temperature = obj.getJSONObject("main").getDouble("temp");
            temp = (int)temperature;

            sky = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            if (sky.equals("Clear") )
            {
                this.imgTempList.add(R.drawable.ic_wb_sunny);
            }
            else if (sky.equals("Rain"))
            {
                this.imgTempList.add(R.drawable.ic_icons8_rain_50);
            }
            else
            {
                this.imgTempList.add(R.drawable.ic_cloud);
            }
            this.hoursTempList.add(temp);

            // After 9 Hours
            obj = arr.getJSONObject(3);
            temperature = obj.getJSONObject("main").getDouble("temp");
            temp = (int)temperature;

            sky = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            if (sky.equals("Clear") )
            {
                this.imgTempList.add(R.drawable.ic_wb_sunny);
            }
            else if (sky.equals("Rain"))
            {
                this.imgTempList.add(R.drawable.ic_icons8_rain_50);
            }
            else
            {
                this.imgTempList.add(R.drawable.ic_cloud);
            }
            this.hoursTempList.add(temp);

            // After 12 Hours
            obj = arr.getJSONObject(4);
            temperature = obj.getJSONObject("main").getDouble("temp");
            temp = (int)temperature;

            sky = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            if (sky.equals("Clear") )
            {
                this.imgTempList.add(R.drawable.ic_wb_sunny);
            }
            else if (sky.equals("Rain"))
            {
                this.imgTempList.add(R.drawable.ic_icons8_rain_50);
            }
            else
            {
                this.imgTempList.add(R.drawable.ic_cloud);
            }
            this.hoursTempList.add(temp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void ToggleAfterHours()
    {
        this.hoursState++;
        if (this.hoursState > 4)
        {
            this.hoursState = 1;
        }
        this.afterHours.setText("" + this.hoursState*3 + "h");
        this.afterHoursTemp.setText(Integer.toString(this.hoursTempList.get(this.hoursState-1)) + " °C");
        this.imgAfterHours.setImageResource(this.imgTempList.get(this.hoursState-1));
    }

}
