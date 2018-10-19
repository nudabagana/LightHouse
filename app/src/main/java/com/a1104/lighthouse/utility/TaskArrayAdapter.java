package com.a1104.lighthouse.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.a1104.lighthouse.MainActivity;
import com.a1104.lighthouse.ORMdb.Item;
import com.a1104.lighthouse.R;

import java.util.ArrayList;
import java.util.List;

public class TaskArrayAdapter extends ArrayAdapter<Item> {

    List<Item> itemList = new ArrayList<>();

    private int idToFocus = 0;

    public TaskArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    public TaskArrayAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        itemList = items;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if ( v == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_todo, null);
        }
        TextView IdView = v.findViewById(R.id.task_id);
        EditText textView = v.findViewById(R.id.task_title);
        CheckBox doneBox = v.findViewById(R.id.task_done);
        doneBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this.getContext());

        Item item = itemList.get(position);
        IdView.setText(String.valueOf(item.getId()));
        textView.setText(item.getText());
        doneBox.setChecked(item.getDone());

        if( idToFocus > 0 && item.getId() == idToFocus)
        {
            idToFocus = 0;
            textView.requestFocus();
            textView.setSelection(textView.getText().length());
        }

        if (item.getId() < 1)
        {
            textView.addTextChangedListener(new TextWatcherWithView((View)doneBox.getParent(),(MainActivity)this.getContext(), textView));
        }
        return v;
    }

    public void setIdToFocus(int id)
    {
        this.idToFocus = id;
    }

}
