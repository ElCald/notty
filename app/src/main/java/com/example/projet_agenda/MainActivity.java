package com.example.projet_agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_agenda.bdd.AccesLocal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//View accueil
public class MainActivity extends AppCompatActivity implements Serializable {

    private TextView main_textView_date;
    private EditText main_textView_selectDateSupp;
    private ImageView main_ajouter, main_option, main_event_supp;
    private Context contexte = this;
    private AccesLocal accesLocal;
    private Button main_btn_rc, main_btn_supp;
    private Switch main_switch_affiche, main_switch_suppAuto;
    private List<Evenement> liste;
    private EvenementAdapter eventAdapt;
    private MainActivity activite;
    private String dateSelected =" ";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        this.activite = this;

        accesLocal = new AccesLocal(contexte);

        ListView eventListView = (ListView) findViewById(R.id.list_item);

        //Affichage des evenements
        liste = new ArrayList<>();
        liste.clear();



        //Barre de menu -------------------------------------------------------------


        //Date du jour accueil
        Date date = new Date();
        String dateJ = DateFormat.format("dd/MM/yyyy", date).toString();
        this.main_textView_date = (TextView) findViewById(R.id.textView_TBar_date);
        main_textView_date.setText(dateJ);


        //Suppression automatique
        String dateSupp = String.valueOf(Serializer.deSerialize("DateSupp", contexte));
        int nDateSupp = Integer.parseInt(dateSupp);

        String suppAuto = String.valueOf(Serializer.deSerialize("EtatSwitchSuppAuto", contexte));
        int etatSuppAuto = Integer.parseInt(suppAuto);

        if(etatSuppAuto == 1){
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            c.add(Calendar.DATE, -nDateSupp);
            Date derniereDate = c.getTime();
            String derniereDateStr = DateFormat.format("dd/MM/yyyy", derniereDate).toString();
            accesLocal.supprimerDate(derniereDateStr);
        }


