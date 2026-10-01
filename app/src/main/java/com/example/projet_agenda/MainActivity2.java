package com.example.projet_agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_agenda.bdd.AccesLocal;

import java.util.Calendar;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {

    private TextView main_textView_TBar_titre, main_textView_selectDate;
    private Button main_btn_valider, main_btn_selectImage;
    private EditText main_edit_titre, main_edit_contenu, main_edit_tag, main_edit_lien;
    private Context contexte = this;
    private AccesLocal accesLocal;
    private String strTitre, strContenu, strTag, strLien, strDate, titre, contenu, tag, date= " ", image;
    private ImageView fleche_retour, image_modifier;


    //Modifer
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        accesLocal = new AccesLocal(contexte);

        Date dateJour = new Date();
        String dateJ = DateFormat.format("dd/MM/yyyy", dateJour).toString();


        String valEtat = String.valueOf(Serializer.deSerialize("EtatSwitch", contexte));
        int etat = Integer.parseInt(valEtat);

        String valSer = String.valueOf(Serializer.deSerialize("positionListe", contexte));
        int pos = Integer.parseInt(valSer);

        if(etat==0) {
            //Recup l'event depuis la liste cree a une certaine pos
            titre = accesLocal.recupAllDate(dateJ).get(pos).getTitre();
            contenu = accesLocal.recupAllDate(dateJ).get(pos).getContenu();
            date = accesLocal.recupAllDate(dateJ).get(pos).getDate();
            tag = accesLocal.recupAllDate(dateJ).get(pos).getTag();
            image = accesLocal.recupAllDate(dateJ).get(pos).getImage();
        }
        else{
            titre = accesLocal.recupAll().get(pos).getTitre();
            contenu = accesLocal.recupAll().get(pos).getContenu();
            date = accesLocal.recupAll().get(pos).getDate();
            tag = accesLocal.recupAll().get(pos).getTag();
            image = accesLocal.recupAll().get(pos).getImage();
        }


        //Barre de menu -------------------------------------------------------------

        this.fleche_retour = (ImageView) findViewById(R.id.fleche_retour);
        fleche_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activiteHome = new Intent(getApplicationContext(), MainActivity4.class);
                startActivity(activiteHome);
                finish();
            }
        });//fin fleche_retour

        this.main_textView_TBar_titre = (TextView) findViewById(R.id.textView_TBar_titre);
        main_textView_TBar_titre.setText("Modifier");

        this.image_modifier  = (ImageView) findViewById(R.id.image_edit);
        image_modifier.setVisibility(View.INVISIBLE);


        //layout ------------------------------------------------------------------------------------

        main_edit_titre = (EditText) findViewById(R.id.edit_titre);
        main_edit_contenu = (EditText) findViewById(R.id.edit_contenu);
        main_edit_tag = (EditText) findViewById(R.id.edit_tag);
        //main_edit_lien = (EditText) findViewById(R.id.edit_lien);
        main_textView_selectDate = (TextView) findViewById(R.id.textView_select_date);


        if(titre == contenu){

        }
        else {
            main_edit_titre.setText(titre);
        }
        if(contenu == " "){
            strContenu = " ";
        }
        else {
            main_edit_contenu.setText(contenu);
        }
        if(tag == " "){
            strTag = " ";
        }
        else {
            main_edit_tag.setText(tag);
        }
        if(date == " "){}
        else {
            main_textView_selectDate.setText(date);
            strDate = date;
        }



        final Calendar cal = Calendar.getInstance();
        final int annee = cal.get(Calendar.YEAR);
        final int mois = cal.get(Calendar.MONTH);
        final int jour = cal.get(Calendar.DAY_OF_MONTH);

        main_textView_selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
                        mois = mois +1;
                        if(jour >= 1 && jour <= 9)
                            strDate = "0"+jour+"/"+mois+"/"+annee;
                        else
                            strDate = jour+"/"+mois+"/"+annee;

                        main_textView_selectDate.setText(strDate);
                    }
                }, annee, mois, jour);
                dialog.show();
            }
        });//fin selectDate



        this.main_btn_valider = (Button) findViewById(R.id.btn_ajouter);
        main_btn_valider.setText("Valider");
        main_btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                main_edit_titre = (EditText) findViewById(R.id.edit_titre);
                main_edit_contenu = (EditText) findViewById(R.id.edit_contenu);
                main_edit_tag = (EditText) findViewById(R.id.edit_tag);
                //main_edit_lien = (EditText) findViewById(R.id.edit_lien);



                strTitre = main_edit_titre.getText().toString();
                strContenu = main_edit_contenu.getText().toString();
                strTag = main_edit_tag.getText().toString();
                strLien = System.currentTimeMillis()+".jpg";


                if(strTitre == " " || strContenu == " " || strTag == " " || strDate == " "){
                    Toast.makeText(getApplicationContext(), "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                }
                else {

                    //Evenement event = new Evenement("test1","contenu1","tag1",new Date(), "lien2");
                    Evenement event = new Evenement(strTitre, strContenu, strTag, strDate, strLien);
                    accesLocal.modifier(event, titre);


                    Intent activiteHome = new Intent(getApplicationContext(), MainActivity4.class);
                    startActivity(activiteHome);
                    finish();

                    Toast.makeText(getApplicationContext(), "Modifié", Toast.LENGTH_SHORT).show();
                }
            }
        });//fin ajouter


    }//fin onCreate
}//fin class