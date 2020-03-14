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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

class MemberListAdapter extends ArrayAdapter<String> {
    ArrayList<String> memberData;
    ArrayList<String> invitedData;
    private Activity context;

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

        View rowView = inflater.inflate(R.layout.member_list, null,true);


        TextView nameText = (TextView) rowView.findViewById(R.id.entry_memberName);
        ImageView iconImage = (ImageView) rowView.findViewById(R.id.icon_image);
        TextView iconText = (TextView) rowView.findViewById(R.id.icon_text);

        nameText.setText(memberData.get(position));
        if (invitedData.contains(memberData.get(position))) {
            nameText.setTextColor(Color.GRAY);
            nameText.setTypeface(nameText.getTypeface(), Typeface.ITALIC);
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

        if (name.equals("Not currently in a team!")) {
            rowView.findViewById(R.id.icon_initials).setVisibility(View.GONE);
        }

        return rowView;
    };


}
