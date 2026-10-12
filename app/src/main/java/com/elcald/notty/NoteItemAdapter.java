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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class NoteItemAdapter extends BaseAdapter {

    private Context context;
    private List<Note> noteItemList;
    private LayoutInflater inflater;


    // constructeur
    public NoteItemAdapter(Context context, List<Note> noteItemList){
        this.context = context;
        this.noteItemList = noteItemList;
        this.inflater = LayoutInflater.from(context);
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
        String itemDirectory = currentItem.getDirectory();
        boolean itemLocker = currentItem.getIsLocked();
        boolean itemFav = currentItem.getFavorite();


        // Resize la tile en fonction de la largeur de l'écran
        // à voir si ça marche vraiment
        WindowManager manager =  (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        // get width and height
        int width = point.x;

        TextView itemTitleView = convertView.findViewById(R.id.id_adapter_text_note_title);
        ImageView itemTileFav = convertView.findViewById(R.id.id_adapter_text_note_fav);
        ImageView itemTileLocker = convertView.findViewById(R.id.id_adapter_text_note_tile_locker);
        TextView itemTileDirectory = convertView.findViewById(R.id.id_adapter_text_note_directory);


        // vérifier si le titre est vide, auquel cas on mettra le corps pour le titre
        if(itemTitle.trim().isEmpty())
            itemTitleView.setText(itemBody);
        else
            itemTitleView.setText(itemTitle);

        itemTitleView.setWidth( width/2 );
        itemTitleView.setHeight( width/2 );


        if(itemLocker){
            itemTileLocker.setVisibility(View.VISIBLE);
        }

        if(itemFav){
            itemTileFav.setVisibility(View.VISIBLE);
        }

        if(!itemDirectory.trim().isEmpty()){
            itemTileDirectory.setVisibility(View.VISIBLE);
            itemTileDirectory.setText(itemDirectory);
        }


        // click sur une note pour l'ouvrir

        itemTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent readNoteActivity = new Intent(context.getApplicationContext(), ReadNoteActivity.class);
                context.startActivity(readNoteActivity.putExtra("id_note", currentItem.getId()));
                ((MainActivity)context).finish();
            }
        });

        itemTitleView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = new Toast(context.getApplicationContext());
                toast.setText("LongClick");
                toast.show();

                return true;
            }
        });



        return convertView;
    }

}
