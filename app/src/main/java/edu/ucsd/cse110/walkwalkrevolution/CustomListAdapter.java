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
import java.util.Calendar;

class CustomListAdapter extends ArrayAdapter<String> {
    ArrayList<Route> routeData;
    private Activity context;
    private Integer[] favoriteImg;

    public CustomListAdapter(Activity context, ArrayList<Route> routeData) {
        super(context, R.layout.route_list);
        this.routeData = routeData;
        for(Route routeValues : routeData) {
            add(routeValues.getName());
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
        TextView startingPointText = (TextView) rowView.findViewById(R.id.entry_startingPoint);
        TextView lastRunText = (TextView) rowView.findViewById(R.id.entry_routeDate);
        TextView stepsAndMilesText = (TextView) rowView.findViewById(R.id.entry_routeStepsAndMiles);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_favoriteStar);



        nameText.setText(routeData.get(position).getName());
        startingPointText.setText("starting point: " + routeData.get(position).getStartingPoint());
        String stepsAndMilesString = "Steps: " + Integer.toString(routeData.get(position).getSteps())
                + "  Distance: " + Float.toString(routeData.get(position).getDistance());
       stepsAndMilesText.setText(stepsAndMilesString);

        Calendar lastRun = routeData.get(position).getLastRun();

        if (lastRun == null) {
            lastRunText.setText(R.string.never_run);
        } else {
            lastRunText.setText(lastRun.get(Calendar.MONTH) + "/" +
                    lastRun.get(Calendar.DAY_OF_MONTH) + "/" +
                    lastRun.get(Calendar.YEAR));
        }

        // Parse Favorite or not
        //imageView.setImageResource(Integer.parseInt(routeData.get(position)[2]));

        return rowView;
    };


}
