package com.a1104.lighthouse.utility;

import android.view.View;
import android.widget.EditText;

import com.a1104.lighthouse.MainActivity;
import com.a1104.lighthouse.R;

public class OnFocusChangeListenerWithActivity implements View.OnFocusChangeListener {

    private MainActivity mainActivity;

    public OnFocusChangeListenerWithActivity(MainActivity mainActivity)
    {
        super();
        this.mainActivity = mainActivity;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus)
        {
           // mainActivity.UpdateItemInDB((View)v.getParent());

//            String tasktitle = String.valueOf(((EditText)v).getText());
            mainActivity.UpdateItemInDB(v);
        }
    }
}
