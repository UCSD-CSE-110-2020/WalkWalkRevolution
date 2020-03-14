package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

class ResponseListAdapter extends ArrayAdapter<String> {
    ArrayList<String> memberData;
    ArrayList<String> responseData;
    private Activity context;

    public ResponseListAdapter(Activity context, ArrayList<String> memberData, ArrayList<String> responseData) {
        super(context, R.layout.route_list);
        this.memberData = memberData;
        for(String member : memberData) {
            add(member);
        }
        this.context = context;
        this.responseData = responseData;
    }

    public ResponseListAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.response_list, null,true);

        TextView nameText = (TextView) rowView.findViewById(R.id.entry_memberName);
        TextView responseText = (TextView) rowView.findViewById(R.id.entry_memberResponse);
        ImageView iconImage = (ImageView) rowView.findViewById(R.id.icon_image);
        TextView iconText = (TextView) rowView.findViewById(R.id.icon_text);

        nameText.setText(memberData.get(position));
        responseText.setText(responseData.get(position));
        if (responseData.get(position).equals("No Response")) {
            responseText.setTextColor(Color.GRAY);
            responseText.setTypeface(nameText.getTypeface(), Typeface.ITALIC);
        }

        int[] rainbow = context.getResources().getIntArray(R.array.rainbow);
        iconImage.getDrawable().setColorFilter(rainbow[position], PorterDuff.Mode.MULTIPLY);
        String name = memberData.get(position);
        String initials = "" + Character.toUpperCase(name.charAt(0));
        for (int i = 1; i < name.length() - 1; i++) {
            if (name.charAt(i) == ' ') {
                initials = initials + (Character.toUpperCase(name.charAt(i + 1)));
            }
        }
        iconText.setText(initials);

        return rowView;
    };


}
