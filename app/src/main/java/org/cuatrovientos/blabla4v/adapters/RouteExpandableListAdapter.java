package org.cuatrovientos.blabla4v.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.cuatrovientos.blabla4v.R;
import org.cuatrovientos.blabla4v.models.Route;

import java.util.HashMap;
import java.util.List;

public class RouteExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Route> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Route, List<String>> listDataChild;

    public RouteExpandableListAdapter(Context context, List<Route> listDataHeader,
                                      HashMap<Route, List<String>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
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
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        // Obtén el nombre del pasajero
        final String passengerName = (String) getChild(groupPosition, childPosition);

        // Obtén la ruta
        Route route = (Route) getGroup(groupPosition);
        String driverName = route.getDriver();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        // Configura el TextView para mostrar el nombre del conductor
        TextView txtDriverName = (TextView) convertView.findViewById(R.id.driverNameTextView);
        txtDriverName.setText(driverName);

        // Configura el TextView para mostrar el nombre del pasajero
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(passengerName);

        return convertView;
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
                             View convertView, ViewGroup parent) {
        Route route = (Route) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(route.getStart() + " - " + route.getEnd());



        return convertView;
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