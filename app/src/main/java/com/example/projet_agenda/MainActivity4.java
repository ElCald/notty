package com.example.projet_agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.projet_agenda.bdd.AccesLocal;

import org.w3c.dom.Text;

import java.io.File;
import java.io.Serializable;
import java.util.Date;


//Affichage de l'evenement selectionne

public class MainActivity4 extends AppCompatActivity implements Serializable {

    private ImageView image_fleche_retour, main_image_ImageView, image_modifier;
    private Toolbar main_toolbar_barreTache;
    private TextView main_textView_TBar_titre, main_textView_contenu, main_textView_date;
    private Context contexte = this;
    private AccesLocal accesLocal;
    private Evenement event;
    private String titre, contenu, date, image;
    private Button main_btn_testBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        String SCAN_PATH;
        File[] allFiles ;


        Date dateJour = new Date();
        String dateJ = DateFormat.format("dd/MM/yyyy", dateJour).toString();

        String valSer = String.valueOf(Serializer.deSerialize("positionListe", contexte));
        int pos = Integer.parseInt(valSer);

        this.main_toolbar_barreTache = (Toolbar) findViewById(R.id.TBar);
        setSupportActionBar(main_toolbar_barreTache);

        accesLocal = new AccesLocal(contexte);

        this.main_textView_TBar_titre = (TextView) findViewById(R.id.textView_TBar_titre);
        this.main_textView_contenu = (TextView) findViewById(R.id.textView_contenu);
        this.main_textView_date = (TextView) findViewById(R.id.textView_date);
        this.main_image_ImageView = (ImageView) findViewById(R.id.image_imageView);
        this.image_modifier = (ImageView) findViewById(R.id.image_edit);

        image_modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activite2 = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(activite2);
                finish();
            }
        });

        String valEtat = String.valueOf(Serializer.deSerialize("EtatSwitch", contexte));
        int etat = Integer.parseInt(valEtat);

        if(etat==0) {
            //Recup l'event depuis la liste cree a une certaine pos
            titre = accesLocal.recupAllDate(dateJ).get(pos).getTitre();
            contenu = accesLocal.recupAllDate(dateJ).get(pos).getContenu();
            date = accesLocal.recupAllDate(dateJ).get(pos).getDate();
            image = accesLocal.recupAllDate(dateJ).get(pos).getImage();
        }
        else{
            titre = accesLocal.recupAll().get(pos).getTitre();
            contenu = accesLocal.recupAll().get(pos).getContenu();
            date = accesLocal.recupAll().get(pos).getDate();
            image = accesLocal.recupAll().get(pos).getImage();
        }

        //Set les textview
        main_textView_TBar_titre.setText(titre);
        main_textView_contenu.setText(contenu);
        main_textView_date.setText(date);
        main_image_ImageView.setImageBitmap(BitmapFactory.decodeFile(image));


        main_textView_contenu.setMovementMethod(new ScrollingMovementMethod());



        File folder = new File(Environment.getExternalStorageDirectory().getPath()+"/sdcard/planningImage/");
        allFiles = folder.listFiles();


        this.main_btn_testBtn = (Button) findViewById(R.id.testImgBtn);
                main_btn_testBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        SingleMediaScanner sms = new SingleMediaScanner(MainActivity4.this, allFiles[0]);
                    }
                });


        this.image_fleche_retour = (ImageView) findViewById(R.id.fleche_retour);
        image_fleche_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activite1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activite1);
                finish();
            }
        });//fin onClick fleche_retour


    }//fin oncreate

    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        public void onScanCompleted(String path, Uri uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
            mMs.disconnect();
        }

    }


}//fin class