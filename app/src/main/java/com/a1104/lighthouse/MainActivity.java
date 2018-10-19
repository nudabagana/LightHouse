package com.a1104.lighthouse;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.a1104.lighthouse.Fragments.CalendarScreenFragment;
import com.a1104.lighthouse.Fragments.OnFragmentInteractionListener;
import com.a1104.lighthouse.Fragments.TaskScreenFragment;
import com.a1104.lighthouse.Fragments.WeatherScreenFragment;
import com.a1104.lighthouse.db.TaskContract;
import com.a1104.lighthouse.db.TaskDbHelper;
import com.a1104.lighthouse.scroll.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.a1104.lighthouse.Fragments.CalendarScreenFragment.AGE;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, PopupMenu.OnMenuItemClickListener,
        CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MainActivity";

    private MyAdapter myAdapter;
    private ViewPager viewPager;

    private TaskDbHelper mHelper;
//    private ListView mTaskListView;
//    private ArrayAdapter<String> mAdapter;
    private View currentMenuView;

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
    }

    private void setupWindowAnimations() {
        Explode explode = new Explode();
        //slide.setDuration(1000);
        getWindow().setExitTransition(explode);
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
                Intent myIntent = new Intent(this, EditTaskActivity.class);
                myIntent.putExtra(AGE,17);

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
}
