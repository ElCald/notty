package com.elcald.notty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DirectorySelectItemAdapter extends BaseAdapter {

    private Context context;
    private Bibliotheque biblio;
    private LayoutInflater inflater;
    private List<Note> selectedNotes;
    private Popup_selectDirectory popup;


    // constructeur
    public DirectorySelectItemAdapter(Context context, List<Note> selectedNotes, Popup_selectDirectory popup){

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.selectedNotes = new ArrayList<>(selectedNotes);
        this.popup = popup;

        try {
            biblio = new Bibliotheque(context);
        } catch (JSONException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public int getCount() {
        return biblio.getDirectories().size()-1;
    }

    @Override
    public String getItem(int position) {
        return biblio.getDirectories().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_select_directory, null);

        String currentItem = getItem(position);

        TextView itemDirectoryText = convertView.findViewById(R.id.id_adapter_select_directory_title);

        if(!currentItem.equals("All")) {
            if (currentItem.equals("None"))
                itemDirectoryText.setText(R.string.none_dir);
            else
                itemDirectoryText.setText(currentItem);

            itemDirectoryText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(Note n : selectedNotes){
                        biblio.getNote(n.getId()).setDirectory(currentItem);
                    }
                    biblio.save_notty_file();
                    popup.dismiss();
                }
            });
        }
        else{
            itemDirectoryText.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }
}
