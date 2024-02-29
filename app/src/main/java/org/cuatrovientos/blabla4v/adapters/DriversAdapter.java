package org.cuatrovientos.blabla4v.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.cuatrovientos.blabla4v.R;

import java.util.HashMap;
import java.util.List;

public class DriversAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> sites;
    private HashMap<String, List<String>> drivers;

    public DriversAdapter(Context context, List<String> sites, HashMap<String, List<String>> drivers) {
        this.context = context;
        this.sites = sites;
        this.drivers = drivers;
    }

    @Override
    public int getGroupCount() {
        return this.sites.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (this.drivers.get(this.sites.get(groupPosition)) != null) {
            return this.drivers.get(this.sites.get(groupPosition)).size();
        } else {
            return 0; // o devuelve un valor predeterminado en caso de que la lista sea null
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.sites.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.drivers.get(this.sites.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String siteText = (String) getGroup(groupPosition);

        if (convertView == null){
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.sites_list, null);
        }

        TextView txtSite = convertView.findViewById(R.id.txtSite);
        txtSite.setText(siteText);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String driverText = (String) getChild(groupPosition, childPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drivers_sublist, null);
        }

        TextView txtDriver = convertView.findViewById(R.id.txtDriver);
        txtDriver.setText(driverText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
