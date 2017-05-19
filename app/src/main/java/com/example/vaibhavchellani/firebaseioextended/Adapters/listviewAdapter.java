package com.example.vaibhavchellani.firebaseioextended.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vaibhavchellani.firebaseioextended.Models.Message;
import com.example.vaibhavchellani.firebaseioextended.R;

import java.util.List;

/**
 * Created by vaibhavchellani on 5/19/17.
 */

public class listViewAdapter extends ArrayAdapter<Message> {
    public listViewAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
    }



    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.row_layout, parent, false);
        }
        TextView messageTextView = (TextView) convertView.findViewById(R.id.userMessageTextView);
        TextView usernameTextView=(TextView) convertView.findViewById(R.id.usernameTextView);
        Message message = getItem(position);
        messageTextView.setText(message.getUserMessage());
        usernameTextView.setText(message.getUserName());
        return convertView;
    }
}
