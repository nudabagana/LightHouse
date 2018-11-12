package com.a1104.lighthouse;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.a1104.lighthouse.Fragments.CalendarScreenFragment;
import com.a1104.lighthouse.Fragments.OnFragmentInteractionListener;
import com.a1104.lighthouse.Fragments.TaskScreenFragment;
import com.a1104.lighthouse.Fragments.WeatherScreenFragment;
import com.a1104.lighthouse.db.TaskDbHelper;
import com.a1104.lighthouse.scroll.MyAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.a1104.lighthouse.Fragments.CalendarScreenFragment.TASK_ID;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, PopupMenu.OnMenuItemClickListener,
        CompoundButton.OnCheckedChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MyAdapter myAdapter;
    private ViewPager viewPager;

    private TaskDbHelper mHelper;
    //    private ListView mTaskListView;
//    private ArrayAdapter<String> mAdapter;
    private View currentMenuView;
    private GoogleApiClient googleApiClient;

    private String cityName = "";
    private String countryName = "";

    private WeatherScreenFragment weatherFragment;
    private CalendarScreenFragment calendarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupWindowAnimations();

//        mHelper = new TaskDbHelper(this);
//        mTaskListView = findViewById(R.id.list_todo);
//
//        SQLiteDatabase db = mHelper.getReadableDatabase();
//        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
//                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
//                null, null, null, null, null);
//        while(cursor.moveToNext()) {
//            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
//            Log.d(TAG, "Task: " + cursor.getString(idx));
//        }
//        cursor.close();
//        db.close();
//        updateUI();

        myAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.pagerContainer);
        viewPager.setAdapter(myAdapter);
        viewPager.setCurrentItem(1);


        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    private void setupWindowAnimations() {
        Explode explode = new Explode();
        //slide.setDuration(1000);
        getWindow().setExitTransition(explode);
    }

    public void SetWeatherFragment(WeatherScreenFragment fragment)
    {
        this.weatherFragment = fragment;
    }

    public void SetCalendarFragment(CalendarScreenFragment fragment)
    {
        this.calendarFragment = fragment;
    }

    public void RefreshCalendarFragment()
    {
        if (this.calendarFragment != null)
        {
            this.calendarFragment.updateUIORM();
        }
    }


    @Override
    protected void onStart() {
// connect googleapiclient
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
// disconnect googleapiclient
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation != null) {
            // here we go you can see current lat long.
            Log.e(TAG, "onConnected: " + String.valueOf(mLastLocation.getLatitude()) + ":" + String.valueOf(mLastLocation.getLongitude()));
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                if (addresses != null) {
                    if (addresses.size() > 0) {
                        this.cityName = addresses.get(0).getLocality();
                        this.countryName = addresses.get(0).getCountryName();
                        ((TaskScreenFragment)getVisibleFragment()).SetLocationInfo(this.cityName, this.countryName);
                        this.weatherFragment.SetLocationInfo(this.cityName, this.countryName);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetCityName()
    {
        return this.cityName;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_add_task:
//                final EditText taskEditText = new EditText(this);
//                AlertDialog dialog = new AlertDialog.Builder(this)
//                        .setTitle("Add a new task")
//                        .setMessage("What do you want to do next?")
//                        .setView(taskEditText)
//                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String task = String.valueOf(taskEditText.getText());
//                                SQLiteDatabase db = mHelper.getWritableDatabase();
//                                ContentValues values = new ContentValues();
//                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
//                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
//                                        null,
//                                        values,
//                                        SQLiteDatabase.CONFLICT_REPLACE);
//                                db.close();
//                                updateUI();
//                            }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .create();
//                dialog.show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_item:

                View parent = (View) this.currentMenuView.getParent();
                TextView taskTextView = parent.findViewById(R.id.task_id);
                int taskId = Integer.parseInt(String.valueOf(taskTextView.getText()));

                Intent myIntent = new Intent(this, EditTaskActivity.class);
                myIntent.putExtra(TASK_ID,taskId);

                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();

                startActivity(myIntent,bundle);
                return true;
            case R.id.action_delete_item:
                ((TaskScreenFragment)getVisibleFragment()).deleteTaskORM(this.currentMenuView);
                return true;
            default:
                return false;
        }
    }


//    private void updateUI() {
//        ArrayList<String> taskList = new ArrayList<>();
//        SQLiteDatabase db = mHelper.getReadableDatabase();
//        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
//                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
//                null, null, null, null, null);
//        while (cursor.moveToNext()) {
//            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
//            taskList.add(cursor.getString(idx));
//        }
//
////        if (mAdapter == null) {
////            mAdapter = new ArrayAdapter<>(this,
////                    R.layout.item_todo,
////                    R.id.task_title,
////                    taskList);
////            mTaskListView.setAdapter(mAdapter);
////        } else {
////            mAdapter.clear();
////            mAdapter.addAll(taskList);
////            mAdapter.notifyDataSetChanged();
////        }
//
//        cursor.close();
//        db.close();
//    }

//    public void deleteTask() {
//        //View parent = (View) view.getParent();
//        View parent = (View) this.currentMenuView.getParent();
//        EditText taskTextView = parent.findViewById(R.id.task_title);
//        String task = String.valueOf(taskTextView.getText());
//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        db.delete(TaskContract.TaskEntry.TABLE,
//                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
//                new String[]{task});
//        db.close();
//        updateUI();
//    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.task_dot_menu, popup.getMenu());
        popup.show();
        this.currentMenuView = v;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        View parent = (View) buttonView.getParent();
        ((TaskScreenFragment)getVisibleFragment()).doneTaskORM(parent, isChecked);
    }

    public void SaveItemToDB(View view)
    {
        ((TaskScreenFragment)getVisibleFragment()).saveNewTaskORM(view);
    }

    public void UpdateItemInDB(View view)
    {
        ((TaskScreenFragment)getVisibleFragment()).updateTaskORM(view);
    }


    public void GoToChosenDate(int day, int month, int year)
    {
        viewPager.setCurrentItem(1);
        ((TaskScreenFragment)getVisibleFragment()).SetCurrentDate(day, month, year);
    }

}
