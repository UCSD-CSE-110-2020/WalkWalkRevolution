package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;

class MemberListAdapter extends ArrayAdapter<String> {
    ArrayList<String> memberData;
    ArrayList<String> invitedData;
    private Activity context;
    private Integer[] userIcon;

    public MemberListAdapter(Activity context, ArrayList<String> memberData, ArrayList<String> invitedData) {
        super(context, R.layout.route_list);
        this.memberData = memberData;
        for(String member : memberData) {
            add(member);
        }
        this.invitedData = invitedData;
        this.context = context;
    }

    public MemberListAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView=inflater.inflate(R.layout.member_list, null,true);


        TextView nameText = (TextView) rowView.findViewById(R.id.entry_memberName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_memberInitials);

        nameText.setText(memberData.get(position));
        if (invitedData.contains(memberData.get(position))) {
            nameText.setTextColor(Color.GRAY);
            nameText.setTypeface(nameText.getTypeface(), Typeface.ITALIC);
        }

        // Parse Favorite or not
        // imageView.setImageResource(Integer.parseInt(routeData.get(position)[2]));

        return rowView;
    };


}
