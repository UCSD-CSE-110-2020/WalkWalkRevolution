package edu.ucsd.cse110.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

class RouteListAdapter extends ArrayAdapter<String> {
    private static String TAG = RouteListAdapter.class.getSimpleName();
    ArrayList<Route> routeData;
    private Team team;
    private Activity context;
    private Integer[] favoriteImg;

    public RouteListAdapter(Activity context, ArrayList<Route> routeData, Team team) {
        super(context, R.layout.route_list);
        this.routeData = routeData;
        this.team = team;
        for(Route routeValues : routeData) {
            add(routeValues.getName());
        }

        this.context=context;
    }

    public RouteListAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
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


        if (team.existsInDatabase()) {
            // Add icon
            ImageView iconImage = (ImageView) rowView.findViewById(R.id.icon_image);
            TextView iconText = (TextView) rowView.findViewById(R.id.icon_text);

            String name = routeData.get(position).getCreator();
            Log.d(TAG, "Drawing icon for " + name);
            team.reload(new Callback.NoArg() {
                @Override
                public void call() {
                    iconImage.getDrawable().setColorFilter(team.getMemberColors().get(name).intValue(), PorterDuff.Mode.MULTIPLY);
                    String initials = "" + Character.toUpperCase(name.charAt(0));
                    for (int i = 1; i < name.length() - 1; i++) {
                        if (name.charAt(i) == ' ') {
                            initials = initials + (Character.toUpperCase(name.charAt(i + 1)));
                        }
                    }
                    iconText.setText(initials);
                }
            });
        }

        // Parse Favorite or not
        //imageView.setImageResource(Integer.parseInt(routeData.get(position)[2]));

        return rowView;
    };


}
