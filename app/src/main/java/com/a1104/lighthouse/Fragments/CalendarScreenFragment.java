package com.a1104.lighthouse.Fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.a1104.lighthouse.EditTaskActivity;
import com.a1104.lighthouse.MainActivity;
import com.a1104.lighthouse.ORMdb.Item;
import com.a1104.lighthouse.ORMdb.ORMdbHelper;
import com.a1104.lighthouse.R;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarScreenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TASK_ID = "com.a1104.lighthouse.Fragments.TaskId";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Dao<Item, Integer> itemDao = null;
    private ORMdbHelper dbHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;

    private OnFragmentInteractionListener mListener;


    public CalendarScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarScreenFragment newInstance(/*String param1, String param2*/) {
        CalendarScreenFragment fragment = new CalendarScreenFragment();
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
        View view = inflater.inflate(R.layout.calendar_screen_layout, container, false);
        CalendarView calendarView=(CalendarView) view.findViewById(R.id.calendar_screen_calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
//                Toast.makeText(getActivity(), ""+dayOfMonth, Toast.LENGTH_SHORT).show();// TODO Auto-generated method stub
//                Intent myIntent = new Intent(getActivity(), EditTaskActivity.class);
//                myIntent.putExtra(AGE,17);
//
//                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
//
//                startActivity(myIntent,bundle);

                ((MainActivity)getActivity()).GoToChosenDate(dayOfMonth, month, year);

                //getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mTaskListView = view.findViewById(R.id.list_all_items);

        dbHelper = new ORMdbHelper(getContext());
        try {
            itemDao = dbHelper.getDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateUIORM();
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
            ((MainActivity)context).SetCalendarFragment(this);
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

    public void updateUIORM() {
        if (itemDao == null)
        {
            return;
        }
        ArrayList<String> taskList = new ArrayList<>();

        List<Item> itemList = getItems();
            for (Item item: itemList)
            {
                taskList.add(item.getText());
            }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(getContext(),
                    R.layout.item_todo_calendar,
                    R.id.task_title_calendar,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<Item> getItems()
    {
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        today = c.getTime();

        List<Item> todaysItems = new ArrayList<>();
        try {
            todaysItems = itemDao.queryBuilder().orderBy(Item.FIELD_NAME_DATE, true)
                    .where().ge(Item.FIELD_NAME_DATE,today)
                    .and()
                    .eq(Item.FIELD_NAME_DONE,false).query();

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

}
