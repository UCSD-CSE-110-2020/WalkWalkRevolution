package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

class CustomListAdapter extends ArrayAdapter<String> {
    ArrayList<String[]> routeData;
    private Activity context;
    private Integer[] favoriteImg;

    public CustomListAdapter(Activity context, ArrayList<String[]> routeData) {
        super(context, R.layout.route_list);
        this.routeData = routeData;
        for(String[] routeValues : routeData) {
            add(routeValues[0]);
        }

        this.context=context;
    }

    public CustomListAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();

        View rowView=inflater.inflate(R.layout.route_list, null,true);

        TextView nameText = (TextView) rowView.findViewById(R.id.entry_routeName);
        TextView stepsText = (TextView) rowView.findViewById(R.id.entry_routeDate);
        TextView milesText = (TextView) rowView.findViewById(R.id.entry_routeStepsAndMiles);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_favoriteStar);

        nameText.setText(routeData.get(position)[0]);
        stepsText.setText(routeData.get(position)[2]);
        milesText.setText(routeData.get(position)[3]);
        // Parse Favorite or not
        //imageView.setImageResource(Integer.parseInt(routeData.get(position)[2]));

        return rowView;
    };


}
