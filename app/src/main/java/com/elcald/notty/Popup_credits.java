package com.elcald.notty;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Popup_credits extends Dialog {

    private ImageView btn_close;
    private ConstraintLayout layout;

    public Popup_credits(Activity activity) {
        super(activity, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.popup_credit);

        btn_close = findViewById(R.id.id_popup_credit_btn_cancel);

        layout = findViewById(R.id.id_popup_credit_layout);

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
