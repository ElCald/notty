package com.elcald.notty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DirectoryItemAdapter extends BaseAdapter {

    private Context context;
    private List<String> directoryItemList;
    private LayoutInflater inflater;


    // constructeur
    public DirectoryItemAdapter(Context context, List<String> directoryItemList){
        this.context = context;
        this.directoryItemList = directoryItemList;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return directoryItemList.size();
    }

    @Override
    public String getItem(int position) {
        return directoryItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_item_directory, null);

        String currentItem = getItem(position);

        TextView itemDirectoryText = convertView.findViewById(R.id.id_directory_adapter_text);

        itemDirectoryText.setText(currentItem);

        return convertView;
    }
}
