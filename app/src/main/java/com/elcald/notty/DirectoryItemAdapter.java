package com.elcald.notty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DirectoryItemAdapter extends BaseAdapter {

    private Context context;
    private List<String> directoryItemList;
    private LayoutInflater inflater;

    private final Drawable.ConstantState bgNormal;
    private final Drawable.ConstantState bgSelected;


    // constructeur
    @SuppressLint("ResourceAsColor")
    public DirectoryItemAdapter(Context context, List<String> directoryItemList){

        this.context = context;
        this.directoryItemList = directoryItemList;
        this.inflater = LayoutInflater.from(context);


        bgNormal = readBackground(context, R.style.styleDirectoryTile);
        bgSelected = readBackground(context, R.style.styleDirectoryTile_selected);
    }


    private Drawable.ConstantState readBackground(Context context, int styleRes) {
        TypedArray a = context.obtainStyledAttributes(styleRes,
                new int[]{android.R.attr.background});
        try {
            Drawable d = a.getDrawable(0);
            return d != null ? d.getConstantState() : null;
        } finally {
            a.recycle();
        }
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

        if(currentItem.equals("None"))
            itemDirectoryText.setText(R.string.none_dir);
        else if(currentItem.equals("All"))
            itemDirectoryText.setText(R.string.all_dir);
        else
            itemDirectoryText.setText(currentItem);



        // changer coouleur a la selection
        boolean selected = ((AbsListView) parent).isItemChecked(position);
        Drawable.ConstantState state = selected ? bgSelected : bgNormal;
        if (state != null) {
            itemDirectoryText.setBackground(state.newDrawable(context.getResources(), context.getTheme()));
        }

        return convertView;
    }
}
