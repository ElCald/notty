package com.elcald.notty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SettingsItemAdapter extends BaseAdapter {

    private List<String> settingsList;
    private Context context;
    private LayoutInflater inflater;


    // constructeur
    public SettingsItemAdapter(Context context, List<String> settingsList){
        this.context = context;
        this.settingsList = settingsList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return settingsList.size();
    }

    @Override
    public String getItem(int position) {
        return settingsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_item_settings, null);

        String currentItem = getItem(position);

        TextView itemSettingsText = convertView.findViewById(R.id.id_settings_adapter_title);

        itemSettingsText.setText(currentItem);

        // click sur une note pour l'ouvrir

        itemSettingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent readNoteActivity = new Intent(context.getApplicationContext(), ReadNoteActivity.class);
//                context.startActivity(readNoteActivity.putExtra("id_note", currentItem.getId()));
//                ((MainActivity)context).finish();
            }
        });


        return convertView;
    }
}