        //Bouton ajouter
        this.main_ajouter = (ImageView) findViewById(R.id.image_ajouter);
        main_ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activiteAjouter = new Intent(getApplicationContext(), MainActivity3.class);
                startActivity(activiteAjouter);
                finish();
            }
        });//fin ajouter




        //popup option-------------------------------------------------------------

        this.main_option = (ImageView) findViewById(R.id.image_option);
        main_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_option popup = new popup_option(activite);
                popup.setTitle("Options");

                main_switch_affiche = popup.getSwitch_list();

                String valSer = String.valueOf(Serializer.deSerialize("EtatSwitch", contexte));
                int etat = Integer.parseInt(valSer);
                if(etat==0){
                    main_switch_affiche.setChecked(false);
                    main_switch_affiche.setText("Aujourd\'hui");
                }
                else{
                    main_switch_affiche.setChecked(true);
                    main_switch_affiche.setText("Tous");
                }

                main_switch_affiche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked)
                            main_switch_affiche.setText("Tous");
                        else
                            main_switch_affiche.setText("Aujourd\'hui");

                    }
                });//fin switch_affiche onChanged




                main_textView_selectDateSupp = popup.getTextView_SuppAuto();
                main_switch_suppAuto = popup.getSwitch_suppAuto();

                String valSuppAuto = String.valueOf(Serializer.deSerialize("EtatSwitchSuppAuto", contexte));
                int etatSwitchSuppAuto = Integer.parseInt(valSuppAuto);

                if(etatSwitchSuppAuto==0){
                    main_switch_suppAuto.setChecked(false);
                    main_textView_selectDateSupp.setEnabled(false);
                    main_textView_selectDateSupp.setHint("Désactivé");
                }
                else{
                    main_switch_suppAuto.setChecked(true);
                    main_textView_selectDateSupp.setEnabled(true);

                    String dernierJour = String.valueOf(Serializer.deSerialize("DateSupp", contexte));
                    main_textView_selectDateSupp.setText(dernierJour);
                }


                main_switch_suppAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked) {
                            Serializer.serialize("EtatSwitchSuppAuto", 1, contexte);
                            main_textView_selectDateSupp.setEnabled(true);
                            main_textView_selectDateSupp.setHint("Nombre de jour");
                            String dernierJour = String.valueOf(Serializer.deSerialize("DateSupp", contexte));
                            main_textView_selectDateSupp.setText(dernierJour);


                        } else {
                            Serializer.serialize("EtatSwitchSuppAuto", 0, contexte);
                            main_textView_selectDateSupp.setHint("Désactivé");
                            main_textView_selectDateSupp.setEnabled(false);
                        }//fin if
                    }
                });//fin switch_affiche onChanged


                main_btn_supp = popup.getToutSupprimer();

                main_btn_supp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder myPopup = new AlertDialog.Builder(activite);
                        myPopup.setTitle("Attention");

                        myPopup.setMessage("Voulez-vous supprimer toute la base de données ?");

                        myPopup.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                accesLocal.supprimerAll();
                                Toast.makeText(getApplicationContext(), "Base de données supprimé", Toast.LENGTH_SHORT).show();
                            }
                        });//fin positiveButton

                        myPopup.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), "Annulé", Toast.LENGTH_SHORT).show();
                            }
                        });//fin negative

                        myPopup.show();

                    }
                });//fin btn_supp




                popup.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Toast.makeText(getApplicationContext(), "OUI", Toast.LENGTH_SHORT).show();

                        if(main_switch_affiche.isChecked()) {
                            Serializer.serialize("EtatSwitch", 1, contexte);

                        } else {
                            Serializer.serialize("EtatSwitch", 0, contexte);

                        }//fin if
                        eventAdapt.notifyDataSetChanged();//actualisation de la listView

                        liste.clear();
                        AfficheListe(dateJ);

                        String suppAuto = String.valueOf(Serializer.deSerialize("EtatSwitchSuppAuto", contexte));
                        int etatSuppAuto = Integer.parseInt(suppAuto);
                        if(etatSuppAuto == 1) {
                            String alpha = main_textView_selectDateSupp.getText().toString();
                            int beta = Integer.valueOf(alpha);

                            Serializer.serialize("DateSupp", beta, contexte);
                        }

                        popup.dismiss();
                    }
                });

                popup.getNoButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                    }
                });

                popup.build();
            }
        });





        //Layout -----------------------------------------------------------------------

        this.main_btn_rc = (Button) findViewById(R.id.btn_recherche);
        main_btn_rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textTest = String.valueOf(accesLocal.NEvenement());
                Toast.makeText(getApplicationContext(), textTest, Toast.LENGTH_SHORT).show();

            }
        });//fin btn_rc



        AfficheListe(dateJ);

        eventAdapt = new EvenementAdapter(this, liste);


        //click sur un evenement
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View view, int position, long id) {
                Serializer.serialize("positionListe", position, contexte);
                startActivity(new Intent(getApplicationContext(), MainActivity4.class));
                finish();
            }
        });//fin onItemClick

        /*//actualisation a chaque click
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventAdapt.notifyDataSetChanged();
                AfficheListe(dateJ);
            }
        };*/

        this.main_event_supp = (ImageView) findViewById(R.id.imageSupp);
        main_event_supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liste.clear();
                //eventAdapt.notifyDataSetChanged();
                AfficheListe(dateJ);
            }
        });

        eventListView.setAdapter(eventAdapt);

    }//fin onCreate


    public void AfficheListe(String dateJ){

        String valSer = String.valueOf(Serializer.deSerialize("EtatSwitch", contexte));
        int etat = Integer.parseInt(valSer);

        if(etat == 0){

            for(Evenement event : accesLocal.recupAllDate(dateJ)){
                liste.add(event);
            };
        }
        else{
            for(Evenement event : accesLocal.recupAll()){
                liste.add(event);
            };
        }
    }//fin AfficheListe

}//fin class