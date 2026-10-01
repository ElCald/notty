package com.example.projet_agenda;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


public class popup_option extends Dialog {

    //attribut
    private String title;
    private Button yesButton, noButton, toutSupprimer;
    private TextView titleView;
    private Switch switch_list, switch_suppAuto;
    private EditText suppAuto;

    //const
    @SuppressLint("MissingInflatedId")
    public popup_option(Activity activity){
        super(activity, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.popup_template_option);

        this.title ="Titre";
        this.yesButton= findViewById(R.id.OUIbouton);
        this.noButton = findViewById(R.id.NONbouton);
        this.titleView= findViewById(R.id.text1);
        this.switch_list= findViewById(R.id.switch_affiche);
        this.toutSupprimer= findViewById(R.id.btn_supprimer);
        this.switch_suppAuto= findViewById(R.id.switch_suppDate);
        this.suppAuto= findViewById(R.id.textView_select_dateSupp);
    }

    //methodes
    public void setTitle(String title){this.title = title;}

    public Button getYesButton(){return yesButton;}

    public Button getNoButton(){return noButton;}

    public Switch getSwitch_list(){return switch_list;}

    public Button getToutSupprimer(){return toutSupprimer;}

    public Switch getSwitch_suppAuto(){return switch_suppAuto;}

    public EditText getTextView_SuppAuto(){return suppAuto;}

    public void build(){
        show();
        titleView.setText(title);
    }


}//fin class
