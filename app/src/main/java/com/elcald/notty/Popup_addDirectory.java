package com.elcald.notty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Popup_addDirectory extends Dialog {

    private ImageView btn_cancel;
    private TextView btn_add;
    private EditText enterText;
    private ConstraintLayout layout;



    @SuppressLint("MissingInflatedId")
    public Popup_addDirectory(Activity activity) {
        super(activity, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.popup_add_directory);

        btn_add = findViewById(R.id.id_popup_adddirectory_btn_valide);
        btn_cancel = findViewById(R.id.id_popup_adddirectory_btn_cancel);
        enterText = findViewById(R.id.id_popup_adddirectory_edit);
        layout = findViewById(R.id.id_popup_adddirectory_home);

        // Permet de récupérer la largeur de l'écran
        WindowManager manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        // get width and height
        int width = point.x;

        layout.setMinWidth(width);

    }

    public TextView getBtn_add(){return btn_add;}
    public ImageView getBtn_cancel(){return btn_cancel;}
    public EditText getEnterText(){return enterText;}

    public void build(){
        show();
    }
}
