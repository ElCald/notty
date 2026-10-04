package com.elcald.notty;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class NoteItemAdapter extends BaseAdapter {

    private Context context;
    private List<Note> noteItemList;
    private LayoutInflater inflater;
   /* private int tile_width;
    private int tile_height;*/

    // constructeur
    public NoteItemAdapter(Context context, List<Note> noteItemList/*, int tile_width, int tile_height*/){
        this.context = context;
        this.noteItemList = noteItemList;
        this.inflater = LayoutInflater.from(context);
        /*this.tile_width = tile_width;
        this.tile_height = tile_height;*/
    }

    // methodes
    @Override
    public int getCount() {
        return noteItemList.size();
    }

    @Override
    public Note getItem(int position) {
        return noteItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_item_note, null);

        // info sur l'item
        Note currentItem = getItem(position);

        String itemTitle = currentItem.getTitle();
        String itemBody = currentItem.getContent();

        // vérifier si le titre est vide, auquel cas on mettra le corps pour le titre

        // à voir si ça marche vraiment
        WindowManager manager =  (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        // get width and height
        int width = point.x;

        TextView itemTitleView = convertView.findViewById(R.id.id_adapter_text_note_title);

        if(itemTitle.trim().isEmpty())
            itemTitleView.setText(itemBody);
        else
            itemTitleView.setText(itemTitle);

        itemTitleView.setWidth( (width/2)-50 );
        itemTitleView.setHeight( (width/2)-50 );


        // click sur une note pour l'ouvrir

        itemTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent readNoteActivity = new Intent(context.getApplicationContext(), ReadNoteActivity.class);
                context.startActivity(readNoteActivity.putExtra("id_note", currentItem.getId()));
                ((MainActivity)context).finish();
            }
        });



        return convertView;
    }

}
