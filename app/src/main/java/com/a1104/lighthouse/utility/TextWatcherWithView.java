package com.a1104.lighthouse.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.a1104.lighthouse.MainActivity;

public class TextWatcherWithView implements TextWatcher {
    private View view;
    private MainActivity mainActivity;
    private EditText listeningTo;

    public TextWatcherWithView(View view, MainActivity mainActivity, EditText listeningTo)
    {
        this.view = view;
        this.mainActivity = mainActivity;
        this.listeningTo = listeningTo;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        this.mainActivity.SaveItemToDB(this.view);
        listeningTo.removeTextChangedListener(this);
    }

}
