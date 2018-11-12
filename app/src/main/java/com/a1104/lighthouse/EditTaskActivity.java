package com.a1104.lighthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.a1104.lighthouse.Fragments.CalendarScreenFragment;
import com.a1104.lighthouse.ORMdb.Item;
import com.a1104.lighthouse.ORMdb.ORMdbHelper;
import com.a1104.lighthouse.db.TaskDbHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskActivity";
    private ORMdbHelper dbHelper;
    Dao<Item, Integer> itemDao = null;
    Item item;
    CheckBox done;
    CheckBox repeat;
    CheckBox persist;
    EditText title;
    EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit_screen);
        setupWindowAnimations();
        Intent intent = getIntent();
        int taskId =  intent.getIntExtra(CalendarScreenFragment.TASK_ID, 0);
        dbHelper = new ORMdbHelper(this);
        try {
            itemDao = dbHelper.getDao();
            item = itemDao.queryForId(taskId);
            done = findViewById(R.id.item_done_check_box);
            repeat = findViewById(R.id.item_repeatable_check_box);
            persist = findViewById(R.id.item_persist_check_box);
            title = findViewById(R.id.item_title);
            desc = findViewById(R.id.item_description);
            done.setChecked(item.getDone());
            repeat.setChecked(item.getRepeat());
            persist.setChecked(item.getPersistTillDone());
            title.setText(item.getText());
            desc.setText(item.getDescription());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupWindowAnimations() {
        Explode fade = new Explode();
        fade.setDuration(1000);
        //getWindow().setEnterTransition(fade);
    }

    @Override
    protected void onPause() {
        if (done.isChecked() && !item.getDone())
        {
            item.setDoneDate(new Date());
        }
        item.setDone(done.isChecked());

        item.setRepeat(repeat.isChecked());
        item.setPersistTillDone(persist.isChecked());
        item.setText(title.getText().toString());
        item.setDescription(desc.getText().toString());

        try {
            itemDao.update(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onPause();
    }
}
