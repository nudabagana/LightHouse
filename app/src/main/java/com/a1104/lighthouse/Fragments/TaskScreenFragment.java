package com.a1104.lighthouse.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.a1104.lighthouse.MainActivity;
import com.a1104.lighthouse.ORMdb.Item;
import com.a1104.lighthouse.ORMdb.ORMdbHelper;
import com.a1104.lighthouse.R;
import com.a1104.lighthouse.Weather.WeatherInfo;
import com.a1104.lighthouse.db.TaskContract;
import com.a1104.lighthouse.db.TaskDbHelper;
import com.a1104.lighthouse.utility.TaskArrayAdapter;
import com.j256.ormlite.dao.Dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskScreenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String AGE = "com.a1104.lighthouse.Fragments.Age";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SimpleDateFormat df;
    private static final String dateFormat = "dd-MMM-yyyy";

    private TaskDbHelper mHelper;
    Dao<Item, Integer> itemDao = null;
    private ORMdbHelper dbHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;
    private TaskArrayAdapter taskArrayAdapter;

    private TextView dateSelected;
    private Date currentDate;

    public TaskScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TaskScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskScreenFragment newInstance(/*String param1, String param2*/) {
        TaskScreenFragment fragment = new TaskScreenFragment();
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

       // setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.task_screen_layout2, container, false);

        dateSelected = view.findViewById(R.id.current_date_text_view);

        Date c = Calendar.getInstance().getTime();

        df = new SimpleDateFormat(dateFormat);
        String formattedDate = df.format(c);
        dateSelected.setText(formattedDate);
        this.currentDate = new Date();

        Button tomorrowButton = view.findViewById(R.id.tomorrow_button);
        tomorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(df.parse(dateSelected.getText().toString()));
                    c.add(Calendar.DATE, 1);  // number of days to add
                    dateSelected.setText(df.format(c.getTime()));  // dt is now the new date
                    c.set(Calendar.MINUTE,10);
                    currentDate = c.getTime();
                    try {
                        itemDao.delete(itemDao.queryBuilder().where().eq(Item.FIELD_NAME_TEXT, "").query());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    updateUIORM();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        Button yesterdayButton = view.findViewById(R.id.yesterday_button);
        yesterdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(df.parse(dateSelected.getText().toString()));
                    c.add(Calendar.DATE, -1);  // number of days to add
                    dateSelected.setText(df.format(c.getTime()));  // dt is now the new date
                    c.set(Calendar.MINUTE,10);
                    currentDate = c.getTime();
                    try {
                        itemDao.delete(itemDao.queryBuilder().where().eq(Item.FIELD_NAME_TEXT, "").query());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    updateUIORM();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        mHelper = new TaskDbHelper(getContext());
        mTaskListView = view.findViewById(R.id.list_todo);
        dbHelper = new ORMdbHelper(getContext());
        try {
            itemDao = dbHelper.getDao();
            itemDao.delete(itemDao.queryBuilder().where().eq(Item.FIELD_NAME_TEXT, "").query());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //registerForContextMenu(mTaskListView);

        //CreateItem("randomText");
        //CreateItemORM("Task17");
        //updateUI();
        updateUIORM();

        return view;
    }


    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(getContext(),
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    private void updateUIORM() {
        ArrayList<Item> taskList = new ArrayList<>();
        taskList.addAll(getItemForDate(this.currentDate));

        if (taskArrayAdapter == null) {
            taskArrayAdapter = new TaskArrayAdapter(getContext(),
                    R.layout.item_todo,
                    taskList);
            taskArrayAdapter.add(new Item("",this.currentDate));
            mTaskListView.setAdapter(taskArrayAdapter);
        } else {
            taskArrayAdapter.clear();
            taskArrayAdapter.addAll(taskList);
            taskArrayAdapter.add(new Item("", this.currentDate));
            taskArrayAdapter.notifyDataSetChanged();
        }
       // ((MainActivity)getContext()).RefreshCalendarFragment();
    }

    private List<Item> getItemForDate(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date today = c.getTime();
        c.add(Calendar.DATE, 1);
        Date tomorrow = c.getTime();

        List<Item> todaysItems = new ArrayList<>();
        try {
            todaysItems = itemDao.queryBuilder()
                    .where()
                    .between(Item.FIELD_NAME_DATE,today, tomorrow)
                    .and()
                    .eq(Item.FIELD_NAME_REPEAT,false)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            List<Item> persistItems = itemDao.queryBuilder()
            .where()
            .eq(Item.FIELD_NAME_PERSIST_TIL_DONE,true)
            .and()
            .eq(Item.FIELD_NAME_DONE, false)
            .query();
            for (Item item: persistItems)
            {
                boolean alreadyIn = false;
                for (Item i :todaysItems) {
                    if (i.getId() == item.getId())
                    {
                        alreadyIn = true;
                    }
                }
                if (!alreadyIn)
                {
                    item.setDone(false);
                    todaysItems.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            List<Item> repeatableItems = itemDao.queryBuilder()
                    .where()
                    .eq(Item.FIELD_NAME_REPEAT, true)
                    .query();
            for (Item item: repeatableItems)
            {
                if (item.getDoneDate() == null || item.getDoneDate().before(today))
                {
                    boolean alreadyIn = false;
                    for (Item i :todaysItems) {
                        if (i.getId() == item.getId())
                        {
                            alreadyIn = true;
                        }
                    }
                    if (!alreadyIn)
                    {
                        item.setDone(false);
                        todaysItems.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todaysItems;
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

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        EditText taskTextView = parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    public void deleteTaskORM(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_id);
        int taskId = Integer.parseInt(String.valueOf(taskTextView.getText()));

        try {
            itemDao.deleteById(taskId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateUIORM();
    }

    private void CreateItem(String text)
    {
        String task = String.valueOf(text);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        updateUI();
    }

    private void CreateItemORM(String text)
    {
        Item item = new Item();
        item.setDone(false);
        item.setDate(new Date());
        item.setDescription("Empty");
        item.setPersistTillDone(false);
        item.setProgress(0.0);
        item.setRepeat(false);
        item.setText(text);
        item.setTime("00:00");

        try {
            itemDao.create(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }

       updateUIORM();
    }

    public void doneTaskORM(View view, boolean isDone) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_id);
        int taskId = Integer.parseInt(String.valueOf(taskTextView.getText()));

        try {
            Item item = itemDao.queryForId(taskId);
            if (item!= null)
            {
                item.setDone(isDone);
                if (isDone)
                {
                    item.setDoneDate(this.currentDate);
                    if (item.getPersistTillDone())
                    {
                        item.setDate(this.currentDate);
                    }
                }
                itemDao.update(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //updateUIORM();
    }

    public void saveNewTaskORM(View view) {
        View parent = (View) view.getParent();
        EditText taskTextView = parent.findViewById(R.id.task_title);
        String tasktitle = String.valueOf(taskTextView.getText());


        TextView TextView = parent.findViewById(R.id.task_id);
        int taskId = Integer.parseInt(String.valueOf(TextView.getText()));
        if  (taskId == 0) {


            Item item = new Item(tasktitle, this.currentDate);
            CheckBox doneBox = parent.findViewById(R.id.task_done);
            item.setDone(doneBox.isChecked());
            if (doneBox.isChecked()) {
                item.setDoneDate(this.currentDate);
            }
            try {
                itemDao.create(item);
                taskArrayAdapter.setIdToFocus(item.getId());
                updateUIORM();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateTaskORM(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.task_id);
        int taskId = Integer.parseInt(String.valueOf(taskTextView.getText()));
        EditText editTextView = parent.findViewById(R.id.task_title);
        String tasktitle = String.valueOf(editTextView.getText());

        try {
            Item item = itemDao.queryForId(taskId);
            if (item!= null)
            {
                item.setText(tasktitle);
                itemDao.update(item);
            }
            else
            {
                saveNewTaskORM(view);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setFocusToTextViewWithId(int id)
    {
        ListView v = (ListView) getView().findViewById(R.id.list_todo);
        for (int i = 0; i < v.getChildCount(); i++)
        {
            View view = v.getChildAt(i);
            TextView taskTextView = view.findViewById(R.id.task_id);
            int taskId = Integer.parseInt(String.valueOf(taskTextView.getText()));
            if (taskId == id)
            {
                EditText editText =  view.findViewById(R.id.task_title);
                editText.requestFocus();
                break;
            }
        }
    }

    public void SetWeather(JSONObject object)
    {
        int temp = 17; // << set temp
        try {
            double temperature = object.getJSONObject("main").getDouble("temp");
            temp = (int)temperature;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView textViewTemp = getView().findViewById(R.id.weatherTemperature);

        textViewTemp.setText(Integer.toString(temp) + " °C");
        ImageView imgView = getView().findViewById(R.id.weatherImage);
        try {
            String sky = object.getJSONArray("weather").getJSONObject(0).getString("main");
            if (sky.equals("Clear") )
            {
                imgView.setImageResource(R.drawable.ic_wb_sunny);
            }
            else if (sky.equals("Rain"))
            {
                imgView.setImageResource(R.drawable.ic_icons8_rain_50);
            }
            else
            {
                imgView.setImageResource(R.drawable.ic_cloud);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void SetLocationInfo(String city, String country)
    {
        WeatherInfo.getInstance(getContext()).makeRequest(city,this);
        TextView textViewLoc = getView().findViewById(R.id.locationTextView);
        textViewLoc.setText(city + ", " + country);
    }

    public void SetCurrentDate(int day, int month, int year)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        dateSelected.setText(df.format(c.getTime()));  // dt is now the new date
        c.set(Calendar.MINUTE,10);
        currentDate = c.getTime();
        updateUIORM();
    }

    @Override
    public void onResume() {
        updateUIORM();
        super.onResume();
    }
}
