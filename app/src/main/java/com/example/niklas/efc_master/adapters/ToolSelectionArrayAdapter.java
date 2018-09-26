package com.example.niklas.efc_master.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.activities.ToolSelectionActivity;

import java.util.List;

public class ToolSelectionArrayAdapter extends ArrayAdapter<ToolSelectionActivity.Tools> {

    private final List<ToolSelectionActivity.Tools> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
        protected ImageView flag;
    }

    public ToolSelectionArrayAdapter(Activity context, List<ToolSelectionActivity.Tools> list) {
        super(context, R.layout.activity_tool_selection, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.activity_tool_selection, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.flag = (ImageView) view.findViewById(R.id.tool_icon);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.flag.setImageDrawable(list.get(position).getIcon());
        return view;
    }
}