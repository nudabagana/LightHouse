package com.a1104.lighthouse.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.a1104.lighthouse.MainActivity;

public class TextWatcherWithView implements TextWatcher {

    private MainActivity mainActivity;
    private EditText listeningTo;

    public TextWatcherWithView( MainActivity mainActivity, EditText listeningTo)
    {
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
        if (s.toString().length() == 1)
        {
            this.mainActivity.SaveItemToDB(this.listeningTo);
            listeningTo.removeTextChangedListener(this);
        }
    }

    public void setListeningTo(EditText listeningTo) {
        this.listeningTo = listeningTo;
    }
}
