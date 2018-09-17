package com.example.niklas.efc_master;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ToolSelectionActivity extends ListActivity
{
    public static String RESULT_TOOL= "tool";
    public String[] ToolNames, ToolCodes;
    private TypedArray ToolIcons;
    private List<Tools> toolList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateToolList();
        ArrayAdapter<Tools> adapter = new ToolSelectionArrayAdapter(this, toolList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tools t = toolList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_TOOL, t.getCode());
                setResult(RESULT_OK, returnIntent);
                ToolIcons.recycle(); //recycle images
                finish();
            }
        });
    }

    public void onResume()
    {
        getWindow().setLayout(620, 980);
        super.onResume();
    }

    private void populateToolList() {
        toolList = new ArrayList<Tools>();
        ToolNames = getResources().getStringArray(R.array.tool_names);
        ToolCodes = getResources().getStringArray(R.array.tool_codes);
        ToolIcons = getResources().obtainTypedArray(R.array.tool_icons);
        for(int i = 0; i < ToolCodes.length; i++){
            toolList.add(new Tools(ToolNames[i], ToolCodes[i], ToolIcons.getDrawable(i)));
        }
    }

    public class Tools {
        private String name;
        private String code;
        private Drawable icon;

        public Tools(String name, String code, Drawable icon){
            this.name = name;
            this.code = code;
            this.icon = icon;
        }
        public String getName() {
            return name;
        }
        public Drawable getIcon() {
            return icon;
        }
        public String getCode() {
            return code;
        }
    }


}
