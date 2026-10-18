package com.elcald.notty;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class Popup_selectDirectory extends Dialog {

    private ListView listView_directories;
    private ConstraintLayout layout;
    private ImageView btn_close;


    public Popup_selectDirectory(Activity activity, List<Note> selectedNotes) {
        super(activity, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.popup_select_directory);

        btn_close = findViewById(R.id.id_popup_select_directory_directories_close);

        DirectorySelectItemAdapter directoryAdapter = new DirectorySelectItemAdapter(activity, selectedNotes, this);
        listView_directories = findViewById(R.id.id_popup_select_directory_directories_listview);
        listView_directories.setAdapter(directoryAdapter);

        layout = findViewById(R.id.id_popup_select_directory_directories_layout);

        // Permet de récupérer la largeur de l'écran
        WindowManager manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        // get width and height
        int width = point.x;

        layout.setMinWidth(width);
    }

    public ImageView getBtn_close(){ return btn_close; }


    public void build(){
        show();
    }
}
