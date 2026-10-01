package com.example.projet_agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_agenda.bdd.AccesLocal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//View de crearion

public class MainActivity3 extends AppCompatActivity {

    private TextView main_textView_TBar_titre, main_textView_selectDate;
    private Button main_btn_annuler, main_btn_ajouter, main_btn_selectImage;
    private EditText main_edit_titre, main_edit_contenu, main_edit_tag, main_edit_lien;
    private Context contexte = this;
    private AccesLocal accesLocal;
    private String strTitre, strContenu, strTag, strLien, date=" ";
    private ImageView image_fleche_retour, image_modifier, imageTest;
    private Bitmap bitmap_selectedImage;

    static final int RESULT_LOAD_IMG = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        accesLocal = new AccesLocal(contexte);


        //Barre de menu -------------------------------------------------------------

        this.image_fleche_retour = (ImageView) findViewById(R.id.fleche_retour);
        image_fleche_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activiteHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activiteHome);
                finish();
            }
        });//fin fleche_retour

        this.main_textView_TBar_titre = (TextView) findViewById(R.id.textView_TBar_titre);
        main_textView_TBar_titre.setText("Nouveau");

        this.image_modifier  = (ImageView) findViewById(R.id.image_edit);
        image_modifier.setVisibility(View.INVISIBLE);


        //Layout -----------------------------------------------------------------------

        accesLocal = new AccesLocal(contexte);

        main_textView_selectDate = (TextView) findViewById(R.id.textView_select_date);

        final Calendar cal = Calendar.getInstance();
        final int annee = cal.get(Calendar.YEAR);
        final int mois = cal.get(Calendar.MONTH);
        final int jour = cal.get(Calendar.DAY_OF_MONTH);

        main_textView_selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity3.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int annee, int mois, int jour) {
                        mois = mois +1;
                        if(jour >= 1 && jour <= 9)
                            date = "0"+jour+"/"+mois+"/"+annee;
                        else
                            date = jour+"/"+mois+"/"+annee;

                        main_textView_selectDate.setText(date);
                    }
                }, annee, mois, jour);
                dialog.show();
            }
        });//fin selectDate


        this.main_btn_selectImage = (Button) findViewById(R.id.edit_lien);
        main_btn_selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });//fin selectImage


        this.imageTest = (ImageView) findViewById(R.id.imageView_test);



        this.main_btn_ajouter = (Button) findViewById(R.id.btn_ajouter);
        main_btn_ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                createDirectoryAndSaveFile(bitmap_selectedImage, System.currentTimeMillis()+"");

                //MediaStore.Images.Media.insertImage(getContentResolver(), selectedImage, System.currentTimeMillis()+"", "Description");

                main_edit_titre = (EditText) findViewById(R.id.edit_titre);
                main_edit_contenu = (EditText) findViewById(R.id.edit_contenu);
                main_edit_tag = (EditText) findViewById(R.id.edit_tag);
                //main_edit_lien = (EditText) findViewById(R.id.edit_lien);


                strTitre = main_edit_titre.getText().toString();
                strContenu = main_edit_contenu.getText().toString();
                strTag = main_edit_tag.getText().toString();
                strLien = System.currentTimeMillis()+".jpg";

                if(strTitre == " " || strContenu == " " || strTag == " " || date == " "){
                    Toast.makeText(getApplicationContext(), "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                }
                else {
                    Evenement event = new Evenement(strTitre, strContenu, strTag, date, strLien);
                    accesLocal.ajout(event);


                    Intent activiteHome = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(activiteHome);
                    finish();

                    Toast.makeText(getApplicationContext(), "Ajouté", Toast.LENGTH_SHORT).show();
                }
            }
        });//fin ajouter


    }//fin onCreate


    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap_selectedImage = BitmapFactory.decodeStream(imageStream);
                imageTest.setImageBitmap(bitmap_selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Une erreur s'est produite",Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(),"Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();

        }
    }//fin onActivityResult

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/planningImage");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/planningImage/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File("/sdcard/planningImage/", fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}//fin class