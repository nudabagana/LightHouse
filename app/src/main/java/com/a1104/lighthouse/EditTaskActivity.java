package com.a1104.lighthouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.Toast;

import com.a1104.lighthouse.Fragments.CalendarScreenFragment;

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit_screen);
        setupWindowAnimations();
        Intent intent = getIntent();
        int passedAge =  intent.getIntExtra(CalendarScreenFragment.AGE, 0);
    }

    private void setupWindowAnimations() {
        Explode fade = new Explode();
        fade.setDuration(1000);
        //getWindow().setEnterTransition(fade);
    }
}
