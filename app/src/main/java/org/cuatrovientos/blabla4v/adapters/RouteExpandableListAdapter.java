package org.cuatrovientos.blabla4v.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.fragments.RouteInfo;
import org.cuatrovientos.blabla4v.models.Route;
import org.cuatrovientos.blabla4v.models.User;

import java.util.HashMap;
import java.util.List;

public class RouteExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Route> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Route, List<String>> listDataChild;

    private ExpandableListView expandableListView;


    public RouteExpandableListAdapter(Context context, List<Route> listDataHeader,
                                      HashMap<Route, List<String>> listChildData,ExpandableListView expandableListView) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.expandableListView = expandableListView;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }



    @Override
    public int getChildrenCount(int groupPosition) {
        List<String> childList = this.listDataChild.get(this.listDataHeader.get(groupPosition));
        return (childList != null) ? childList.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        final Route route = (Route) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        ImageView availabilityImageView = (ImageView) convertView
                .findViewById(R.id.availabilityImageView);

        int passengersCount = getChildrenCount(groupPosition);
        int totalSeats = route.getAvailableSeats();
        String day = route.getDate();
        String time = route.getTime();

        lblListHeader.setText(route.getStart() + " - " + route.getEnd());

        if (passengersCount < totalSeats) {
            availabilityImageView.setColorFilter(ContextCompat.getColor(context, R.color.green));
        } else {
            availabilityImageView.setColorFilter(ContextCompat.getColor(context, R.color.red));
        }

        lblListHeader.setOnClickListener(v -> {
            Route selectedRoute = listDataHeader.get(groupPosition);

            // Create a new instance of RouteInfoFragment
            RouteInfo routeInfoFragment = (RouteInfo) ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag("routeInfoFragment");

            FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();

            if (routeInfoFragment == null || !routeInfoFragment.isVisible()) {
                routeInfoFragment = new RouteInfo();
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedRoute", selectedRoute);
                routeInfoFragment.setArguments(bundle);

                transaction.add(R.id.drivers_fragment, routeInfoFragment, "routeInfoFragment");
                transaction.addToBackStack(null);
            } else {
                transaction.remove(routeInfoFragment);
                ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
            }

            transaction.commit();
        });

        // Set an empty OnClickListener on the ImageView
        availabilityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}