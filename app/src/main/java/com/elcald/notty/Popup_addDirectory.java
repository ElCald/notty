package com.elcald.notty;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Popup_addDirectory extends Dialog {

    private Button btn_add, btn_cancel;
    private EditText enterText;

    public Popup_addDirectory(Activity activity) {
        super(activity, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.popup_add_directory);

        btn_add = findViewById(R.id.id_popup_adddirectory_btn_valide);
        btn_cancel = findViewById(R.id.id_popup_adddirectory_btn_cancel);
        enterText = findViewById(R.id.id_popup_adddirectory_edit);

    }

    public Button getBtn_add(){return btn_add;}
    public Button getBtn_cancel(){return btn_cancel;}
    public EditText getEnterText(){return enterText;}

    public void build(){
        show();
    }
}
